package com.norbo.android.projects.rssolvaso.acutils.weather;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.norbo.android.projects.rssolvaso.model.weather.Weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public interface WeatherInterface {
    /**
     * Jelen fejlesztési szakaszban, ezzel frissítem a saját appbar tartalmát
     * @param imageView
     * @param textView
     * @param clickbyikon
     */
    void doWeather(ImageView imageView, TextView textView, boolean clickbyikon);
    Weather getWeather(boolean clickbyikon);

    void setUserLat(Double userLat);
    void setUserLon(Double userLon);

    default String getStringFromstream(@NonNull HttpURLConnection con) throws IOException {
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
