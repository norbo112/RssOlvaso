package com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ArticleDescriptionImage {
    public static String getImageUrlFromDescription(String description) {
        String result = null;
//        Pattern pattern = Pattern.compile("<img src=\"([^\"]+)\"");
        Pattern pattern = Pattern.compile("src=\"([^\"]+.jpg|png|gif)\"");
        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            result = matcher.group(1);
        }

        if(result != null && result.length() > 0)
            return result;
        else
            return null;
    }
}
