package com.norbo.android.projects.rssolvaso.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.norbo.android.projects.rssolvaso.database.model.RssLink;

import java.util.List;

@Dao
public interface RssLinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RssLink rssLink);

    @Query("DELETE FROM rsslink")
    void deleteAll();

    @Query("SELECT * FROM rsslink ORDER BY csatronanev")
    LiveData<List<RssLink>> getAllLinks();

    @Query("DELETE FROM rsslink WHERE csatronanev = :csatornanev")
    void delete(String csatornanev);
}
