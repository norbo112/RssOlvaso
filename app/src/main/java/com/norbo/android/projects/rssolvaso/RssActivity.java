package com.norbo.android.projects.rssolvaso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.norbo.android.projects.rssolvaso.controller.RssController;
import com.norbo.android.projects.rssolvaso.model.RssItem;
import com.norbo.android.projects.rssolvaso.rcview.RssItemAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RssActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        final RecyclerView rv = findViewById(R.id.rvRssHir);
        final String link = getIntent().getStringExtra("link");

        TextView tvCsatornaCim = findViewById(R.id.tvCsatornaCim);
        tvCsatornaCim.setText(getIntent().getStringExtra("cim"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<RssItem> rssItems = RssController.getRssItems(link);
                if(rssItems != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rv.setAdapter(new RssItemAdapter(getApplicationContext(), rssItems));
                            rv.setItemAnimator(new DefaultItemAnimator());
                            rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        }
                    });
                }
            }
        }).start();
    }
}
