package com.norbo.android.projects.rssolvaso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.norbo.android.projects.rssolvaso.controller.RssController;
import com.norbo.android.projects.rssolvaso.database.viewmodel.HirSaveViewModel;
import com.norbo.android.projects.rssolvaso.model.RssItem;
import com.norbo.android.projects.rssolvaso.rcview.RssItemAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RssActivity extends AppCompatActivity {
    private WeatherActivity weatherActivity;
    private HirSaveViewModel hirSaveViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        weatherActivity = new WeatherActivity(this);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Hír olvasó");
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setLogo(R.drawable.ic_rss_feed_black_24dp);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_appbar_layout);
        getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        ImageView imIcon = view.findViewById(R.id.weather_logo);
        TextView tvDesc = view.findViewById(R.id.weather_info);
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
        weatherActivity.doWeather(imIcon, tvDesc);
        imIcon.setOnClickListener((event) -> {
            weatherActivity.doWeather(imIcon, tvDesc);
        });

        final RecyclerView rv = findViewById(R.id.rvRssHir);
        final String link = getIntent().getStringExtra("link");

        TextView tvCsatornaCim = findViewById(R.id.tvCsatornaCim);
        tvCsatornaCim.setText(getIntent().getStringExtra("cim"));

        final RssController rssController = new RssController(this);
        hirSaveViewModel = new ViewModelProvider(this).get(HirSaveViewModel.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rssController.showProgress();
                    }
                });

                final List<RssItem> rssItems = rssController.getRssItems(link);
                if(rssItems != null) {
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
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
