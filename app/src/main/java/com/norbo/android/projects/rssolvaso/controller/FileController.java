package com.norbo.android.projects.rssolvaso.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
    public static final int IRAS = 1;
    public static final int OLVASAS = 2;

    private int fileControllerMode;
    private Context context;

    public FileController(int fileControllerMode, Context context) {
        this.fileControllerMode = fileControllerMode;
        this.context = context;
    }

    @Override
    protected List<RssLink> doInBackground(List<RssLink>... lists) {
        if(fileControllerMode == OLVASAS) {
            return fileolvasas(FILENAME);
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
            Log.e("FileController","fileolvasas nem található a fájl",e);
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
