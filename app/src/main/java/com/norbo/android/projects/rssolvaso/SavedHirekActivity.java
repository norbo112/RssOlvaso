package com.norbo.android.projects.rssolvaso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.norbo.android.projects.rssolvaso.database.model.HirModel;
import com.norbo.android.projects.rssolvaso.database.viewmodel.HirSaveViewModel;
import com.norbo.android.projects.rssolvaso.rcview.SavedHirAdapter;

import java.time.LocalDateTime;
import java.util.Comparator;

public class SavedHirekActivity extends AppCompatActivity implements LocationInterfaceActivity {
    private HirSaveViewModel hirSaveViewModel;
    private WeatherActivity weatherActivity;
    private LocationManager locationManager;
    ImageView imIcon;
    TextView tvDesc;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_hirek);

        weatherActivity = new WeatherActivity(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_appbar_layout);
        getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        imIcon = view.findViewById(R.id.weather_logo);
        tvDesc = view.findViewById(R.id.weather_info);
        ImageView applogohome = view.findViewById(R.id.applogo_home);

        applogohome.setOnClickListener((event) -> {
            Intent intent = new Intent(SavedHirekActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        LoactionUtil.getLastLocationAndUpdateWeatherData(SavedHirekActivity.this,
                locationManager, weatherActivity, false);
        imIcon.setOnClickListener((event) -> {
            LoactionUtil.getLastLocationAndUpdateWeatherData(SavedHirekActivity.this,
                    locationManager, weatherActivity, true);
        });
        RecyclerView rc = findViewById(R.id.rvSavedHrek);

        hirSaveViewModel = new ViewModelProvider(this).get(HirSaveViewModel.class);
        hirSaveViewModel.getHirek().observe(this, hirModels -> {
            rc.setAdapter(new SavedHirAdapter(hirModels, getApplicationContext(), hirSaveViewModel));
            rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rc.setItemAnimator(new DefaultItemAnimator());
        });
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
