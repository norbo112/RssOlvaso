package com.norbo.android.projects.rssolvaso.database.repositoryd;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.norbo.android.projects.rssolvaso.database.dao.HirDao;
import com.norbo.android.projects.rssolvaso.database.model.HirModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {HirModel.class}, version = 1, exportSchema = false)
public abstract class HirDatabase extends RoomDatabase {
    public abstract HirDao hirDao();

    public static volatile HirDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService hirDbExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static HirDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (HirDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            HirDatabase.class, "hirek_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
