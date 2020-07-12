package com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.norbo.android.projects.rssolvaso.mvvm.data.api.LinkRepository;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;
import com.norbo.android.projects.rssolvaso.mvvm.db.entities.LinkEntity;

import java.util.List;
import java.util.stream.Collectors;

public class LinkViewModel extends ViewModel {
    private final SavedStateHandle savedStateHandle;
    private final LinkRepository repository;
    private LiveData<List<Link>> linksLiveData;

    @ViewModelInject
    public LinkViewModel(@Assisted SavedStateHandle savedStateHandle, LinkRepository repository) {
        this.savedStateHandle = savedStateHandle;
        this.repository = repository;
        this.linksLiveData = Transformations.map(repository.getData(), entities ->
                entities.stream().map(
                        (linkEntity -> new Link(
                                linkEntity.getId(),
                                linkEntity.getNev(),
                                linkEntity.getLink()))).collect(Collectors.toList()));
    }

    public LiveData<List<Link>> getLinksLiveData() {
        return linksLiveData;
    }

    public void insertLinks(List<Link> links) {
        repository.loadData(links.stream()
                .map(link -> new LinkEntity(link.getNev(), link.getLink()))
                .collect(Collectors.toList()));
    }

    public void insertLink(Link link) {
        repository.insert(new LinkEntity(link.getNev(), link.getLink()));
    }
}
