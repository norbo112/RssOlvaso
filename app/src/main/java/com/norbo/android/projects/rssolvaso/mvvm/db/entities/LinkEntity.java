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

    public LinkEntity() {
    }

    @Ignore
    public LinkEntity(String nev, String link) {
        this.nev = nev;
        this.link = link;
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
