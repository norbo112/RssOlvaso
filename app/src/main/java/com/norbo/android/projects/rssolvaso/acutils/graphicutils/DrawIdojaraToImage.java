package com.norbo.android.projects.rssolvaso.acutils.graphicutils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.norbo.android.projects.rssolvaso.model.weather.WData;
import com.norbo.android.projects.rssolvaso.model.weather.Weather;

public class DrawIdojaraToImage {
    private Context context;
    private Bitmap srcBitmap;
    private int padding = 200;
    private Bitmap origIamgeBitmap;

    private int scale_width = 300;
    private int scale_height = 300;
    private boolean tablet = false;

    public DrawIdojaraToImage(Context context, Bitmap srcBitmap) {
        this.context = context;
        this.srcBitmap = srcBitmap;
        this.origIamgeBitmap = srcBitmap.copy(Bitmap.Config.ARGB_8888, true);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        System.out.println("displaymetrics: w="+displayMetrics.widthPixels+", h="+displayMetrics.heightPixels);
        if(displayMetrics.widthPixels < 1080 || displayMetrics.heightPixels < 800) {
            this.padding = 50;
            this.scale_width = 150;
            this.scale_height = 150;
            this.tablet = true;
            System.out.println("Tablet True");
        }
    }

    public Bitmap drawWeather(Weather weather) {
        Bitmap newBitmap = Bitmap.createBitmap(origIamgeBitmap.getWidth(), origIamgeBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(origIamgeBitmap, 0, 0, null);

        if(weather != null) {
            Paint textPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(Color.BLACK);

            Bitmap imageBitmap = weather.getWicon().copy(Bitmap.Config.ARGB_8888, true);
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, scale_width,scale_height, false);
            drawWeatherToBitmap(weather.getData().get(0), padding, textPaint, imageBitmap, canvas);
        } else {
            drawTextToBitmap("Sajnos nincs információm az időjárásról", canvas);
        }

        return newBitmap;
    }

    private void drawWeatherToBitmap(WData wData, int padding, Paint textPaint, Bitmap imageBitmap, Canvas canvas) {
        int imagex = imageBitmap.getWidth()+padding;
        float y = tablet ? pxFromDp(context, 100) + 50 : pxFromDp(context, 110) + 100;
        canvas.drawBitmap(imageBitmap,padding,pxFromDp(context, 110) , null);

        textPaint.setTextSize(pxFromDp(context, 30));
        canvas.drawText(wData.getTemp()+" °C / "+wData.getApp_temp()+" °C", imagex+20
                , y, textPaint);
        textPaint.setTextSize(pxFromDp(context, 20));
        Rect textBounds = new Rect();
        textPaint.getTextBounds(wData.getCity_name(),0, wData.getCity_name().length(), textBounds);
        canvas.drawText(wData.getCity_name(), imagex+50
                ,y + (tablet ? 20 : 60),textPaint);
        textPaint.setTextSize(pxFromDp(context, 20));
        textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        canvas.drawText(", "+wData.getWeather().getDescription(), textBounds.width()+imagex+55,y + (tablet ? 20 : 60),textPaint);
    }

    private void drawTextToBitmap(String text, Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setColor(Color.RED);
        paint.setTextSize(pxFromDp(context, 20));
        canvas.drawText(text, 250, pxFromDp(context, 140), paint);
    }

    private static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
