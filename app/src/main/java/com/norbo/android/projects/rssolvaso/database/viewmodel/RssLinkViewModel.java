package com.norbo.android.projects.rssolvaso.database.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.norbo.android.projects.rssolvaso.database.model.RssLink;
import com.norbo.android.projects.rssolvaso.database.repositoryd.RssLinkRepository;

import java.util.List;
import java.util.Map;

public class RssLinkViewModel extends AndroidViewModel {
    private RssLinkRepository repository;
    private LiveData<List<RssLink>> allLinks;

    public RssLinkViewModel(@NonNull Application application) {
        super(application);
        repository = new RssLinkRepository(application);
        allLinks = repository.getAllLinks();
    }

    public LiveData<List<RssLink>> getAllLinks() {
        return allLinks;
    }

    public void insert(RssLink rssLink) {
        repository.insert(rssLink);
    }

    public void delete(int id) { repository.delete(id); }

    public void update(int id, String csatronaneve, String csatornalink) {
        repository.update(id, csatronaneve, csatornalink);
    }
}
