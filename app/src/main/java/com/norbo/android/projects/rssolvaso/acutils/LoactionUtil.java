package com.norbo.android.projects.rssolvaso.acutils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.norbo.android.projects.rssolvaso.WeatherActivity;

public class LoactionUtil {
    private static final int LOCATION_PERM = 5001;
    private static final String TAG = "LocationUtil";

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void getLastLocationAndUpdateWeatherData(LocationInterfaceActivity main, LocationManager locationManager,
                                                           WeatherActivity weatherActivity, boolean clicked) {
        if (ActivityCompat.checkSelfPermission((Activity) main, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission((Activity) main, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) main, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_PERM);
            return;
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(lastKnownLocation != null) {
            weatherActivity.setUserLat(lastKnownLocation.getLatitude());
            weatherActivity.setUserLon(lastKnownLocation.getLongitude());
            Log.i(TAG, "getLastLocationAndUpdateWeatherData: not null: "
                    +lastKnownLocation.getLatitude()+" : "+lastKnownLocation.getLongitude());
        }

        weatherActivity.doWeather(main.getImIcon(), main.getTvDesc(), clicked);
    }
}
