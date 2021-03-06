package com.norbo.android.projects.rssolvaso.mvvm.data.api;

import androidx.lifecycle.LiveData;

import com.norbo.android.projects.rssolvaso.mvvm.db.entities.LinkEntity;

import java.util.List;

public interface LinkRepository {
    LiveData<List<LinkEntity>> getData();
    void loadData(List<LinkEntity> links);
    void insert(LinkEntity linkEntity);
    void insert(List<LinkEntity> entities);
    void update(LinkEntity linkEntity);

    void delete(LinkEntity linkEntity);
    void deleteAll();

    void setFavorite(LinkEntity entity);
}
