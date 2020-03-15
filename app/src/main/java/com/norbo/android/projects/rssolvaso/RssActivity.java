package com.norbo.android.projects.rssolvaso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.norbo.android.projects.rssolvaso.acutils.LoactionUtil;
import com.norbo.android.projects.rssolvaso.acutils.LocationInterfaceActivity;
import com.norbo.android.projects.rssolvaso.controller.RssController;
import com.norbo.android.projects.rssolvaso.database.viewmodel.HirSaveViewModel;
import com.norbo.android.projects.rssolvaso.model.RssItem;
import com.norbo.android.projects.rssolvaso.rcview.RssItemAdapter;

import java.util.List;

public class RssActivity extends AppCompatActivity implements LocationInterfaceActivity {
    private WeatherActivity weatherActivity;
    private HirSaveViewModel hirSaveViewModel;
    private LocationManager lm;
    private RssController rssController;
    private SwipeRefreshLayout swipeRefreshLayout;
    ImageView imIcon;
    TextView tvDesc;

    String link;
    RecyclerView rv;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        weatherActivity = new WeatherActivity(this);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_appbar_layout);
        getSupportActionBar().setElevation(0);

        View view = getSupportActionBar().getCustomView();
        TextView name = view.findViewById(R.id.name);
        imIcon = view.findViewById(R.id.weather_logo);
        tvDesc = view.findViewById(R.id.weather_info);
        ImageView applogohome = view.findViewById(R.id.applogo_home);
        applogohome.setOnClickListener((event) -> {
            Intent intent = new Intent(RssActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        ImageView appSavedHirek = view.findViewById(R.id.viewSaveHirek);
        appSavedHirek.setOnClickListener((event) -> {
            startActivity(new Intent(getApplicationContext(), SavedHirekActivity.class));
        });
        LoactionUtil.getLastLocationAndUpdateWeatherData(RssActivity.this, lm, weatherActivity, false);
        imIcon.setOnClickListener((event) -> {
            LoactionUtil.getLastLocationAndUpdateWeatherData(RssActivity.this, lm, weatherActivity, true);
        });

        rv = findViewById(R.id.rvRssHir);
        link = getIntent().getStringExtra("link");

//        TextView tvCsatornaCim = findViewById(R.id.tvCsatornaCim);
        name.setText(getIntent().getStringExtra("cim"));

        rssController = new RssController(this);
        hirSaveViewModel = new ViewModelProvider(this).get(HirSaveViewModel.class);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(swipeHirekBetoltese).start();
            }
        });


        new Thread(hirekBetoltese).start();
    }

    final Runnable hirekBetoltese = new Runnable() {
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
                    }
                });
            }
        }
    };

    final Runnable swipeHirekBetoltese = new Runnable() {
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
