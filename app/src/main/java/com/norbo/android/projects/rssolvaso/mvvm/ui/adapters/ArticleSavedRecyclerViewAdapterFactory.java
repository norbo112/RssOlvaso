package com.norbo.android.projects.rssolvaso.mvvm.ui.adapters;

import android.content.Context;

import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;

import java.util.List;

public class ArticleSavedRecyclerViewAdapterFactory {
    private Context context;

    public ArticleSavedRecyclerViewAdapterFactory(Context context) {
        this.context = context;
    }

    public ArticleSavedRecyclerViewAdapter create(List<Article> articles) {
        return new ArticleSavedRecyclerViewAdapter(context, articles);
    }
}
