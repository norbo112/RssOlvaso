package com.norbo.android.projects.rssolvaso.model.weather;

import android.graphics.Bitmap;

import java.util.List;

public class Weather {
    private String count;
    private Bitmap wicon;
    private List<WData> data;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<WData> getData() {
        return data;
    }

    public void setData(List<WData> data) {
        this.data = data;
    }

    public Bitmap getWicon() {
        return wicon;
    }

    public void setWicon(Bitmap wicon) {
        this.wicon = wicon;
    }
}
