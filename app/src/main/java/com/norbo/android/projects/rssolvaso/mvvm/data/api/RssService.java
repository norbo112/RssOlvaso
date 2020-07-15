package com.norbo.android.projects.rssolvaso.mvvm.data.api;

import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.norbo.android.projects.rssolvaso.mvvm.data.services.AdatOlvasasExeption;
import com.norbo.android.projects.rssolvaso.mvvm.data.services.XMLExeption;

import java.util.List;

public interface RssService {
    List<Article> getArticles(String url) throws XMLExeption, AdatOlvasasExeption;
}
