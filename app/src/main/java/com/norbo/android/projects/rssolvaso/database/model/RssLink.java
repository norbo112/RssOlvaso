package com.norbo.android.projects.rssolvaso.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class RssLink implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "csatronanev")
    private String csatornaNeve;

    @NonNull
    @ColumnInfo(name = "csatornalink")
    private String csatornaLink;

    public RssLink() {
    }

    @Ignore
    public RssLink(@NonNull String csatornaNeve, @NonNull String csatornaLink) {
        this.csatornaNeve = csatornaNeve;
        this.csatornaLink = csatornaLink;
    }

    @NonNull
    public String getCsatornaNeve() {
        return csatornaNeve;
    }

    public void setCsatornaNeve(@NonNull String csatornaNeve) {
        this.csatornaNeve = csatornaNeve;
    }

    @NonNull
    public String getCsatornaLink() {
        return csatornaLink;
    }

    public void setCsatornaLink(@NonNull String csatornaLink) {
        this.csatornaLink = csatornaLink;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    @NonNull
    @Override
    public String toString() {
        return csatornaNeve;
    }
}
