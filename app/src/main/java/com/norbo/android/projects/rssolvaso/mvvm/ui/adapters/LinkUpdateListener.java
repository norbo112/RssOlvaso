package com.norbo.android.projects.rssolvaso.mvvm.ui.adapters;

import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;

public interface LinkUpdateListener {
    void linkUpdate(Link link);
    void deleteLink(Link link);
}
