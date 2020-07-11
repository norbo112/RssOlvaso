package com.norbo.android.projects.rssolvaso.mvvm.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.databinding.ActivityNewRssReaderBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.LinkClickedListener;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.LinkListAdapter;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.DefaultLinks;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.ArticleViewModel;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.LinkViewModel;

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
        articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);

        linkViewModel.insertLinks(DefaultLinks.generateDefaultLinks());

        linkViewModel.getLinksLiveData().observe(this, links -> {
            if(links != null) {
                getSupportFragmentManager().beginTransaction().replace(
                        binding.container.getId(),
                        LinkFragment.newInstance(links))
                .addToBackStack(null)
                .commit();
            }
        });
    }

    @Override
    public void link(String url) {
        articleViewModel.getArticlesByLink(url);
        articleViewModel.getArticles().observe(this, articles -> {
            if(articles != null) {
                getSupportFragmentManager().beginTransaction().replace(
                        binding.container.getId(),
                        ArticleFragment.newInstance(articles))
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}