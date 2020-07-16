package com.norbo.android.projects.rssolvaso.mvvm.data.model;

import java.io.Serializable;
import java.util.Objects;

public class Channel implements Serializable {
    private String title;
    private String description;
    private String link;
    private String language;

    public Channel() {
    }

    public Channel(String title, String description, String link, String language) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.language = language;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(title, channel.title) &&
                Objects.equals(description, channel.description) &&
                Objects.equals(link, channel.link) &&
                Objects.equals(language, channel.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, link, language);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
