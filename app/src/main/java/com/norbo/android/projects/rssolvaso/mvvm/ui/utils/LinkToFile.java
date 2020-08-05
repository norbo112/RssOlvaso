package com.norbo.android.projects.rssolvaso.mvvm.ui.utils;

public class LinkToFile {
    private String nev;
    private String link;
    private int favorite;

    public LinkToFile(String nev, String link, int favorite) {
        this.nev = nev;
        this.link = link;
        this.favorite = favorite;
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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
