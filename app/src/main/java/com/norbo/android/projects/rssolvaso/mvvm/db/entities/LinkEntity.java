package com.norbo.android.projects.rssolvaso.mvvm.db.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class LinkEntity {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String nev;
    private String link;
    private Integer favorite;

    public LinkEntity() {
    }

    @Ignore
    public LinkEntity(String nev, String link) {
        this.nev = nev;
        this.link = link;
        this.favorite = 0;
    }

    @Ignore
    public LinkEntity(String nev, String link, Integer favorite) {
        this.nev = nev;
        this.link = link;
        this.favorite = favorite;
    }

    @Ignore
    public LinkEntity(Integer id, String nev, String link) {
        this.id = id;
        this.nev = nev;
        this.link = link;
    }

    @Ignore
    public LinkEntity(Integer id, String nev, String link, Integer favorite) {
        this.id = id;
        this.nev = nev;
        this.link = link;
        this.favorite = favorite;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
