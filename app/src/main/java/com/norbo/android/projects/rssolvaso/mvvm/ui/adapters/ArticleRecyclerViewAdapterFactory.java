package com.norbo.android.projects.rssolvaso.mvvm.ui.adapters;

import android.content.Context;

import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class ArticleRecyclerViewAdapterFactory {
    private Context context;

    public ArticleRecyclerViewAdapterFactory(Context context) {
        this.context = context;
    }

    public ArticleRecyclerViewAdapter create(List<Article> articles) {
        return new ArticleRecyclerViewAdapter(context, articles);
    }
}
