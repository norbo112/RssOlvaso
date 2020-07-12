package com.norbo.android.projects.rssolvaso.mvvm.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.norbo.android.projects.rssolvaso.mvvm.db.daos.ArticleDao;
import com.norbo.android.projects.rssolvaso.mvvm.db.daos.LinkDao;
import com.norbo.android.projects.rssolvaso.mvvm.db.entities.ArticleEntity;
import com.norbo.android.projects.rssolvaso.mvvm.db.entities.LinkEntity;

@Database(entities = {LinkEntity.class, ArticleEntity.class}, version = 2, exportSchema = false)
public abstract class LinkDatabase extends RoomDatabase {
    public static final String DB_NAME = "LinkDatabase";
    public abstract LinkDao linkDao();
    public abstract ArticleDao articleDao();

    public static final Migration MIGRATE_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS articleentity (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL,"+
                    "link TEXT NOT NULL," +
                    "guid TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "category TEXT NOT NULL,"+
                    "pubDate TEXT NOT NULL,"+
                    "imageUrl TEXT NOT NULL" +
                    ")");
        }
    };
}
