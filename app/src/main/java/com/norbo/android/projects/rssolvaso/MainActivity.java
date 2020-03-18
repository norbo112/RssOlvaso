package com.norbo.android.projects.rssolvaso;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.norbo.android.projects.rssolvaso.acutils.LoactionUtil;
import com.norbo.android.projects.rssolvaso.acutils.LocationInterfaceActivity;
import com.norbo.android.projects.rssolvaso.controller.FileController;
import com.norbo.android.projects.rssolvaso.database.model.RssLink;
import com.norbo.android.projects.rssolvaso.database.viewmodel.RssLinkViewModel;
import com.norbo.android.projects.rssolvaso.model.sajatlv.SajatListViewAdapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements LocationInterfaceActivity {
    public static final String MENU_EDIT = "Szerkesztés";
    private static final String MENU_DELETE = "Törlés";

    public static final String CSAT_NEV = "csatnev";
    public static final String CSAT_LINK = "csatlink";
    public static final String CSAT_ID = "csatid";

    private static final int REQUEST_CODE_NEW_LINK = 110;
    private static final int READ_WRITE_STORAGE = 100;
    private static final int LOCATION_PERM = 200;
    private static final String TAG = "MainActivity";

    private RssLinkViewModel viewModel;

    private TextView tvDesc;
    private ImageView imIcon;
    private ListView lv;
    private ImageView btnPopupFile;

    private FileController fileController;

    private LocationManager lm;
    private DoWeatherImpl doWeatherImpl;

    private FloatingActionButton fab, fabExport, fabNew;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        }, READ_WRITE_STORAGE);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, LOCATION_PERM);

        doWeatherImpl = new DoWeatherImpl(this);
