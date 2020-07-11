package com.norbo.android.projects.rssolvaso.mvvm.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.databinding.NewRssReaderLinkListItemBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;

import java.util.List;

public class LinkListAdapter extends BaseAdapter {
    private List<Link> links;

    public LinkListAdapter(List<Link> links) {
        this.links = links;
    }

    @Override
    public int getCount() {
        return links.size();
    }

    @Override
    public Object getItem(int position) {
        return links.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NewRssReaderLinkListItemBinding binding;
        if(convertView == null) {
            convertView = inflater
                    .inflate(R.layout.new_rss_reader_link_list_item, parent, false);
            binding = DataBindingUtil.bind(convertView);
            convertView.setTag(binding);
        } else {
            binding = (NewRssReaderLinkListItemBinding) convertView.getTag();
        }

        binding.setLink(links.get(position));

        return binding.getRoot();
    }
}
