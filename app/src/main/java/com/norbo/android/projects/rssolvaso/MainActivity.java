package com.norbo.android.projects.rssolvaso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        ListView lv = findViewById(R.id.listCsatorna);

        viewModel = new ViewModelProvider(this).get(RssLinkViewModel.class);

        viewModel.getAllLinks().observe(this, new Observer<List<RssLink>>() {
            @Override
            public void onChanged(List<RssLink> rssLinks) {
                lv.setAdapter(new ArrayAdapter<RssLink>(MainActivity.this
                        , android.R.layout.simple_list_item_1,
                        rssLinks));
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RssActivity.class);
                intent.putExtra("cim", viewModel.getAllLinks().getValue().get(position).getCsatornaNeve());
                intent.putExtra("link", viewModel.getAllLinks().getValue().get(position).getCsatornaLink());
                startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(this)
                    .setMessage("Biztos törölni akarod?")
                    .setTitle("Csatorna törlése")
                    .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.delete(viewModel.getAllLinks().getValue().get(position).getCsatornaNeve());
                        }
                    })
                    .setNegativeButton("Nem", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i(MainActivity.class.getSimpleName(), "onClick: nem törölted");
                        }
                    })
                    .create()
                    .show();
            return true;
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

    private String getUrl(List<RssLink> links, String search) {
        for (RssLink link: links) {
            if(link.getCsatornaNeve().equals(search)) {
                return link.getCsatornaLink();
            }
        }
        return null;
    }
}
