package com.norbo.android.projects.rssolvaso.mvvm.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.norbo.android.projects.rssolvaso.databinding.ActivityArticleSavedListBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.ArticleRecyclerViewAdapter;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.ArticleSavedRecyclerViewAdapterFactory;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.ArticleSavedViewModel;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.ArticleViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ArticleSavedActivity extends AppCompatActivity implements ArticleRecyclerViewAdapter.ArticleView {
    private ActivityArticleSavedListBinding binding;
    private ArticleSavedViewModel articleSavedViewModel;

    @Inject
    ArticleSavedRecyclerViewAdapterFactory factory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleSavedListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setuptActionBar("Mentett hírek");

        articleSavedViewModel = new ViewModelProvider(this).get(ArticleSavedViewModel.class);

        articleSavedViewModel.getArticleLiveData().observe(this, articles -> {
            if(articles != null) {
                setSavedTitle(articles);
                initRecyclerView(articles);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setSavedTitle(List<Article> articles) {
        if(articles.isEmpty()) binding.articleSavedTitle.setText("Nincsenek mentett hírek");
        else binding.articleSavedTitle.setText(articles.size()+" db hír mentve");
    }

    private void initRecyclerView(List<Article> articles) {
        binding.articlelist.setLayoutManager(new LinearLayoutManager(this));
        binding.articlelist.setAdapter(factory.create(articles));
    }

    private void setuptActionBar(String title) {
        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void viewArticle(Article article) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(article.getLink()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
    }

    @Override
    public void shareArticle(Article article) {
        ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setChooserTitle("Hír megosztása...")
                .setText(article.getLink())
                .startChooser();
    }
}
