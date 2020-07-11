package com.norbo.android.projects.rssolvaso.mvvm.data.model;

import java.io.Serializable;
import java.util.Objects;

public class Link implements Serializable {
    private Integer id;
    private String nev;
    private String link;

    public Link() {
    }

    public Link(String nev, String link) {
        this.nev = nev;
        this.link = link;
    }

    public Link(Integer id, String nev, String link) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link1 = (Link) o;
        return Objects.equals(id, link1.id) &&
                Objects.equals(nev, link1.nev) &&
                Objects.equals(link, link1.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nev, link);
    }

    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", nev='" + nev + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
