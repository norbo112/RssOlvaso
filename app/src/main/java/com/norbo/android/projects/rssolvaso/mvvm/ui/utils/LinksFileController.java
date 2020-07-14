package com.norbo.android.projects.rssolvaso.mvvm.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;

@ActivityContext
public class LinksFileController {
    private static final String TAG = "LinksFileController";
    public static final int FILE_LOAD_RCODE = 100;
    private Context context;

    @Inject
    public LinksFileController(@ActivityContext Context context) {
        this.context = context;
    }

    /**
     * StartActivityForResult - ez az indító félnél kezelendő
     * @param activity
     */
    public void showFileChooser(Activity activity) {
        Intent filechooser = new Intent(Intent.ACTION_GET_CONTENT);
        filechooser.setType("*/*");
        filechooser.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/octet-stream", "application/json"});
        filechooser.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(filechooser, FILE_LOAD_RCODE);
    }

    private List<Link> linksFromFile(Uri data) throws JsonSyntaxException, FileNotFoundException {
        if(data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        String source = loadFile(context.getContentResolver().openInputStream(data));
        Type type = new TypeToken<List<Link>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(source, type);
    }

    private String loadFile(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "loadFile: ", e);
        }
        return sb.toString();
    }

    public List<Link> loadLinks(@Nullable Intent data) {
        List<Link> links = new ArrayList<>();
        try {
            return linksFromFile(data.getData());
        } catch (FileNotFoundException e) {
            Log.e(TAG, "onActivityResult: notFound", e);
            Toast.makeText(context, "Adatok betöltése sikertelen volt :(", Toast.LENGTH_SHORT).show();
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "onActivityResult: JSON Syntax", e);
            Toast.makeText(context, "Adatok betöltése sikertelen volt :(", Toast.LENGTH_SHORT).show();
        }

        return links;
    }
}
