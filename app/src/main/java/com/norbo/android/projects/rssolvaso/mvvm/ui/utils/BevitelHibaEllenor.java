package com.norbo.android.projects.rssolvaso.mvvm.ui.utils;

import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;

@ActivityContext
public class BevitelHibaEllenor {
    @Inject
    public BevitelHibaEllenor() {
    }

    /**
     * Egyszerübb adat tag ellenőrzés, még fejlesztés alatt :)
     * @param link
     * @return
     */
    public boolean linkIsPassed(Link link) {
        return (link.getLink() != null && link.getNev() != null) &&
                (link.getLink().length() != 0 && link.getNev().length() != 0);
    }
}
