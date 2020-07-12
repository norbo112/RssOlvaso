package com.norbo.android.projects.rssolvaso.mvvm.ui.adapters;

import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;

public interface LinkClickedListener {
    void link(String url, Link link);
}
