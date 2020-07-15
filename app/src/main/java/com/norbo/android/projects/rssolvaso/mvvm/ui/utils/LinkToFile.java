package com.norbo.android.projects.rssolvaso.mvvm.ui.utils;

public class LinkToFile {
    private String nev;
    private String link;

    public LinkToFile(String nev, String link) {
        this.nev = nev;
        this.link = link;
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
