package com.norbo.android.projects.rssolvaso.mvvm.di.modules;

import android.app.Application;

import androidx.room.Room;

import com.norbo.android.projects.rssolvaso.mvvm.data.api.ArticleRepository;
import com.norbo.android.projects.rssolvaso.mvvm.data.api.LinkRepository;
import com.norbo.android.projects.rssolvaso.mvvm.data.repositories.LocalArticleRepository;
import com.norbo.android.projects.rssolvaso.mvvm.data.repositories.LocalLinkRepository;
import com.norbo.android.projects.rssolvaso.mvvm.db.LinkDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class DatabaseModule {
    @Provides
    @Singleton
    LinkDatabase linkDatabase(Application application) {
        return Room.databaseBuilder(application, LinkDatabase.class, LinkDatabase.DB_NAME)
                .addMigrations(LinkDatabase.MIGRATE_1_2).build();
    }

    @Provides
    @Singleton
    ExecutorService executor() {
        return Executors.newFixedThreadPool(5);
    }

    @Provides
    @Singleton
    LinkRepository linkRepository(LinkDatabase linkDatabase, ExecutorService executorService) {
        return new LocalLinkRepository(linkDatabase, executorService);
    }

    @Provides
    @Singleton
    ArticleRepository articleRepository(LinkDatabase database, ExecutorService executorService) {
        return new LocalArticleRepository(database, executorService);
    }
}
