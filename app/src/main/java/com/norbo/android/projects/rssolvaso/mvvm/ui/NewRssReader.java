package com.norbo.android.projects.rssolvaso.mvvm.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.databinding.ActivityNewRssReaderBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.LinkClickedListener;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.LinkListAdapter;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.MyLinkRecyclerViewAdapter;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.DefaultLinks;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.ArticleViewModel;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.LinkViewModel;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewRssReader extends AppCompatActivity implements LinkClickedListener {
    private LinkViewModel linkViewModel;
    private ArticleViewModel articleViewModel;

    ActivityNewRssReaderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewRssReaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        linkViewModel = new ViewModelProvider(this).get(LinkViewModel.class);
        linkViewModel.insertLinks(DefaultLinks.generateDefaultLinks());
        linkViewModel.getLinksLiveData().observe(this, links -> {
            if(links != null) {
                initRecyclerView(links);
            }
        });
    }

    private void initRecyclerView(List<Link> links) {
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(new MyLinkRecyclerViewAdapter(this, links));
    }

    @Override
    public void link(String url) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("article_url", url);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 0)
            super.onBackPressed();
        else
            getSupportFragmentManager().popBackStack();
    }
}