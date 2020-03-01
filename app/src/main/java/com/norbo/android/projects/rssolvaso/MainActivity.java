package com.norbo.android.projects.rssolvaso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.norbo.android.projects.rssolvaso.database.model.RssLink;
import com.norbo.android.projects.rssolvaso.database.viewmodel.RssLinkViewModel;
import com.norbo.android.projects.rssolvaso.model.RssItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_NEW_LINK = 110;
    //private static final Map<String, String> urlmap = new HashMap<>();
    private String[] urlMapString = new String[100];
    private int urlMapStringPoz = 0;

    private RssLinkViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initUrlMap();

        ListView lv = findViewById(R.id.listCsatorna);
        final RecyclerView rv = findViewById(R.id.rvRssHir);

        viewModel = new ViewModelProvider(this).get(RssLinkViewModel.class);

        viewModel.getAllLinks().observe(this, new Observer<List<RssLink>>() {
            @Override
            public void onChanged(List<RssLink> rssLinks) {
                /*int capacity = 0;
                for (RssLink link: rssLinks) {
                    urlMapString[capacity++] = link.getCsatornaNeve();
                }
                urlMapString = Arrays.copyOf(urlMapString, capacity);*/
                lv.setAdapter(new ArrayAdapter<RssLink>(MainActivity.this
                        , android.R.layout.simple_list_item_1,
                        rssLinks));
            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RssActivity.class);
                intent.putExtra("cim", urlMapString[position]);
                intent.putExtra("link", viewModel.getAllLinks().getValue().get(position).getCsatornaLink());
                startActivity(intent);
            }
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, UjHirFelvetele.class), REQUEST_CODE_NEW_LINK);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_NEW_LINK && resultCode == RESULT_OK) {
            RssLink link = new RssLink(data.getStringExtra(UjHirFelvetele.EXTRA_CSAT_NEV),
                    data.getStringExtra(UjHirFelvetele.EXTRA_CSAT_LINK));
            viewModel.insert(link);
        } else {
            showToast(getResources().getString(R.string.empty_not_saved));
        }

    }

    private void showToast(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sajat_toast, (ViewGroup) findViewById(R.id.layoutToast));
        TextView tv = view.findViewById(R.id.toastText);
        tv.setText(msg);
        Toast toast = new Toast(MainActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(view);
        toast.show();
    }

    private void initUrlMap() {
//        urlmap.put("Itthon", "https://www.origo.hu/contentpartner/rss/itthon/origo.xml");
//        urlmap.put("Nagyvilág", "https://www.origo.hu/contentpartner/rss/nagyvilag/origo.xml");
//        urlmap.put("Gazdaság", "https://www.origo.hu/contentpartner/rss/uzletinegyed/origo.xml");
//        urlmap.put("Filmklub", "https://www.origo.hu/contentpartner/rss/filmklub/origo.xml");
//        urlmap.put("Sport", "https://www.origo.hu/contentpartner/rss/sport/origo.xml");
//        urlmap.put("Tudomány", "https://www.origo.hu/contentpartner/rss/tudomany/origo.xml");
//        urlmap.put("Technika", "https://www.origo.hu/contentpartner/rss/techbazis/origo.xml");
//        urlmap.put("HVG-Világ", "http://hvg.hu/rss/vilag");
//        urlmap.put("HVG-Gazdaság", "http://hvg.hu/rss/gazdasag");
//        urlmap.put("HVG-Itthon", "http://hvg.hu/rss/itthon");
//        urlmap.put("Index 24 Óra", "https://index.hu/24ora/rss/");
//        urlmap.put("NewYork Times: Europe", "https://rss.nytimes.com/services/xml/rss/nyt/Europe.xml");

//        for (Map.Entry<String, String> entry: urlmap.entrySet()) {
//            urlMapString[urlMapStringPoz++] = entry.getKey();
//        }
//
//        urlMapString = Arrays.copyOf(urlMapString, urlMapStringPoz);
//        Arrays.sort(urlMapString);
    }

    /*private String getUrl(String keyName) {
        if(urlmap.containsKey(keyName)) {
            return urlmap.get(keyName);
        }

        return null;
    }*/

    private String getUrl(List<RssLink> links, String search) {
        for (RssLink link: links) {
            if(link.getCsatornaNeve().equals(search)) {
                return link.getCsatornaLink();
            }
        }
        return null;
    }
}
