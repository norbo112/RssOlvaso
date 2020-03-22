package com.norbo.android.projects.rssolvaso.model.sajatlv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.database.model.RssLink;

import java.util.ArrayList;
import java.util.List;

public class SajatListViewAdapter extends ArrayAdapter<RssLink> implements Filterable {
    private List<RssLink> rssLinks;
    private List<RssLink> rssLinksFilter;
    private Context context;
    private CimFilter cimFilter;

    private static class ViewHolder {
        TextView textViewNev;
        TextView textViewLink;
    }

    public SajatListViewAdapter(@NonNull Context context, List<RssLink> links) {
        super(context, R.layout.rss_lv_item, links);
        this.rssLinks = links;
        this.context = context;
        this.rssLinksFilter = links;
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

    @Override
    public int getCount() {
        return rssLinks.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public RssLink getItem(int position) {
        return rssLinks.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(cimFilter == null)
            cimFilter = new CimFilter();
        return cimFilter;
    }

    private class CimFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length() > 0) {
                List<RssLink> filterList = new ArrayList<>();
                for(RssLink rssItem: rssLinksFilter) {
                    if(rssItem.getCsatornaNeve().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(rssItem);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = rssLinksFilter.size();
                results.values = rssLinksFilter;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                rssLinks = (List<RssLink>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
