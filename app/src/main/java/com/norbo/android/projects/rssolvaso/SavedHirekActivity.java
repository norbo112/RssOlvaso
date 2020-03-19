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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.norbo.android.projects.rssolvaso.acutils.LoactionUtil;
import com.norbo.android.projects.rssolvaso.acutils.LocationInterfaceActivity;
import com.norbo.android.projects.rssolvaso.database.viewmodel.HirSaveViewModel;
import com.norbo.android.projects.rssolvaso.rcview.SavedHirAdapter;

public class SavedHirekActivity extends AppCompatActivity implements LocationInterfaceActivity {
    private HirSaveViewModel hirSaveViewModel;
    private DoWeatherImpl doWeatherImpl;
    private LocationManager locationManager;
    ImageView imIcon;
    TextView tvDesc;
    RecyclerView rc;
    FloatingActionButton scrollUpFab;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_hirek);

        doWeatherImpl = new DoWeatherImpl(this);
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
        LoactionUtil.updateLocationWithFusedLPC(SavedHirekActivity.this,
                doWeatherImpl, false);
        imIcon.setOnClickListener((event) -> {
            LoactionUtil.updateLocationWithFusedLPC(SavedHirekActivity.this,
                    doWeatherImpl, true);
        });

        rc = findViewById(R.id.rvSavedHrek);
        scrollUpFab = findViewById(R.id.savedTotopFAB);

        hirSaveViewModel = new ViewModelProvider(this).get(HirSaveViewModel.class);
        hirSaveViewModel.getHirek().observe(this, hirModels -> {
            rc.setAdapter(new SavedHirAdapter(hirModels, getApplicationContext(), hirSaveViewModel));
            rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rc.setItemAnimator(new DefaultItemAnimator());

            setRecycleScrollUp();
        });

        EditText searchText = findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((SavedHirAdapter)rc.getAdapter()).getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setRecycleScrollUp() {
        int lastItemPoz = ((LinearLayoutManager) rc.getLayoutManager()).findFirstVisibleItemPosition();;
        rc.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
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

    @Override
    public ImageView getImIcon() {
        return imIcon;
    }

    @Override
    public TextView getTvDesc() {
        return tvDesc;
    }
}
