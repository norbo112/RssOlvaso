package com.norbo.android.projects.rssolvaso.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.norbo.android.projects.rssolvaso.database.model.HirModel;

import java.util.List;

@Dao
public interface HirDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HirModel hirModel);

    @Query("DELETE FROM hirmodel")
    void deleteAll();

    @Query("SELECT * FROM hirmodel ORDER BY pubdate")
    LiveData<List<HirModel>> getAllData();

    @Query("SELECT hircim FROM hirmodel WHERE hircim LIKE :title LIMIT 1")
    LiveData<HirModel> getHir(String title);
}
