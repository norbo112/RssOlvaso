package com.norbo.android.projects.rssolvaso.mvvm.data.repositories;

import androidx.lifecycle.LiveData;

import com.norbo.android.projects.rssolvaso.mvvm.data.api.LinkRepository;
import com.norbo.android.projects.rssolvaso.mvvm.db.LinkDatabase;
import com.norbo.android.projects.rssolvaso.mvvm.db.daos.LinkDao;
import com.norbo.android.projects.rssolvaso.mvvm.db.entities.LinkEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocalLinkRepository implements LinkRepository {
    private LinkDao linkDao;
    private ExecutorService executorService;

    @Inject
    public LocalLinkRepository(LinkDatabase linkDatabase, ExecutorService executorService) {
        this.linkDao = linkDatabase.linkDao();
        this.executorService = executorService;
    }

    @Override
    public LiveData<List<LinkEntity>> getData() {
        return linkDao.getAll();
    }

    @Override
    public void loadData(List<LinkEntity> links) {
        executorService.execute(() -> {
            if(linkDao.count() == 0) linkDao.insert(links);
        });
    }

    @Override
    public void insert(LinkEntity linkEntity) {
        executorService.execute(() -> linkDao.insert(linkEntity));
    }

    @Override
    public void update(LinkEntity linkEntity) {
        executorService.execute(() -> linkDao.update(linkEntity));
    }
}
