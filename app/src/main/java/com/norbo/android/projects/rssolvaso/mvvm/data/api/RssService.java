package com.norbo.android.projects.rssolvaso.mvvm.data.api;

import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;

import java.util.List;

public interface RssService {
    List<Article> getArticles(String url);
}
