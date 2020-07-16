package com.norbo.android.projects.rssolvaso.mvvm.data.api;

import com.norbo.android.projects.rssolvaso.mvvm.data.model.Channel;
import com.norbo.android.projects.rssolvaso.mvvm.data.services.AdatOlvasasExeption;
import com.norbo.android.projects.rssolvaso.mvvm.data.services.XMLExeption;

public interface RssChannelService {
    Channel geteChannelInfo(String url) throws XMLExeption, AdatOlvasasExeption;
}
