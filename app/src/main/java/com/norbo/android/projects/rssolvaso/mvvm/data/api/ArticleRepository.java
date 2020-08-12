package com.norbo.android.projects.rssolvaso.mvvm.data.api;

import androidx.lifecycle.LiveData;

import com.norbo.android.projects.rssolvaso.mvvm.db.entities.ArticleEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ArticleRepository {
    LiveData<List<ArticleEntity>> getData();
    void insert(ArticleEntity articleEntity);
    void delete(String title);
    CompletableFuture<ArticleEntity> getOne(String title);
}