//        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_appbar_layout);
        getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        imIcon = view.findViewById(R.id.weather_logo);
        tvDesc = view.findViewById(R.id.weather_info);

        LoactionUtil.updateLocationWithFusedLPC(MainActivity.this, doWeatherImpl, false);

        ImageView appSavedHirek = view.findViewById(R.id.viewSaveHirek);
        appSavedHirek.setOnClickListener((event) -> {
            startActivity(new Intent(getApplicationContext(), SavedHirekActivity.class));
        });
        //weatherActivity.doWeather(imIcon, tvDesc, false);
        imIcon.setOnClickListener((event) -> {
            //weatherActivity.doWeather(imIcon, tvDesc, true);
            LoactionUtil.updateLocationWithFusedLPC(MainActivity.this, doWeatherImpl, true);
        });

        lv = findViewById(R.id.listCsatorna);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(new Intent(MainActivity.this, UjHirFelvetele.class), REQUEST_CODE_NEW_LINK);
                if (fabExport.isShown() || fabNew.isShown()) {
                    fabNew.hide();
                    fabExport.hide();
                } else {
                    fabNew.show();
                    fabExport.show();
                }
            }
        });


        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                    fab.hide();
                    if (fabExport.isShown() || fabNew.isShown()) {
                        fabNew.hide();
                        fabExport.hide();
                    }
                } else {
                    fab.show();
                }
            }
        });

        //btnPopupFile = findViewById(R.id.btnPopupFile);
        fabExport = findViewById(R.id.fabExport);
        fabExport.hide();
        fabExport.setOnClickListener(popupFileListener);
        fabNew = findViewById(R.id.fabNew);
        fabNew.hide();
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(new Intent(MainActivity.this, UjHirFelvetele.class), REQUEST_CODE_NEW_LINK);
                showCreateLinkDialog(false,0);
            }
        });
        //btnPopupFile.setOnClickListener(popupFileListener);

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

        Intent fromShare = getIntent();
        String action = fromShare.getAction();
        String type = fromShare.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            showCreateLinkDialog(fromShare);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == READ_WRITE_STORAGE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(getClass().getSimpleName(), "onRequestPermissionsResult: permission granted!");
            }
        } else if(requestCode == LOCATION_PERM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //TODO itt máskell...
            }
        }
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

    public void showToast(String msg) {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showCreateLinkDialog(boolean edit, int position) {
        final View customView = LayoutInflater.from(this).inflate(R.layout.dialog_uj_hir_felvetele, null);
        EditText etNev = customView.findViewById(R.id.etdialogNeve);
        EditText etLink = customView.findViewById(R.id.etdialogLink);
        Integer csatId = null;
        if(edit) {
            RssLink link = viewModel.getAllLinks().getValue().get(position);
            csatId = link.getId();
            etNev.setText(link.getCsatornaNeve());
            etLink.setText(link.getCsatornaLink());
        }

        Integer finalCsatId = csatId;
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyAlertDialog))
                .setView(customView)
                .setPositiveButton((edit ? "szerkeszt" : "felvesz"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(etNev.getText().toString()) || TextUtils.isEmpty(etLink.getText().toString())) {
                            showToast("Üres a név vagy a link, kérlek töltsd ki");
                        } else {
                            RssLink rssLink = new RssLink(etNev.getText().toString(), etLink.getText().toString());
                            if(edit && finalCsatId != null) {
                                viewModel.update(finalCsatId, etNev.getText().toString(), etLink.getText().toString());
                            } else {
                                viewModel.insert(rssLink);
                            }
                            showToast("Új csatorna "+(edit ? "Szerkesztve":"Hozzáadva"));
                            fabHide();
                        }
                    }
                })
                .setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(getClass().getSimpleName(), "onClick: nincs felvétel");
                    }
                })
                .create().show();
    }

    private void showCreateLinkDialog(Intent intent) {
        final View customView = LayoutInflater.from(this).inflate(R.layout.dialog_uj_hir_felvetele, null);
        EditText etNev = customView.findViewById(R.id.etdialogNeve);
        EditText etLink = customView.findViewById(R.id.etdialogLink);
        String sharedLink = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(sharedLink != null) {
            etLink.setText(sharedLink);
        }

        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyAlertDialog))
                .setView(customView)
                .setPositiveButton("Felvesz", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(etNev.getText().toString()) || TextUtils.isEmpty(etLink.getText().toString())) {
                            showToast("Üres a név vagy a link, kérlek töltsd ki");
                        } else {
                            RssLink rssLink = new RssLink(etNev.getText().toString(), etLink.getText().toString());
                            viewModel.insert(rssLink);
                            showToast("Új csatorna hozzáadva");

                            fabHide();
                        }
                    }
                })
                .setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(getClass().getSimpleName(), "onClick: nincs felvétel");
                    }
                })
                .create().show();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        switch (title) {
            case MENU_EDIT:
                showCreateLinkDialog(true, kijeloltRssLinkPoz);
                break;
            case MENU_DELETE:
                showAlertDialog(kijeloltRssLinkPoz);
                break;
        }
        return super.onContextItemSelected(item);
    }

    public ListView getLv() {
        return lv;
    }

    @Override
    public TextView getTvDesc() {
        return tvDesc;
    }

    @Override
    public ImageView getImIcon() {
        return imIcon;
    }

    private View.OnClickListener popupFileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
            popupMenu.inflate(R.menu.popupfile);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.menuExportList) {
                        fileController = new FileController(FileController.IRAS, MainActivity.this);
                        fileController.execute(viewModel.getAllLinks().getValue());
                    } else if(item.getItemId() == R.id.menuImportList) {
                        fileController = new FileController(FileController.OLVASAS, MainActivity.this);
                        try {
                            List<RssLink> rssLinks = fileController.execute(viewModel.getAllLinks().getValue()).get();
                            if(rssLinks != null) {
                                for (RssLink r : rssLinks) {
                                    viewModel.insert(r);
                                }
                            }
                        } catch (ExecutionException e) {
                            Log.e(getClassLoader().getClass().getSimpleName(), "onMenuItemClick: exec", e);
                        } catch (InterruptedException e) {
                            Log.e(getClassLoader().getClass().getSimpleName(), "onMenuItemClick: inter", e);
                        }

                    }

                    if(fabExport.isShown()) {
                        fabExport.hide();
                        fabNew.hide();
                    }
                    return true;
                }
            });
            popupMenu.show();
        }
    };

    private void fabHide() {
        if(fabNew.isShown() || fabExport.isShown()) {
            fabExport.hide();
            fabNew.hide();
        }
    }
}
