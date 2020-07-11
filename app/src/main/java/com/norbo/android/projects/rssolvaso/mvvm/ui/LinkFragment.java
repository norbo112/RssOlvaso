package com.norbo.android.projects.rssolvaso.mvvm.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.LinkClickedListener;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.MyLinkRecyclerViewAdapter;

import java.io.Serializable;
import java.util.List;

public class LinkFragment extends Fragment {
    private List<Link> linkList;
    private LinkClickedListener linkClickedListener;

    public LinkFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.linkClickedListener = (LinkClickedListener) context;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LinkFragment newInstance(List<Link> links) {
        LinkFragment fragment = new LinkFragment();
        Bundle args = new Bundle();
        args.putSerializable("link_list", (Serializable) links);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            linkList = (List<Link>) getArguments().getSerializable("link_list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyLinkRecyclerViewAdapter(linkClickedListener, linkList));
        return view;
    }
}