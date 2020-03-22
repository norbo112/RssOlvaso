package com.norbo.android.projects.rssolvaso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.norbo.android.projects.rssolvaso.acutils.LoactionUtil;
import com.norbo.android.projects.rssolvaso.acutils.LocationInterfaceActivity;
import com.norbo.android.projects.rssolvaso.acutils.weather.DoWeatherImpl;
import com.norbo.android.projects.rssolvaso.controller.RssController;
import com.norbo.android.projects.rssolvaso.database.viewmodel.HirSaveViewModel;
import com.norbo.android.projects.rssolvaso.model.RssItem;
import com.norbo.android.projects.rssolvaso.rcview.RssItemAdapter;

import java.util.List;

public class RssActivity extends AppCompatActivity implements LocationInterfaceActivity {
    private DoWeatherImpl doWeatherImpl;
    private HirSaveViewModel hirSaveViewModel;
    private LocationManager lm;
    private RssController rssController;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int lastItemPoz;
    ImageView imIcon;
    TextView tvDesc;

    private View rootView;
    private FloatingActionButton scrollUpFab;

    String link;
    RecyclerView rv;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_activity_start);

        Toolbar toolbar = findViewById(R.id.rss_toolbar);
        toolbar.setTitle(getIntent().getStringExtra("cim"));
        toolbar.setLogo(R.drawable.ic_rss_feed_black_24dp);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        doWeatherImpl = new DoWeatherImpl(this);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        rv = findViewById(R.id.rvRssHir);
        link = getIntent().getStringExtra("link");

        rssController = new RssController(this);
        hirSaveViewModel = new ViewModelProvider(this).get(HirSaveViewModel.class);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_dark
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(swipeHirekBetoltese).start();
            }
        });

        new Thread(hirekBetoltese).start();
        rootView = findViewById(R.id.rootView);
        scrollUpFab = findViewById(R.id.scrollUpFab);

        EditText searchText = findViewById(R.id.rssa_search);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(rv.getAdapter() != null) {
                    ((RssItemAdapter) rv.getAdapter()).getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setRecycleScrollUp() {
        lastItemPoz = ((LinearLayoutManager) rv.getLayoutManager()).findFirstVisibleItemPosition();;
        rv.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                System.out.println(":::::::: scrolllistener");
                final int visiblePoz = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if(visiblePoz > lastItemPoz) {
                    scrollUpFab.show();
                    scrollUpFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recyclerView.getLayoutManager()
                                    .scrollToPosition(0);
                            scrollUpFab.hide();
                        }
                    });
                }
            }
        });
    }

    final Runnable hirekBetoltese = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rssController.showProgress();
                }
            });

            final List<RssItem> rssItems = rssController.getRssItems(link);
            if (rssItems != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rssController.dismissProgress();
                        rv.setAdapter(new RssItemAdapter(RssActivity.this, rssItems, hirSaveViewModel));
                        rv.setItemAnimator(new DefaultItemAnimator());
                        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        setRecycleScrollUp();
                    }
                });
            }
        }
    };

    final Runnable swipeHirekBetoltese = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            final List<RssItem> rssItems = rssController.getRssItems(link);

            if(rssItems != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        RssItemAdapter itemAdapter = (RssItemAdapter) rv.getAdapter();
                        itemAdapter.clear();
                        itemAdapter.addAll(rssItems);
                    }
                });
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fomenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_saved : {
                startActivity(new Intent(this, SavedHirekActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public ImageView getImIcon() {
        return imIcon;
    }

    @Override
    public TextView getTvDesc() {
        return tvDesc;
    }
}
