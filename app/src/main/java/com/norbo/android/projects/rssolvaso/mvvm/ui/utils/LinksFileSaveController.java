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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;

@ActivityContext
public class LinksFileSaveController {
    private static final String TAG = "LinksFileSaveController";
    public static final int DIRECTORY_CHOOSE = 200;
    private Context context;

    @Inject
    public LinksFileSaveController(@ActivityContext Context context) {
        this.context = context;
    }

    public void showDirectoryChooser(Activity activity) {
        Intent dIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        dIntent.addCategory(Intent.CATEGORY_DEFAULT);
        activity.startActivityForResult(dIntent, DIRECTORY_CHOOSE);
    }

    public void saveLinks(List<Link> links, Intent data) {
        Uri uri = data.getData();
        if(uri == null)
            throw new IllegalArgumentException("Uri is null");

        String[] uristr = Objects.requireNonNull(uri.getPath(), "Uri is NULL").split(":");
        String path = uristr[1];
        Log.i(TAG, "saveLinks: path= "+path);

        saveLinksToDirectory(links, path);
    }

    private void saveLinksToDirectory(List<Link> links, String path) {
        String name = "links_"+System.currentTimeMillis()+".json";
        List<LinkToFile> linkToFiles = links.stream()
                .map(link -> new LinkToFile(link.getNev(), link.getLink()))
                .collect(Collectors.toList());
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path, name)))) {
            Gson gson = new Gson();
            writer.append(gson.toJson(linkToFiles));

            Toast.makeText(context, name+" fájl mentése megtörtént.", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "saveLinksToDirectory: nem található a fájl", e);
        } catch (IOException e) {
            Log.e(TAG, "saveLinksToDirectory: I/O hiba", e);
        }
    }
}
