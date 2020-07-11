package com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.norbo.android.projects.rssolvaso.mvvm.data.api.RssService;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

@Singleton
public class ArticleViewModel extends ViewModel {
    private SavedStateHandle handle;
    private RssService rssService;
    private ExecutorService executorService;
    private MutableLiveData<List<Article>> articles;

    @ViewModelInject
    public ArticleViewModel(@Assisted SavedStateHandle savedStateHandle,
                            RssService rssService,
                            ExecutorService executorService) {
        this.handle = savedStateHandle;
        this.rssService = rssService;
        this.executorService = executorService;
    }

    public MutableLiveData<List<Article>> getArticles() {
        if(articles == null)
            articles = new MutableLiveData<>(new ArrayList<>());
        return articles;
    }

    public void getArticlesByLink(String link) {
        executorService.execute(() -> {
            List<Article> list = rssService.getArticles(link);
            articles.postValue(list);
        });
    }
}
