package com.norbo.android.projects.rssolvaso.mvvm.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.databinding.ActivityNewRssReaderBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.ArticleFragmentRecyclerViewAdapter;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.LinkClickedListener;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.LinkUpdateListener;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.MyLinkRecyclerViewAdapter;
import com.norbo.android.projects.rssolvaso.mvvm.ui.fragments.ArticleFragment;
import com.norbo.android.projects.rssolvaso.mvvm.ui.fragments.LinkListFragment;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.BevitelHibaEllenor;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.DefaultLinks;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.LinksFileController;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.LinksFileSaveController;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.actions.LinkAction;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.ArticleSavedViewModel;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.LinkViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewRssReader extends AppCompatActivity implements LinkClickedListener, LinkUpdateListener,
        ArticleFragmentRecyclerViewAdapter.ArticleView, ArticleFragmentRecyclerViewAdapter.ArticleSave {
    private static final String TAG = "NewRssReader";
    private LinkViewModel linkViewModel;
    private ArticleSavedViewModel articleSavedViewModel;

    ActivityNewRssReaderBinding binding;

    @Inject
    LinkAction linkAction;

    @Inject
    LinksFileController fileController;

    @Inject
    LinksFileSaveController fileSaveController;

    @Inject
    BevitelHibaEllenor ellenor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewRssReaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setuptActionBar("Hír olvasó");

        articleSavedViewModel = new ViewModelProvider(this).get(ArticleSavedViewModel.class);
        linkViewModel = new ViewModelProvider(this).get(LinkViewModel.class);
        linkViewModel.insertLinks(DefaultLinks.generateDefaultLinks());
        linkViewModel.getLinksLiveData().observe(this, links -> {
            if(links != null) {
                initRecyclerView(links);
            }
        });

        if(!isTablet()) {
            binding.fab.setOnClickListener(v -> linkAction.showDialog(LinkAction.ACTION_ADD, null, (mode, link) -> {
                if (mode == LinkAction.ACTION_ADD) {
                    if (ellenor.linkIsPassed(link))
                        linkViewModel.insertLink(link);
                    else
                        Toast.makeText(this, "Kérlek töltsd ki a mezőket, különben nem tudom hozzáadni a linket", Toast.LENGTH_SHORT).show();
                }
            }));
        }

        fromSharedLinkInsert();
    }

    public boolean isTablet() {
        return getResources().getBoolean(R.bool.is_tablet);
    }

    private void fromSharedLinkInsert() {
        Intent shared = getIntent();
        if(shared != null && shared.getStringExtra(Intent.EXTRA_TEXT) != null) {
            linkAction.showDialog(LinkAction.ACTION_ADD, new Link("", shared.getStringExtra(Intent.EXTRA_TEXT)), (mode, link) -> {
                if(ellenor.linkIsPassed(link))
                    linkViewModel.insertLink(link);
                else
                    Toast.makeText(this, "Kérlek töltsd ki a mezőket, különben nem tudom hozzáadni a linket", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        if(!isTablet()) menu.removeItem(R.id.menu_new_link);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_saved_articles) {
            startActivity(new Intent(this, ArticleSavedActivity.class));
        } else if(item.getItemId() == R.id.menu_load_links) {
            fileController.showFileChooser(this);
        } else if(item.getItemId() == R.id.menu_savelink) {
            fileSaveController.showDirectoryChooser(this);
        } else if(item.getItemId() == R.id.menu_new_link) {
            linkAction.showDialog(LinkAction.ACTION_ADD, null, (mode, link) -> {
                if (mode == LinkAction.ACTION_ADD) {
                    if (ellenor.linkIsPassed(link))
                        linkViewModel.insertLink(link);
                    else
                        Toast.makeText(this, "Kérlek töltsd ki a mezőket, különben nem tudom hozzáadni a linket", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void setuptActionBar(String title) {
        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    private void initRecyclerView(List<Link> links) {
        if(!isTablet()) {
            binding.list.setLayoutManager(new LinearLayoutManager(this));
            binding.list.setAdapter(new MyLinkRecyclerViewAdapter(this, links));
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.listFragment, new LinkListFragment(this, links))
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.articleFragment, new ArticleFragment( this))
                    .commit();
        }
    }

    @Override
    public void linkViewArticle(String url, String csatnev) {
        if(!isTablet()) {
            Intent intent = new Intent(this, ArticleActivity.class);
            intent.putExtra("article_title", csatnev);
            intent.putExtra("article_url", url);
            startActivity(intent);
        } else {
            ArticleFragment articleFragment = (ArticleFragment) getSupportFragmentManager().findFragmentById(R.id.articleFragment);
            articleFragment.initData(url);
        }
    }

    @Override
    public void linkUpdate(Link link) {
        linkAction.showDialog(LinkAction.ACTION_EDIT, link, (mode, link1) -> {
            if (mode == LinkAction.ACTION_EDIT) {
                linkViewModel.update(link1);
            }
        });
    }

    @Override
    public void deleteLink(Link link) {
        new AlertDialog.Builder(NewRssReader.this)
                .setTitle(link.getNev() + " törlése")
                .setMessage("Biztosan törlöd?")
                .setPositiveButton("igen", (dialog, which) -> linkViewModel.delete(link))
                .setNegativeButton("nem", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == LinksFileController.FILE_LOAD_RCODE && resultCode == RESULT_OK) {
            loadlinks(data);
        } else if(requestCode == LinksFileSaveController.DIRECTORY_CHOOSE && resultCode == RESULT_OK) {
            fileSaveController.saveLinks(linkViewModel.getLinksLiveData().getValue(), data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void saveArticle(Article article) {
        articleSavedViewModel.insert(article);
        Toast.makeText(this, article.getTitle()+" hír elmentve!", Toast.LENGTH_SHORT).show();
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

    private void loadlinks(Intent data) {
        List<Link> links = fileController.getLinks(data);
        if (links.isEmpty()) {
            Toast.makeText(this, "Adatok betöltése sikertelen volt :(", Toast.LENGTH_SHORT).show();
        } else {
            linkViewModel.insert(links);
            Log.i(TAG, "onActivityResult: Adatok betöltve");
            Toast.makeText(this, "Adatok betöltve", Toast.LENGTH_SHORT).show();
        }
    }
}