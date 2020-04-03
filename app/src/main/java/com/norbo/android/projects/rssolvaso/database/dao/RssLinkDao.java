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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(RssLink rssLink);

    @Query("DELETE FROM rsslink")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM rsslink")
    int count();

    @Query("SELECT * FROM rsslink ORDER BY csatronanev")
    LiveData<List<RssLink>> getAllLinks();

    @Query("DELETE FROM rsslink WHERE id= :id")
    void delete(int id);

    @Query("UPDATE rsslink SET csatronanev=:csatornanev, csatornalink=:csatornalink WHERE id=:id")
    void update(int id, String csatornanev, String csatornalink);
}
