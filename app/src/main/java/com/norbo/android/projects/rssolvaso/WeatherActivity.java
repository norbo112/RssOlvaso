package com.norbo.android.projects.rssolvaso;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.norbo.android.projects.rssolvaso.model.weather.WData;
import com.norbo.android.projects.rssolvaso.model.weather.Weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {
    ProgressDialog progressBar;

    private String baseURL = "https://api.weatherbit.io/v2.0/";
    private String iconLink = "https://www.weatherbit.io/static/img/icons/";
//    private final String KEY = "9de6d03367e74f4f9a046a275ceb5741";
    private final String KEY = "dab36f15298041359ad86856a878f82b";
    private final double LAT = 47.4706;
    private final double LON = 18.81892;
    private final String LANG = "hu";

    private Double userLat;
    private Double userLon;

    private Context context;

    TextView tvcityName;
    TextView tvFok;
    TextView tvSzel;
    TextView tvDesc;
    ImageView imIcon;

    public WeatherActivity(Context context) {
        this.context = context;
    }

    public WeatherActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Időjárás");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.ic_rss_feed_black_24dp);

        tvcityName = findViewById(R.id.tvCityName);
        tvFok = findViewById(R.id.tvFok);
        tvSzel = findViewById(R.id.tvSzel);
        tvDesc = findViewById(R.id.tvDesc);
        imIcon = findViewById(R.id.imIcon);

        doWeather(tvcityName, tvFok, tvSzel, tvDesc, imIcon);
    }

    void doWeather(TextView tvcityName, TextView tvFok, TextView tvSzel, TextView tvDesc,
                    ImageView imIcon) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if(progressBar == null) {
                        progressBar = new ProgressDialog(context != null ? context : WeatherActivity.this);
                        progressBar.setTitle("Betöltés...");
                    }
                    progressBar.show();
                });
                String linkplusQuery = baseURL+"current?"+
                        "lat="+LAT+"&"+
                        "lon="+LON+"&"+
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
                        runOnUiThread(() -> {
                            imIcon.setImageBitmap(bitmap);
                            tvcityName.setText(weather.getData().get(0).getCity_name());
                            tvFok.setText(weather.getData().get(0).getTemp()+" °C");
                            tvSzel.setText(weather.getData().get(0).getWind_speed()+" m/s");
                            tvDesc.setText(weather.getData().get(0).getWeather().getDescription());
                        });

                        con.disconnect();
                    } else {
                        runOnUiThread(() ->{
                            Toast.makeText(WeatherActivity.this,
                                    "Nincsenek meg a kért adatok", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    progressBar.dismiss();
                });
            }
        }).start();
    }

    public void doWeather(ImageView imageView, TextView textView, boolean clickbyikon) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(clickbyikon) {
                    runOnUiThread(() -> {
                        if (progressBar == null) {
                            progressBar = new ProgressDialog(context != null ? context : WeatherActivity.this);
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
                        runOnUiThread(() -> {
                            WData wData = weather.getData().get(0);
                            imageView.setImageBitmap(bitmap);
                            textView.setText(wData.getCity_name()+" "+wData.getTemp()+" °C");
                        });

                        con.disconnect();
                    } else {
                        runOnUiThread(() ->{
                            Toast.makeText(context,
                                    "Nincsenek meg a kért adatok", Toast.LENGTH_SHORT).show();
                        });
                        Log.i(getClass().getSimpleName(), "Connection error: "+con.getResponseCode());
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(context, "Hiba lépett fel", Toast.LENGTH_SHORT).show();
                    });
                    Log.e(getClass().getSimpleName(), "run: i/o hiba", e);
                }

                runOnUiThread(() -> {
                    if(clickbyikon)  progressBar.dismiss();
                });
            }
        }).start();
    }

    void doWeather(MenuItem menuItem) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if(progressBar == null) {
                        progressBar = new ProgressDialog(context != null ? context : WeatherActivity.this);
                        progressBar.setTitle("Betöltés...");
                    }
                    progressBar.show();
                });
                String linkplusQuery = baseURL+"current?"+
                        "lat="+LAT+"&"+
                        "lon="+LON+"&"+
                        "lang="+LANG+"&"+
                        "key="+KEY;
                try {
                    HttpURLConnection con = (HttpURLConnection) new URL(linkplusQuery)
                            .openConnection();
                    if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        String sb = getStringFromstream(con);

                        Weather weather = new Gson().fromJson(sb, Weather.class);

                        final String icon = weather.getData().get(0).getWeather().getIcon();
                        runOnUiThread(() -> {
                            WData wData = weather.getData().get(0);
                            menuItem.setTitle(wData.getCity_name()+ " "+wData.getTemp()+" °C");
                            menuItem.setIcon(context.getResources().getIdentifier(icon, "raw", context.getPackageName()));
                        });

                        con.disconnect();
                    } else {
                        runOnUiThread(() ->{
                            Toast.makeText(WeatherActivity.this,
                                    "Nincsenek meg a kért adatok", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    progressBar.dismiss();
                });
            }
        }).start();
    }

    private String getStringFromstream(HttpURLConnection con) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            sb.append(line);
        }

        reader.close();

        return sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fomenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuMentett :
                startActivity(new Intent(getApplicationContext(), SavedHirekActivity.class));
                break;
            case R.id.menuIdojaras :
                doWeather(tvcityName, tvFok, tvSzel, tvDesc, imIcon);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUserLat(Double userLat) {
        this.userLat = userLat;
    }

    public void setUserLon(Double userLon) {
        this.userLon = userLon;
    }
}
