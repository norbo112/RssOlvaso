package com.norbo.android.projects.rssolvaso.database.repositoryd;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.norbo.android.projects.rssolvaso.database.dao.HirDao;
import com.norbo.android.projects.rssolvaso.database.model.HirModel;

import java.util.List;
import java.util.Optional;

public class HirRepository {
    private HirDao hirDao;
    private LiveData<List<HirModel>> hirek;

    public HirRepository(Application application) {
        HirDatabase db = HirDatabase.getDatabase(application);
        hirDao = db.hirDao();
        hirek = hirDao.getAllData();
    }

    public LiveData<List<HirModel>> getHirek() {
        return hirek;
    }

    public void insert(HirModel hirModel) {
        HirDatabase.hirDbExecutor.execute(() -> {
            hirDao.insert(hirModel);
        });
    }

    public void deleteAll() {
        HirDatabase.hirDbExecutor.execute(() -> {
            hirDao.deleteAll();
        });
    }
}
