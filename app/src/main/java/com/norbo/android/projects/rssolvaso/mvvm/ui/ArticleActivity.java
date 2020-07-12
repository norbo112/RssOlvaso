package com.norbo.android.projects.rssolvaso.mvvm.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.databinding.ActivityArticleListBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.ArticleRecyclerViewAdapter;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.ArticleRecyclerViewAdapterFactory;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.ArticleSavedViewModel;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.ArticleViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ArticleActivity extends AppCompatActivity implements ArticleRecyclerViewAdapter.ArticleView, ArticleRecyclerViewAdapter.ArticleSave {
    private ActivityArticleListBinding binding;
    private ArticleViewModel articleViewModel;
    private ArticleSavedViewModel articleSavedViewModel;

    @Inject
    ArticleRecyclerViewAdapterFactory factory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setuptActionBar(getIntent().getStringExtra("article_title"));

        articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        articleSavedViewModel = new ViewModelProvider(this).get(ArticleSavedViewModel.class);

        articleViewModel.getLoadingMessage().observe(this, message -> {
            if(message != null)
                Snackbar.make(binding.coordinator, message, BaseTransientBottomBar.LENGTH_SHORT).show();
        });
        articleViewModel.getArticlesByLink(getIntent().getStringExtra("article_url"));
        articleViewModel.getArticles().observe(this, articles -> {
            if(articles != null) {
                initRecyclerView(articles);
            }
        });

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_saved_articles) {
            startActivity(new Intent(this, ArticleSavedActivity.class));
        }
        return super.onOptionsItemSelected(item);
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
    public void saveArticle(Article article) {
        articleSavedViewModel.insert(article);
        Toast.makeText(this, article.getTitle()+" hír elmentve!", Toast.LENGTH_SHORT).show();
    }
}