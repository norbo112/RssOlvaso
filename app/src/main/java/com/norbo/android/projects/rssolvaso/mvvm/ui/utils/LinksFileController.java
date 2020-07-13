package com.norbo.android.projects.rssolvaso.mvvm.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.LinkViewModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;
import okio.Utf8;

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

    public List<Link> linksFromFile(Uri data) throws JsonSyntaxException, FileNotFoundException {
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

    public void loadLinks(@Nullable Intent data, LinkViewModel linkViewModel) {
        List<Link> links = null;
        try {
            links = linksFromFile(data.getData());
            if (links.isEmpty()) {
                Toast.makeText(context, "Adatok betöltése sikertelen volt :(", Toast.LENGTH_SHORT).show();
            } else {
                linkViewModel.insert(links);
                Log.i(TAG, "onActivityResult: Adatok betöltve");
                Toast.makeText(context, "Adatok betöltve", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "onActivityResult: notFound", e);
            Toast.makeText(context, "Adatok betöltése sikertelen volt :(", Toast.LENGTH_SHORT).show();
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "onActivityResult: JSON Syntax", e);
            Toast.makeText(context, "Adatok betöltése sikertelen volt :(", Toast.LENGTH_SHORT).show();
        }
    }
}
