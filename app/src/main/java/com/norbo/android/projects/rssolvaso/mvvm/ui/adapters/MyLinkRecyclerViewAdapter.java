package com.norbo.android.projects.rssolvaso.mvvm.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.databinding.NewRssReaderLinkListItemBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;

import java.util.List;

public class MyLinkRecyclerViewAdapter extends RecyclerView.Adapter<MyLinkRecyclerViewAdapter.ViewHolder> {
    private final List<Link> mValues;
    private LinkClickedListener linkClickedListener;
    private LinkUpdateListener linkUpdateListener;
    private Context context;

    public MyLinkRecyclerViewAdapter(Context context, List<Link> items) {
        mValues = items;
        linkClickedListener = (LinkClickedListener) context;
        linkUpdateListener = (LinkUpdateListener) context;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewRssReaderLinkListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.new_rss_reader_link_list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position), holder.itemView);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final NewRssReaderLinkListItemBinding binding;

        public ViewHolder(NewRssReaderLinkListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Link currentLink, View currentView) {
            binding.setLink(currentLink);
            binding.setAction(new Action());
            binding.alllinear.setOnLongClickListener(v -> {
                new Action().linkLongClicked(currentLink, currentView);
                return true;
            });
            binding.executePendingBindings();
        }
    }

    public class Action {
        public void linkClicked(Link link) {
            linkClickedListener.link(link.getLink(), link.getNev());
        }

        @SuppressLint("RestrictedApi")
        public void linkLongClicked(Link link, View currentView) {
            PopupMenu popupMenu = new PopupMenu(context, currentView);
            popupMenu.inflate(R.menu.linksetting);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.link_menu_edit) {
                    linkUpdateListener.linkUpdate(link);
                } else if(item.getItemId() == R.id.link_menu_delete) {
                    linkUpdateListener.deleteLink(link);
                }
                return true;
            });

            MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) popupMenu.getMenu(), currentView);
            menuHelper.setForceShowIcon(true);
            menuHelper.show();
        }
    }
}