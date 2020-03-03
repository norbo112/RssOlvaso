package com.norbo.android.projects.rssolvaso;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.norbo.android.projects.rssolvaso.model.weather.Weather;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {
    TextView tvcityName;
    TextView tvFok;
    TextView tvSzel;
    TextView tvDesc;
    ImageView imIcon;
    ProgressDialog progressBar;

    private String baseURL = "http://api.weatherbit.io/v2.0/";
    private String iconLink = "https://www.weatherbit.io/static/img/icons/";
    private final String KEY = "9de6d03367e74f4f9a046a275ceb5741";
    private final double LAT = 47.4706;
    private final double LON = 18.81892;
    private final String LANG = "hu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvcityName = findViewById(R.id.tvCityName);
        tvFok = findViewById(R.id.tvFok);
        tvSzel = findViewById(R.id.tvSzel);
        tvDesc = findViewById(R.id.tvDesc);
        imIcon = findViewById(R.id.imIcon);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if(progressBar == null) {
                        progressBar = new ProgressDialog(WeatherActivity.this);
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

//                        JSONObject object = new JSONObject(sb);
//                        JSONArray array = object.getJSONArray("data");
//                        //itt lenne a weather osztály;
//                        System.out.println(array.getJSONObject(0).toString());
//                        //itt meg a weatherben lévő ikon
//                        System.out.println(array.getJSONObject(0).getJSONObject("weather").toString());

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
}
