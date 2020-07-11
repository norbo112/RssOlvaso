package com.norbo.android.projects.rssolvaso.mvvm.data.model;

import java.io.Serializable;
import java.util.Objects;

public class Article implements Serializable {
    private String title;
    private String link;
    private String guid;
    private String description;
    private String category;
    private String pubDate;
    private String imageUrl;

    public Article() {
    }

    public Article(String title, String link, String guid, String description, String category, String pubDate, String imageUrl) {
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.description = description;
        this.category = category;
        this.pubDate = pubDate;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(title, article.title) &&
                Objects.equals(link, article.link) &&
                Objects.equals(guid, article.guid) &&
                Objects.equals(description, article.description) &&
                Objects.equals(category, article.category) &&
                Objects.equals(pubDate, article.pubDate) &&
                Objects.equals(imageUrl, article.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, link, guid, description, category, pubDate, imageUrl);
    }
}
