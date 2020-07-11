package com.norbo.android.projects.rssolvaso.mvvm.di.modules;

import com.norbo.android.projects.rssolvaso.mvvm.data.api.LinkRepository;
import com.norbo.android.projects.rssolvaso.mvvm.data.api.RssService;
import com.norbo.android.projects.rssolvaso.mvvm.data.repositories.LocalLinkRepository;
import com.norbo.android.projects.rssolvaso.mvvm.data.services.RssServiceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public abstract class ActivityModule {
    @Binds
    public abstract RssService rssService(RssServiceImpl rssService);

    @Binds
    public abstract LinkRepository linkRepository(LocalLinkRepository localLinkRepository);
}
