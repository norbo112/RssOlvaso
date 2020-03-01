package com.norbo.android.projects.rssolvaso.model.sajatlv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.database.model.RssLink;

import java.util.List;

public class SajatListViewAdapter extends ArrayAdapter<RssLink> {
    private List<RssLink> rssLinks;
    private Context context;

    private static class ViewHolder {
        TextView textViewNev;
        TextView textViewLink;
    }

    public SajatListViewAdapter(@NonNull Context context, List<RssLink> links) {
        super(context, R.layout.rss_lv_item, links);
        this.rssLinks = links;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RssLink links = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.rss_lv_item, parent, false);
            viewHolder.textViewNev = convertView.findViewById(R.id.tvCsatNev);
            viewHolder.textViewLink = convertView.findViewById(R.id.tvCsatLink);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewNev.setText(links.getCsatornaNeve());
        viewHolder.textViewLink.setText(links.getCsatornaLink());

        return convertView;
    }
}
