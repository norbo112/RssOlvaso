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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.norbo.android.projects.rssolvaso.acutils.weather.WeatherInterface;

public class LoactionUtil {
    private static final int LOCATION_PERM = 5001;
    private static final String TAG = "LocationUtil";

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void getLastLocationAndUpdateWeatherData(LocationInterfaceActivity main, LocationManager locationManager,
                                                           WeatherInterface weatherActivity, boolean clicked) {
        if (ActivityCompat.checkSelfPermission((Activity) main, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission((Activity) main, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) main, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_PERM);
            Log.i(TAG, "getLastLocationAndUpdateWeatherData: permission not granted");
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

    /**
     * Utolsó ismert pozició lekérése a google.play service használatával
     * @param main LocationInterfaceActivity -t megvalósító osztály, evvel kapcsolódik a saját
     *             appbaron lévő szöveggel és képpel
     * @param weatherActivity osztályként használt, időjárás lekérő és frissitő weatheractivity
     * @param clicked kattintáskor előjön a loading -bar...
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void updateLocationWithFusedLPC(LocationInterfaceActivity main,
                                                            WeatherInterface weatherActivity,
                                                            boolean clicked) {
        if (ActivityCompat.checkSelfPermission((Activity) main, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission((Activity) main, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) main, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_PERM);
            Log.i(TAG, "updateLocationWithFusedLPC: permission not granted");
            return;
        }

        FusedLocationProviderClient fusedLocClient = LocationServices.getFusedLocationProviderClient((Activity) main);

        fusedLocClient.getLastLocation().addOnSuccessListener((Activity) main, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    weatherActivity.setUserLat(location.getLatitude());
                    weatherActivity.setUserLon(location.getLongitude());
                    Log.i(TAG, "updateLocationWithFusedLPC: not null: "
                            +location.getLatitude()+" : "+location.getLongitude());
                }
            }
        });

        weatherActivity.doWeather(main.getImIcon(), main.getTvDesc(), clicked);
    }
}