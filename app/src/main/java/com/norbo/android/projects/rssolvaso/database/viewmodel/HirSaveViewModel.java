package com.norbo.android.projects.rssolvaso.database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.norbo.android.projects.rssolvaso.database.model.HirModel;
import com.norbo.android.projects.rssolvaso.database.repositoryd.HirRepository;
import com.norbo.android.projects.rssolvaso.model.RssItem;

import java.util.List;

public class HirSaveViewModel extends AndroidViewModel {
    private HirRepository hirRepository;
    private LiveData<List<HirModel>> hirek;

    public HirSaveViewModel(@NonNull Application application) {
        super(application);
        hirRepository = new HirRepository(application);
        hirek = hirRepository.getHirek();
    }

    public LiveData<List<HirModel>> getHirek() {
        return hirek;
    }

    public void insert(HirModel hirModel) {
        hirRepository.insert(hirModel);
    }

    public void deleteAll() {
        hirRepository.deleteAll();
    }

    public LiveData<HirModel> getHirByTitle(String title) { return hirRepository.getHirByTitle(title); }

    public void delete(String title) { hirRepository.delete(title); }
}
