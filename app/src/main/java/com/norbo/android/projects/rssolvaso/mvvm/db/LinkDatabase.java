package com.norbo.android.projects.rssolvaso.mvvm.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.norbo.android.projects.rssolvaso.mvvm.db.daos.LinkDao;
import com.norbo.android.projects.rssolvaso.mvvm.db.entities.LinkEntity;

@Database(entities = LinkEntity.class, version = 1, exportSchema = false)
public abstract class LinkDatabase extends RoomDatabase {
    public static final String DB_NAME = "LinkDatabase";
    public abstract LinkDao linkDao();
}
