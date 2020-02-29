package com.norbo.android.projects.rssolvaso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.norbo.android.projects.rssolvaso.controller.RssController;
import com.norbo.android.projects.rssolvaso.model.RssItem;
import com.norbo.android.projects.rssolvaso.rcview.RssItemAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final Map<String, String> urlmap = new HashMap<>();
    private String[] urlMapString = new String[20];
    private int urlMapStringPoz = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUrlMap();

        ListView lv = findViewById(R.id.listCsatorna);
        final RecyclerView rv = findViewById(R.id.rvRssHir);

        lv.setAdapter(new ArrayAdapter<String>(this
                , android.R.layout.simple_list_item_1,
                urlMapString));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RssActivity.class);
                intent.putExtra("cim", urlMapString[position]);
                intent.putExtra("link", getUrl(urlMapString[position]));
                startActivity(intent);
            }
        });

    }

    private void initUrlMap() {
        urlmap.put("Itthon", "https://www.origo.hu/contentpartner/rss/itthon/origo.xml");
        urlmap.put("Nagyvilág", "https://www.origo.hu/contentpartner/rss/nagyvilag/origo.xml");
        urlmap.put("Gazdaság", "https://www.origo.hu/contentpartner/rss/uzletinegyed/origo.xml");
        urlmap.put("Filmklub", "https://www.origo.hu/contentpartner/rss/filmklub/origo.xml");
        urlmap.put("Sport", "https://www.origo.hu/contentpartner/rss/sport/origo.xml");
        urlmap.put("Tudomány", "https://www.origo.hu/contentpartner/rss/tudomany/origo.xml");
        urlmap.put("Technika", "https://www.origo.hu/contentpartner/rss/techbazis/origo.xml");
        urlmap.put("HVG-Világ", "http://hvg.hu/rss/vilag");
        urlmap.put("HVG-Gazdaság", "http://hvg.hu/rss/gazdasag");
        urlmap.put("HVG-Itthon", "http://hvg.hu/rss/itthon");
        urlmap.put("Index 24 Óra", "https://index.hu/24ora/rss/");
        urlmap.put("NewYork Times: Europe", "https://rss.nytimes.com/services/xml/rss/nyt/Europe.xml");

        for (Map.Entry<String, String> entry: urlmap.entrySet()) {
            urlMapString[urlMapStringPoz++] = entry.getKey();
        }

        urlMapString = Arrays.copyOf(urlMapString, urlMapStringPoz);
        Arrays.sort(urlMapString);
    }

    private String getUrl(String keyName) {
        if(urlmap.containsKey(keyName)) {
            return urlmap.get(keyName);
        }

        return null;
    }
}
