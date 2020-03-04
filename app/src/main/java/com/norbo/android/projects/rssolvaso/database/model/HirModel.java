package com.norbo.android.projects.rssolvaso.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HirModel {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String pubdate;
    @ColumnInfo
    private String link;
    @ColumnInfo
    private String hircim;
    @ColumnInfo
    private String hirdesc;

    public HirModel() {
    }

    public HirModel(String pubdate, String link, String hircim, String hirdesc) {
        this.pubdate = pubdate;
        this.link = link;
        this.hircim = hircim;
        this.hirdesc = hirdesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getHircim() {
        return hircim;
    }

    public void setHircim(String hircim) {
        this.hircim = hircim;
    }

    public String getHirdesc() {
        return hirdesc;
    }

    public void setHirdesc(String hirdesc) {
        this.hirdesc = hirdesc;
    }
}
