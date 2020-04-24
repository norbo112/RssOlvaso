package com.norbo.android.projects.rssolvaso.database.repositoryd;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.norbo.android.projects.rssolvaso.database.dao.RssLinkDao;
import com.norbo.android.projects.rssolvaso.database.model.RssLink;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {RssLink.class}, version = 2, exportSchema = false)
public abstract class RssLinkDatabase extends RoomDatabase {
    public abstract RssLinkDao rssLinkDao();

    public static volatile RssLinkDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService dbWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RssLinkDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RssLinkDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RssLinkDatabase.class, "rsslink_database")
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            dbWriteExecutor.execute(() -> {

                RssLinkDao dao = INSTANCE.rssLinkDao();

                if(dao.count() > 0) return;

                Map<String, String> urlmap = new HashMap<>();

                urlmap.put("Itthon", "https://www.origo.hu/contentpartner/rss/itthon/origo.xml");
                urlmap.put("Nagyvilág", "https://www.origo.hu/contentpartner/rss/nagyvilag/origo.xml");
                urlmap.put("Gazdaság", "https://www.origo.hu/contentpartner/rss/uzletinegyed/origo.xml");
                urlmap.put("Filmklub", "https://www.origo.hu/contentpartner/rss/filmklub/origo.xml");
                urlmap.put("Sport", "https://www.origo.hu/contentpartner/rss/sport/origo.xml");
                urlmap.put("Tudomány", "https://www.origo.hu/contentpartner/rss/tudomany/origo.xml");
                urlmap.put("Technika", "https://www.origo.hu/contentpartner/rss/techbazis/origo.xml");
                urlmap.put("HVG-Világ", "https://hvg.hu/rss/vilag");
                urlmap.put("HVG-Gazdaság", "https://hvg.hu/rss/gazdasag");
                urlmap.put("HVG-Itthon", "https://hvg.hu/rss/itthon");
                urlmap.put("Index 24 Óra", "https://index.hu/24ora/rss/");
                urlmap.put("NewYork Times: Europe", "https://rss.nytimes.com/services/xml/rss/nyt/Europe.xml");

                for (Map.Entry<String, String> entry : urlmap.entrySet()) {
                    RssLink item = new RssLink(entry.getKey(), entry.getValue());
                    dao.insert(item);
                }
            });
        }
    };
}
