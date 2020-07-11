package com.norbo.android.projects.rssolvaso.mvvm.ui.utils;

import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultLinks {
    public static List<Link> generateDefaultLinks() {
        List<Link> links = new ArrayList<>();
        Map<String, String> urlmap = new HashMap<>();

        urlmap.put("Origo Itthon", "https://www.origo.hu/contentpartner/rss/itthon/origo.xml");
        urlmap.put("Origo Nagyvilág", "https://www.origo.hu/contentpartner/rss/nagyvilag/origo.xml");
        urlmap.put("Origo Gazdaság", "https://www.origo.hu/contentpartner/rss/uzletinegyed/origo.xml");
        urlmap.put("Origo Filmklub", "https://www.origo.hu/contentpartner/rss/filmklub/origo.xml");
        urlmap.put("Origo Sport", "https://www.origo.hu/contentpartner/rss/sport/origo.xml");
        urlmap.put("Origo Tudomány", "https://www.origo.hu/contentpartner/rss/tudomany/origo.xml");
        urlmap.put("Origo Technika", "https://www.origo.hu/contentpartner/rss/techbazis/origo.xml");
        urlmap.put("HVG-Világ", "https://hvg.hu/rss/vilag");
        urlmap.put("HVG-Gazdaság", "https://hvg.hu/rss/gazdasag");
        urlmap.put("HVG-Itthon", "https://hvg.hu/rss/itthon");
        urlmap.put("Index 24 Óra", "https://index.hu/24ora/rss/");
        urlmap.put("NewYork Times: Europe", "https://rss.nytimes.com/services/xml/rss/nyt/Europe.xml");

        for (Map.Entry<String, String> entry : urlmap.entrySet()) {
            links.add(new Link(entry.getKey(), entry.getValue()));
        }
        return links;
    }
}
