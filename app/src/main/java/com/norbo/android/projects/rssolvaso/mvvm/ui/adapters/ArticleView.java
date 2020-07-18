package com.norbo.android.projects.rssolvaso.mvvm.ui.adapters;

import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;

public interface ArticleView {
    void viewArticle(Article article);
    void shareArticle(Article article);
}
