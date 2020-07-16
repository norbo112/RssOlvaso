package com.norbo.android.projects.rssolvaso.mvvm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.databinding.ActivityNewRssReaderBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.LinkClickedListener;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.LinkUpdateListener;
import com.norbo.android.projects.rssolvaso.mvvm.ui.adapters.MyLinkRecyclerViewAdapter;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.BevitelHibaEllenor;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.DefaultLinks;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.LinksFileController;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.LinksFileSaveController;
import com.norbo.android.projects.rssolvaso.mvvm.ui.utils.actions.LinkAction;
import com.norbo.android.projects.rssolvaso.mvvm.ui.viewmodels.LinkViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewRssReader extends AppCompatActivity implements LinkClickedListener, LinkUpdateListener {
    private static final String TAG = "NewRssReader";
    private LinkViewModel linkViewModel;

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

        fromSharedLinkInsert();
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void setuptActionBar(String title) {
        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    private void initRecyclerView(List<Link> links) {
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(new MyLinkRecyclerViewAdapter(this, links));
    }

    @Override
    public void linkViewArticle(String url, String csatnev) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("article_title", csatnev);
        intent.putExtra("article_url", url);
        startActivity(intent);
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