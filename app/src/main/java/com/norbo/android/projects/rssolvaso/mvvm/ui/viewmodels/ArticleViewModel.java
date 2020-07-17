package com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.norbo.android.projects.rssolvaso.mvvm.data.api.RssChannelService;
import com.norbo.android.projects.rssolvaso.mvvm.data.api.RssService;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Channel;
import com.norbo.android.projects.rssolvaso.mvvm.data.services.AdatOlvasasExeption;
import com.norbo.android.projects.rssolvaso.mvvm.data.services.XMLExeption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

@Singleton
public class ArticleViewModel extends ViewModel {
    private SavedStateHandle handle;
    private RssService rssService;
    private RssChannelService rssChannelService;
    private ExecutorService executorService;
    private MutableLiveData<List<Article>> articles;
    private MutableLiveData<String> loadingMessage;
    private MutableLiveData<Channel> channelData;

    @ViewModelInject
    public ArticleViewModel(@Assisted SavedStateHandle savedStateHandle,
                            RssService rssService, RssChannelService rssChannelService,
                            ExecutorService executorService) {
        this.handle = savedStateHandle;
        this.rssService = rssService;
        this.rssChannelService = rssChannelService;
        this.executorService = executorService;
        this.loadingMessage = new MutableLiveData<>(null);
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

    public MutableLiveData<Channel> getChannelData() {
        if(channelData == null)
            channelData = new MutableLiveData<>(new Channel());
        return channelData;
    }

    public void loadChannelData(String url) {
        executorService.execute(() -> {
            try {
                Channel channel = rssChannelService.getChannel(url);
                channelData.postValue(channel);
            } catch (XMLExeption | AdatOlvasasExeption xmlExeption) {
                loadingMessage.postValue("CSatorna adatainak olvasása nem sikerült");
            }
        });
    }

    public void snackBarShoved() {
        loadingMessage.setValue(null);
    }
}
