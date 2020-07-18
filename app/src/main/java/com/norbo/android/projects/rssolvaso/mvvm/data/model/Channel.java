package com.norbo.android.projects.rssolvaso.mvvm.data.model;

import java.io.Serializable;
import java.util.Objects;

public class Channel implements Serializable {
    private String title;
    private String description;
    private String link;
    private String language;
    private String pubDate;
    private String url;

    public Channel() {
    }

    public Channel(String title, String description, String link, String language, String pubDate, String url) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.language = language;
        this.pubDate = pubDate;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(title, channel.title) &&
                Objects.equals(description, channel.description) &&
                Objects.equals(link, channel.link) &&
                Objects.equals(language, channel.language) &&
                Objects.equals(pubDate, channel.pubDate) &&
                Objects.equals(url, channel.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, link, language, pubDate, url);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", language='" + language + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
