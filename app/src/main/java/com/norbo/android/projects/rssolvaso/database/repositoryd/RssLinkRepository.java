package com.norbo.android.projects.rssolvaso.database.repositoryd;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.norbo.android.projects.rssolvaso.database.dao.RssLinkDao;
import com.norbo.android.projects.rssolvaso.database.model.RssLink;

import java.util.List;

public class RssLinkRepository {
    private RssLinkDao rssLinkDao;
    private LiveData<List<RssLink>> allLinks;

    public RssLinkRepository(Application application) {
        RssLinkDatabase db = RssLinkDatabase.getDatabase(application);
        rssLinkDao = db.rssLinkDao();
        allLinks = rssLinkDao.getAllLinks();
    }

    public LiveData<List<RssLink>> getAllLinks() {
        return allLinks;
    }

    public void insert(RssLink link) {
        RssLinkDatabase.dbWriteExecutor.execute(() -> {
            rssLinkDao.insert(link);
        });
    }

    public void delete(int id) {
        RssLinkDatabase.dbWriteExecutor.execute(() -> {
            rssLinkDao.delete(id);
        });
    }

    public void update(int id, String csatornanev, String csatornalink) {
        RssLinkDatabase.dbWriteExecutor.execute(() -> {
            rssLinkDao.update(id, csatornanev, csatornalink);
        });
    }
}
