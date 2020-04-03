package com.norbo.android.projects.rssolvaso.acutils.graphicutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.norbo.android.projects.rssolvaso.model.weather.WData;
import com.norbo.android.projects.rssolvaso.model.weather.Weather;

public class DrawIdojaraToImage {
    private Context context;
    private Bitmap srcBitmap;

    public DrawIdojaraToImage(Context context, Bitmap srcBitmap) {
        this.context = context;
        this.srcBitmap = srcBitmap;
    }

    public Bitmap drawWeather(Weather weather) {
        int padding = 200;

        Bitmap origIamgeBitmap = srcBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap newBitmap = Bitmap.createBitmap(origIamgeBitmap.getWidth(), origIamgeBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(origIamgeBitmap, 0, 0, null);

        if(weather != null) {
            Paint textPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(Color.BLACK);

            Bitmap imageBitmap = weather.getWicon().copy(Bitmap.Config.ARGB_8888, true);
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 250,250, false);
            drawWeatherToBitmap(weather.getData().get(0), padding, textPaint, imageBitmap, canvas);
        } else {
            drawTextToBitmap("Sajnos nincs információm az időjárásról", canvas);
        }

        return newBitmap;
    }

    private void drawWeatherToBitmap(WData wData, int padding, Paint textPaint, Bitmap imageBitmap, Canvas canvas) {
        int imagex = imageBitmap.getWidth()+padding;
        canvas.drawBitmap(imageBitmap,padding,pxFromDp(context, 110) , null);

        textPaint.setTextSize(pxFromDp(context, 30));
        canvas.drawText(wData.getTemp()+" °C / "+wData.getApp_temp()+" °C", imagex+20
                , pxFromDp(context, 110) + 100, textPaint);
        textPaint.setTextSize(pxFromDp(context, 20));
        Rect textBounds = new Rect();
        textPaint.getTextBounds(wData.getCity_name(),0, wData.getCity_name().length(), textBounds);
        canvas.drawText(wData.getCity_name(), imagex+50
                ,pxFromDp(context, 130)+120,textPaint);
        textPaint.setTextSize(pxFromDp(context, 20));
        textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        canvas.drawText(", "+wData.getWeather().getDescription(), textBounds.width()+imagex+55,pxFromDp(context, 130)+120,textPaint);
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
