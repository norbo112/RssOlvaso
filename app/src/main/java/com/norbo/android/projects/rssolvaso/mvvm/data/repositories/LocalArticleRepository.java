package com.norbo.android.projects.rssolvaso.mvvm.data.repositories;

import androidx.lifecycle.LiveData;

import com.norbo.android.projects.rssolvaso.mvvm.data.api.ArticleRepository;
import com.norbo.android.projects.rssolvaso.mvvm.db.LinkDatabase;
import com.norbo.android.projects.rssolvaso.mvvm.db.daos.ArticleDao;
import com.norbo.android.projects.rssolvaso.mvvm.db.entities.ArticleEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocalArticleRepository implements ArticleRepository {
    private ArticleDao articleDao;
    private ExecutorService executorService;

    @Inject
    public LocalArticleRepository(LinkDatabase linkDatabase, ExecutorService executorService) {
        this.articleDao = linkDatabase.articleDao();
        this.executorService = executorService;
    }

    @Override
    public LiveData<List<ArticleEntity>> getData() {
        return articleDao.selectAll();
    }


    @Override
    public void insert(ArticleEntity articleEntity) {
        executorService.execute(() -> articleDao.insert(articleEntity));
    }

    @Override
    public void delete(String title) {
        executorService.execute(() -> articleDao.delete(title));
    }

    @Override
    public CompletableFuture<ArticleEntity> getOne(String title) {
        return CompletableFuture.supplyAsync(() -> articleDao.getOne(title), executorService);
    }
}
