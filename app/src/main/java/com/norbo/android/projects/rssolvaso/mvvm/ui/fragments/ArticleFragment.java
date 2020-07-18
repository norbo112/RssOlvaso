package com.norbo.android.projects.rssolvaso.mvvm.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.norbo.android.projects.rssolvaso.databinding.ActivityArticleListInFragmentBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Channel;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.ArticleFragmentRecyclerViewAdapter;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.ArticleRecyclerViewAdapterFactory;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.ArticleViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ArticleFragment extends Fragment {
    private Context context;
    private List<Article> articles;
    private Channel channel;
    private ActivityArticleListInFragmentBinding binding;
    private ArticleViewModel articleViewModel;
    private ViewGroup container;

    private ArticleFragmentRecyclerViewAdapter adapter;

    public ArticleFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.articleViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ArticleViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        binding = ActivityArticleListInFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        initRecyclerView(articles);
    }

    private void initRecyclerView(List<Article> articles) {
        adapter = new ArticleFragmentRecyclerViewAdapter(context, articles);
        binding.articlelist.setLayoutManager(new LinearLayoutManager(context));
        binding.articlelist.setAdapter(adapter);
    }

    public void setChannel(Channel channel) {
        binding.setChannel(channel);
    }

    public void initData(String url) {
        articleViewModel.loadChannelData(url);
        articleViewModel.getArticlesByLink(url);
        articleViewModel.getChannelData().observe(this, channel -> binding.setChannel(channel));
        articleViewModel.getArticles().observe(this, this::initRecyclerView);
        articleViewModel.getLoadingMessage().observe(this, s -> {
            Snackbar.make(container, s, BaseTransientBottomBar.LENGTH_SHORT);
        });
    }
}
