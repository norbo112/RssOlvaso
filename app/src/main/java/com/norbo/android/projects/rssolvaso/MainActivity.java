package com.norbo.android.projects.rssolvaso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.norbo.android.projects.rssolvaso.database.model.RssLink;
import com.norbo.android.projects.rssolvaso.database.viewmodel.RssLinkViewModel;
import com.norbo.android.projects.rssolvaso.model.RssItem;
import com.norbo.android.projects.rssolvaso.model.sajatlv.SajatListViewAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String MENU_EDIT = "Szerkesztés";
    private static final String MENU_DELETE = "Törlés";

    public static final String CSAT_NEV = "csatnev";
    public static final String CSAT_LINK = "csatlink";
    public static final String CSAT_ID = "csatid";

    private static final int REQUEST_CODE_NEW_LINK = 110;

    private RssLinkViewModel viewModel;

    private ProgressDialog dialog;
    private TextView tvcityName;
    private TextView tvFok;
    private TextView tvSzel;
    private TextView tvDesc;
    private ImageView imIcon;
    private MenuItem menuItemIdojaras;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_appbar_layout);
        getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        imIcon = view.findViewById(R.id.weather_logo);
        tvDesc = view.findViewById(R.id.weather_info);
        ImageView applogohome = view.findViewById(R.id.applogo_home);
        applogohome.setOnClickListener((event) -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        ImageView appSavedHirek = view.findViewById(R.id.viewSaveHirek);
        appSavedHirek.setOnClickListener((event) -> {
            startActivity(new Intent(getApplicationContext(), SavedHirekActivity.class));
        });
        new WeatherActivity(this).doWeather(imIcon, tvDesc);
        imIcon.setOnClickListener((event) -> {
            new WeatherActivity(this).doWeather(imIcon, tvDesc);
        });

        ListView lv = findViewById(R.id.listCsatorna);

        viewModel = new ViewModelProvider(this).get(RssLinkViewModel.class);

        viewModel.getAllLinks().observe(this, new Observer<List<RssLink>>() {
            @Override
            public void onChanged(List<RssLink> rssLinks) {
                lv.setAdapter(new SajatListViewAdapter(MainActivity.this, rssLinks));
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

        registerForContextMenu(lv);

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
            if(data.getBooleanExtra(MainActivity.MENU_EDIT, false)) {

                viewModel.update(data.getIntExtra(MainActivity.CSAT_ID,0),
                        data.getStringExtra(UjHirFelvetele.EXTRA_CSAT_NEV),
                        data.getStringExtra(UjHirFelvetele.EXTRA_CSAT_LINK));
            } else {
                viewModel.insert(link);
            }
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
        toast.setGravity(Gravity.BOTTOM, 0, 20);
        toast.setView(view);
        toast.show();
    }

    private void showAlertDialog(int poz) {
        int csatornaid = viewModel.getAllLinks().getValue().get(poz).getId();
        String csatronanev = viewModel.getAllLinks().getValue().get(poz).getCsatornaNeve();
        new AlertDialog.Builder(this)
                .setMessage("Biztos törölni akarod?")
                .setTitle(csatronanev+" törlése")
                .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.delete(csatornaid);
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
    }

    int kijeloltRssLinkPoz;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo mi = (AdapterView.AdapterContextMenuInfo) menuInfo;

        if(v == findViewById(R.id.listCsatorna)) {
            menu.add(MENU_EDIT);
            menu.add(MENU_DELETE);
            menu.setHeaderTitle("Szerkesztő");
            menu.setHeaderIcon(R.drawable.ic_edit);
            kijeloltRssLinkPoz = mi.position;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        switch (title) {
            case MENU_EDIT:
                Intent intent = new Intent(this, UjHirFelvetele.class);
                intent.putExtra(MENU_EDIT ,true);
                intent.putExtra(CSAT_NEV, viewModel.getAllLinks().getValue().get(kijeloltRssLinkPoz).getCsatornaNeve());
                intent.putExtra(CSAT_LINK, viewModel.getAllLinks().getValue().get(kijeloltRssLinkPoz).getCsatornaLink());
                intent.putExtra(CSAT_ID, viewModel.getAllLinks().getValue().get(kijeloltRssLinkPoz).getId());
                startActivityForResult(intent, REQUEST_CODE_NEW_LINK);
                break;
            case MENU_DELETE:
                showAlertDialog(kijeloltRssLinkPoz);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.menuIdojaras) {
//            new WeatherActivity(this).doWeather(menuItemIdojaras);
//            //startActivity(new Intent(this, WeatherActivity.class));
//        }
        return super.onOptionsItemSelected(item);
    }
}
