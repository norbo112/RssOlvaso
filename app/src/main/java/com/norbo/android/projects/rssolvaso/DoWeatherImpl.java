package com.norbo.android.projects.rssolvaso;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.norbo.android.projects.rssolvaso.acutils.weather.WeatherInterface;
import com.norbo.android.projects.rssolvaso.model.weather.WData;
import com.norbo.android.projects.rssolvaso.model.weather.Weather;

import java.net.HttpURLConnection;
import java.net.URL;

public class DoWeatherImpl implements WeatherInterface {
    private ProgressDialog progressBar;

    private String baseURL = "https://api.weatherbit.io/v2.0/";
    private String iconLink = "https://www.weatherbit.io/static/img/icons/";
//    private final String KEY = "9de6d03367e74f4f9a046a275ceb5741";
    private final String KEY = "dab36f15298041359ad86856a878f82b";
    private final double LAT = 47.4706;
    private final double LON = 18.81892;
    private final String LANG = "hu";

    private Double userLat;
    private Double userLon;

    private Activity context;

    public DoWeatherImpl(Activity context) {
        this.context = context;
    }

    @Override
    public void doWeather(ImageView imageView, TextView textView, boolean clickbyikon) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(clickbyikon) {
                    context.runOnUiThread(() -> {
                        if (progressBar == null) {
                            progressBar = new ProgressDialog(context);
                            progressBar.setTitle("Betöltés...");
                            progressBar.setMessage("Időjárás betöltése");
                        }

                        progressBar.show();
                    });
                }

                String linkplusQuery = baseURL+"current?"+
                        "lat="+(userLat != null ? userLat : LAT)+"&"+
                        "lon="+(userLon != null ? userLon : LON)+"&"+
                        "lang="+LANG+"&"+
                        "key="+KEY;
                try {
                    HttpURLConnection con = (HttpURLConnection) new URL(linkplusQuery)
                            .openConnection();
                    if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        String sb = getStringFromstream(con);

                        Weather weather = new Gson().fromJson(sb, Weather.class);

                        final URL url = new URL(iconLink+weather.getData().get(0).getWeather().getIcon()+".png");
                        final Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        context.runOnUiThread(() -> {
                            WData wData = weather.getData().get(0);
                            imageView.setImageBitmap(bitmap);
                            textView.setText(wData.getCity_name()+" "+wData.getTemp()+" °C");
                        });

                        con.disconnect();
                    } else {
                        context.runOnUiThread(() ->{
                            Toast.makeText(context,
                                    "Nincsenek meg a kért adatok", Toast.LENGTH_SHORT).show();
                        });
                        Log.i(getClass().getSimpleName(), "Connection error: "+con.getResponseCode());
                    }
                } catch (Exception e) {
                    context.runOnUiThread(() -> {
                        Toast.makeText(context, "Hiba lépett fel", Toast.LENGTH_SHORT).show();
                    });
                    Log.e(getClass().getSimpleName(), "run: i/o hiba", e);
                }

                context.runOnUiThread(() -> {
                    if(clickbyikon)  progressBar.dismiss();
                });
            }
        }).start();
    }

    @Override
    public void setUserLat(Double userLat) {
        this.userLat = userLat;
    }

    @Override
    public void setUserLon(Double userLon) {
        this.userLon = userLon;
    }
}
