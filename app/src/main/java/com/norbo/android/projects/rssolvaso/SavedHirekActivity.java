package com.norbo.android.projects.rssolvaso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.norbo.android.projects.rssolvaso.database.viewmodel.HirSaveViewModel;
import com.norbo.android.projects.rssolvaso.rcview.SavedHirAdapter;

public class SavedHirekActivity extends AppCompatActivity {
    private HirSaveViewModel hirSaveViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_hirek);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_appbar_layout);
        getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        ImageView imIcon = view.findViewById(R.id.weather_logo);
        TextView tvDesc = view.findViewById(R.id.weather_info);
        ImageView applogohome = view.findViewById(R.id.applogo_home);
        applogohome.setOnClickListener((event) -> {
            Intent intent = new Intent(SavedHirekActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        new WeatherActivity(this).doWeather(imIcon, tvDesc);
        imIcon.setOnClickListener((event) -> {
            new WeatherActivity(this).doWeather(imIcon, tvDesc);
        });

        RecyclerView rc = findViewById(R.id.rvSavedHrek);

        hirSaveViewModel = new ViewModelProvider(this).get(HirSaveViewModel.class);
        hirSaveViewModel.getHirek().observe(this, hirModels -> {
            rc.setAdapter(new SavedHirAdapter(hirModels, getApplicationContext()));
            rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rc.setItemAnimator(new DefaultItemAnimator());
        });
    }
}
