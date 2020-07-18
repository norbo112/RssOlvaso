package com.norbo.android.projects.rssolvaso.mvvm.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.norbo.android.projects.rssolvaso.mvvm.db.entities.ArticleEntity;

import java.util.List;

@Dao
public interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ArticleEntity articleEntity);

    @Query("SELECT * FROM articleentity")
    LiveData<List<ArticleEntity>> selectAll();

    @Delete
    void delete(ArticleEntity articleEntity);
}
