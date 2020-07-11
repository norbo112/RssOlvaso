package com.norbo.android.projects.rssolvaso.mvvm.di.modules;

import android.content.Context;

import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.ArticleRecyclerViewAdapterFactory;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ActivityContext;

@Module
@InstallIn(ActivityComponent.class)
public class ControllerModule {

    @Provides
    ArticleRecyclerViewAdapterFactory articleRecyclerViewAdapterFactory(@ActivityContext Context context) {
        return new ArticleRecyclerViewAdapterFactory(context);
    }
}
