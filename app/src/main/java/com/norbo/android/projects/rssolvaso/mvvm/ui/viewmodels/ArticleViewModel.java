package com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels;

import android.util.Log;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.norbo.android.projects.rssolvaso.mvvm.data.api.RssService;
import com.norbo.android.projects.rssolvaso.mvvm.data.api.RssServiceWithChannels;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Channel;
import com.norbo.android.projects.rssolvaso.mvvm.data.services.AdatOlvasasExeption;
import com.norbo.android.projects.rssolvaso.mvvm.data.services.XMLExeption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

@Singleton
public class ArticleViewModel extends ViewModel {
    private SavedStateHandle handle;
    private RssService rssService;
    private RssServiceWithChannels rssServiceWithChannels;
    private ExecutorService executorService;
    private MutableLiveData<List<Article>> articles;
    private MutableLiveData<String> loadingMessage;

    //csak teszt
    private MutableLiveData<Map<Channel, List<Article>>> mapMutableLiveData;

    @ViewModelInject
    public ArticleViewModel(@Assisted SavedStateHandle savedStateHandle,
                            RssService rssService, RssServiceWithChannels rssServiceWithChannels,
                            ExecutorService executorService) {
        this.handle = savedStateHandle;
        this.rssService = rssService;
        this.rssServiceWithChannels = rssServiceWithChannels;
        this.executorService = executorService;
        this.loadingMessage = new MutableLiveData<>(null);
        this.mapMutableLiveData = new MutableLiveData<>(new HashMap<>());
    }

    public MutableLiveData<List<Article>> getArticles() {
        if(articles == null)
            articles = new MutableLiveData<>(new ArrayList<>());
        return articles;
    }

    public MutableLiveData<String> getLoadingMessage() {
        return loadingMessage;
    }

    public void getArticlesByLink(String link) {
        loadingMessage.postValue("Cikkek betöltése");
        executorService.execute(() -> {
            List<Article> list = null;
            try {
                list = rssService.getArticles(link);
            } catch (XMLExeption xmlExeption) {
                loadingMessage.postValue(xmlExeption.getMessage());
            } catch (AdatOlvasasExeption adatOlvasasExeption) {
                loadingMessage.postValue("Adatolvasás: "+adatOlvasasExeption.getMessage());
            }

            articles.postValue(list);
        });
    }

    public void snackBarShoved() {
        loadingMessage.setValue(null);
    }

    public MutableLiveData<Map<Channel, List<Article>>> getMapMutableLiveData() {
        return mapMutableLiveData;
    }

    public void setMapMutableLiveData(String url) {
        loadingMessage.postValue("Map feldolgozása");
        executorService.execute(() -> {
            Map<Channel, List<Article>> map = null;
            try {
                map = rssServiceWithChannels.getChannelAndArticles(url);
            } catch (XMLExeption | AdatOlvasasExeption xmlExeption) {
                loadingMessage.postValue(xmlExeption.getMessage());
            }

            mapMutableLiveData.postValue(map);
        });
    }
}
