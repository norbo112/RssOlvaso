package com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.norbo.android.projects.rssolvaso.mvvm.data.api.ArticleRepository;
import com.norbo.android.projects.rssolvaso.mvvm.data.api.RssService;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.norbo.android.projects.rssolvaso.mvvm.data.repositories.LocalArticleRepository;
import com.norbo.android.projects.rssolvaso.mvvm.db.entities.ArticleEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import javax.inject.Singleton;

@Singleton
public class ArticleSavedViewModel extends ViewModel {
    private SavedStateHandle handle;
    private ArticleRepository repository;
    private LiveData<List<Article>> articleLiveData;

    @ViewModelInject
    public ArticleSavedViewModel(@Assisted SavedStateHandle savedStateHandle,
                                 ArticleRepository repository) {
        this.handle = savedStateHandle;
        this.repository = repository;
        this.articleLiveData = Transformations.map(repository.getData(), entities ->
                entities.stream().map(articleEntity -> new Article(articleEntity.getId(),
                articleEntity.getTitle(),
                articleEntity.getLink(),
                articleEntity.getGuid(),
                articleEntity.getDescription(),
                articleEntity.getCategory(),
                articleEntity.getPubDate(),
                articleEntity.getImageUrl())).collect(Collectors.toList()));
    }

    public LiveData<List<Article>> getArticleLiveData() {
        return articleLiveData;
    }

    public void insert(Article article) {
        repository.insert(new ArticleEntity(
                        article.getTitle(), article.getLink(),
                        article.getGuid(), article.getDescription(),
                        article.getCategory(), article.getPubDate(),
                        article.getImageUrl()));
    }

    public void delete(Article article) {
        repository.delete(article.getTitle());
    }
}
