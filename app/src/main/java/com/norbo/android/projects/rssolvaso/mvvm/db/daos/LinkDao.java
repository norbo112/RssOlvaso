package com.norbo.android.projects.rssolvaso.mvvm.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.norbo.android.projects.rssolvaso.mvvm.db.entities.LinkEntity;

import java.util.List;

@Dao
public interface LinkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LinkEntity linkEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<LinkEntity> linkEntities);

    @Query("SELECT * FROM linkentity ORDER BY nev ASC")
    LiveData<List<LinkEntity>> getAll();

    @Query("DELETE FROM linkentity")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM linkentity")
    int count();

    @Update
    int update(LinkEntity linkEntity);

    @Delete
    void delete(LinkEntity linkEntity);
}
