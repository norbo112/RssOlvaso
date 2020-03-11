package com.norbo.android.projects.rssolvaso.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.FileUtils;
import android.util.Log;

import androidx.room.util.FileUtil;

import com.norbo.android.projects.rssolvaso.MainActivity;
import com.norbo.android.projects.rssolvaso.database.model.RssLink;
import com.norbo.android.projects.rssolvaso.model.sajatlv.SajatListViewAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class FileController extends AsyncTask<List<RssLink>, Void, List<RssLink>> {
    private static final String FILENAME = "rsslista.obj";
    private String path;
    public static final int IRAS = 1;
    public static final int OLVASAS = 2;
    public boolean vanefile = true;

    private int fileControllerMode;
    private Context context;

    public FileController(int fileControllerMode, Context context) {
        this.fileControllerMode = fileControllerMode;
        this.context = context;
    }

    public FileController(String path, int fileControllerMode, Context context) {
        this.path = path;
        this.fileControllerMode = fileControllerMode;
        this.context = context;
    }

    @Override
    protected List<RssLink> doInBackground(List<RssLink>... lists) {
        if(fileControllerMode == OLVASAS) {
            List<RssLink> links = fileolvasas(FILENAME);
            return links;
        } else if( fileControllerMode == IRAS) {
            fileiras(FILENAME, lists[0]);
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<RssLink> links) {
        super.onPostExecute(links);
        if(links != null && fileControllerMode == OLVASAS) {
            ((MainActivity) context).getLv().setAdapter(new SajatListViewAdapter(context, links));
            ((MainActivity) context).showToast("Rss Lista betöltve!");
        } else if(!vanefile){
            ((MainActivity) context).showToast("Nem található a fájl!");
        } else {
            ((MainActivity) context).showToast("Rss Lista mentve!");
        }

    }

    private List<RssLink> fileolvasas(String filename) {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(
                    new FileInputStream(new File(
                    context.getExternalFilesDir(null),FILENAME)));
            return (List<RssLink>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            vanefile = false;
            Log.e("FileController","fileolvasas FNF",e);
        } catch (IOException e) {
            Log.e("FileController","fileolvasas",e);
        } catch (ClassNotFoundException e) {
            Log.e("FileController","fileolvasas",e);
        } finally {
            try {
                if(objectInputStream != null) objectInputStream.close();
            } catch (IOException e) {
                Log.e("FileController","fileolvasas",e);
            }
        }

        return null;
    }

    @SuppressLint("WorldReadableFiles")
    private void fileiras(String filename, List<RssLink> links) {
        ObjectOutputStream objectOutputStream = null;
        try {
//            objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(path, filename)));
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(
                    context.getExternalFilesDir(null),FILENAME)));
            objectOutputStream.writeObject(links);
        } catch (FileNotFoundException e) {
            Log.e("FileController","fileolvasas nem található a fájl",e);
        } catch (IOException e) {
            Log.e("FileController","fileolvasas",e);
        } finally {
            try {
                if(objectOutputStream != null) objectOutputStream.close();
            } catch (IOException e) {
                Log.e("FileController","fileolvasas",e);
            }
        }
    }
}
