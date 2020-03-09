package com.norbo.android.projects.rssolvaso.model;

import android.graphics.Bitmap;

public class RssItem {
    private String title;
    private String link;
    private String guid;
    private String description;
    private String category;
    private String pubDate;
    private Bitmap enclosure;

    public RssItem() {
    }

    public RssItem(String title, String link,
                   String guid, String description,
                   String category, String pubDate) {
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.description = description;
        this.category = category;
        this.pubDate = pubDate;
    }

    public RssItem(String title, String link, String guid, String description, String category, String pubDate, Bitmap enclosure) {
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.description = description;
        this.category = category;
        this.pubDate = pubDate;
        this.enclosure = enclosure;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public Bitmap getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(Bitmap enclosure) {
        this.enclosure = enclosure;
    }
}
