package com.norbo.android.projects.rssolvaso.mvvm.data.api;

import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Channel;
import com.norbo.android.projects.rssolvaso.mvvm.data.services.AdatOlvasasExeption;
import com.norbo.android.projects.rssolvaso.mvvm.data.services.XMLExeption;

import java.util.List;
import java.util.Map;

public interface RssServiceWithChannels {
    Map<Channel, List<Article>> getChannelAndArticles(String url) throws XMLExeption, AdatOlvasasExeption;
}
