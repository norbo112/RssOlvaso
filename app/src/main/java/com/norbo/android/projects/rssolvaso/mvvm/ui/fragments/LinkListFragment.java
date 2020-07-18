package com.norbo.android.projects.rssolvaso.mvvm.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.norbo.android.projects.rssolvaso.databinding.LinklistForFragmentBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.MyLinkRecyclerViewAdapter;

import java.util.List;

public class LinkListFragment extends Fragment {
    private Context context;
    private List<Link> linkList;
    private LinklistForFragmentBinding binding;

    public LinkListFragment(Context context, List<Link> linkList) {
        this.context = context;
        this.linkList = linkList;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LinklistForFragmentBinding.inflate(inflater, container, false);
        initRecyclerView(linkList);
        return binding.getRoot();
    }

    public void setLinkList(List<Link> linkList) {
        this.linkList = linkList;
        initRecyclerView(linkList);
    }

    private void initRecyclerView(List<Link> linkList) {
        binding.list.setLayoutManager(new LinearLayoutManager(context));
        binding.list.setAdapter(new MyLinkRecyclerViewAdapter(context, linkList));
    }
}
