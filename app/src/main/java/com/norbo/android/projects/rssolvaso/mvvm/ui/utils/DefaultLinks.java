package com.norbo.android.projects.rssolvaso.mvvm.ui.utils;

import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;

import java.util.ArrayList;
import java.util.List;

public class DefaultLinks {
    public static List<Link> generateDefaultLinks() {
        List<Link> links = new ArrayList<>();

        links.add(new Link("Origo Itthon", "https://www.origo.hu/contentpartner/rss/itthon/origo.xml"));
        links.add(new Link("Origo Nagyvilág", "https://www.origo.hu/contentpartner/rss/nagyvilag/origo.xml");
        links.add(new Link("Origo Gazdaság", "https://www.origo.hu/contentpartner/rss/uzletinegyed/origo.xml");
        links.add(new Link("Origo Filmklub", "https://www.origo.hu/contentpartner/rss/filmklub/origo.xml"));
        links.add(new Link("Origo Sport", "https://www.origo.hu/contentpartner/rss/sport/origo.xml"));
        links.add(new Link("Origo Tudomány", "https://www.origo.hu/contentpartner/rss/tudomany/origo.xml"));
        links.add(new Link("Origo Technika", "https://www.origo.hu/contentpartner/rss/techbazis/origo.xml"));
        links.add(new Link("HVG-Világ", "https://hvg.hu/rss/vilag"));
        links.add(new Link("HVG-Gazdaság", "https://hvg.hu/rss/gazdasag"));
        links.add(new Link("HVG-Itthon", "https://hvg.hu/rss/itthon"));
        links.add(new Link("Index 24 Óra", "https://index.hu/24ora/rss/"));
        links.add(new Link("NewYork Times: Europe", "https://rss.nytimes.com/services/xml/rss/nyt/Europe.xml"));
        
        return links;
    }
}
