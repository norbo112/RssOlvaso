package com.norbo.android.projects.rssolvaso.mvvm.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.norbo.android.projects.rssolvaso.databinding.ActivityNewRssReaderBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.LinkClickedListener;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.MyLinkRecyclerViewAdapter;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.DefaultLinks;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.actions.LinkAction;
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

    @Inject
    LinkAction linkAction;

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

        binding.fab.setOnClickListener(v -> linkAction.showDialog(LinkAction.ACTION_ADD, null, (mode, link) -> {
            if(mode == LinkAction.ACTION_ADD) {
                linkViewModel.insertLink(link);
            }
        }));
    }

    private void initRecyclerView(List<Link> links) {
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(new MyLinkRecyclerViewAdapter(this, links));
    }

    @Override
    public void link(String url, Link link) {
        if(link == null) {
            Intent intent = new Intent(this, ArticleActivity.class);
            intent.putExtra("article_url", url);
            startActivity(intent);
        } else {
            linkAction.showDialog(LinkAction.ACTION_EDIT, link, (mode, link1) -> {
                if (mode == LinkAction.ACTION_EDIT) {
                    linkViewModel.update(link1);
                }
            });
        }
    }
}