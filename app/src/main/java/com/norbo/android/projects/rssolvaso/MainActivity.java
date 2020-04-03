package com.norbo.android.projects.rssolvaso;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.norbo.android.projects.rssolvaso.acutils.LoactionUtil;
import com.norbo.android.projects.rssolvaso.acutils.LocationInterfaceActivity;
import com.norbo.android.projects.rssolvaso.acutils.graphicutils.DrawIdojaraToImage;
import com.norbo.android.projects.rssolvaso.acutils.weather.DoWeatherImpl;
import com.norbo.android.projects.rssolvaso.acutils.weather.WeatherInterface;
import com.norbo.android.projects.rssolvaso.controller.FileController;
import com.norbo.android.projects.rssolvaso.database.model.RssLink;
import com.norbo.android.projects.rssolvaso.database.viewmodel.RssLinkViewModel;
import com.norbo.android.projects.rssolvaso.model.sajatlv.SajatListViewAdapter;
import com.norbo.android.projects.rssolvaso.model.weather.Weather;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class MainActivity extends AppCompatActivity implements LocationInterfaceActivity {
    public static final String MENU_EDIT = "Szerkesztés";
    private static final String MENU_DELETE = "Törlés";
    public static final String CSAT_ID = "csatid";

    private static final int REQUEST_CODE_NEW_LINK = 110;
    private static final int READ_WRITE_STORAGE = 100;
    private static final int LOCATION_PERM = 200;
    private static final String TAG = "MainActivity";

    private RssLinkViewModel viewModel;
    private SajatListViewAdapter listViewAdapter;

    private FloatingActionButton fab;
    private ImageView imIcon;
    private ListView lv;

    private FileController fileController;
    private DoWeatherImpl doWeatherImpl;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private CompletableFuture<Weather> weatherFuture = null;

    private DrawIdojaraToImage drawIdojaraToImage;
    private Bitmap magyarZaszloBitmap;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hír olvasó");
        toolbar.setLogo(R.drawable.ic_rss_feed_black_24dp);
        setSupportActionBar(toolbar);

        imIcon = findViewById(R.id.weather_logo_cl);
        doWeatherImpl = new DoWeatherImpl(this);
        magyarZaszloBitmap =
                BitmapFactory.decodeResource(getResources(), R.mipmap.magyarzaszlo);

        doWeatherImage(doWeatherImpl, magyarZaszloBitmap,false);

        imIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWeatherImage(doWeatherImpl, magyarZaszloBitmap, true);
            }
        });

        lv = findViewById(R.id.listCsatorna);

        lv.setNestedScrollingEnabled(true);
        lv.startNestedScroll(View.OVER_SCROLL_ALWAYS);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateLinkDialog(null,false,0);
            }
        });

        viewModel = new ViewModelProvider(this).get(RssLinkViewModel.class);

        viewModel.getAllLinks().observe(this, new Observer<List<RssLink>>() {
            @Override
            public void onChanged(List<RssLink> rssLinks) {
                listViewAdapter = new SajatListViewAdapter(MainActivity.this, rssLinks);
                lv.setAdapter(listViewAdapter);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RssActivity.class);
                final RssLink item = listViewAdapter.getItem(position);
                intent.putExtra("cim", item.getCsatornaNeve());
                intent.putExtra("link", item.getCsatornaLink());
                startActivity(intent);
            }
        });

        registerForContextMenu(lv);

        Intent fromShare = getIntent();
        String action = fromShare.getAction();
        String type = fromShare.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            showCreateLinkDialog(fromShare, false, 0);
        }
    }

    private void doWeatherImage(WeatherInterface weatherInterface, Bitmap bitmap, boolean clickbyikon) {
        drawIdojaraToImage = new DrawIdojaraToImage(getApplicationContext(), bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            weatherFuture = CompletableFuture.supplyAsync(new Supplier<Weather>() {
                @Override
                public Weather get() {
                    return LoactionUtil.getLocatedWeather(MainActivity.this, doWeatherImpl, clickbyikon);
                }
            }, executorService);

            if(weatherFuture != null) {
                weatherFuture.thenAccept((weather) -> {
                    runOnUiThread(() -> {
                        Bitmap newBitmap = drawIdojaraToImage.drawWeather(weather);
                        imIcon.setImageBitmap(newBitmap);
                    });
                });
            }
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
        int csatornaid = listViewAdapter.getItem(poz).getId();
        String csatronanev = listViewAdapter.getItem(poz).getCsatornaNeve();

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
    private void showCreateLinkDialog(Intent intent, boolean edit, int position) {
        final View customView = LayoutInflater.from(this).inflate(R.layout.dialog_uj_hir_felvetele, null);
        EditText etNev = customView.findViewById(R.id.etdialogNeve);
        EditText etLink = customView.findViewById(R.id.etdialogLink);

        String sharedLink = intent != null ? intent.getStringExtra(Intent.EXTRA_TEXT) : null;
        if(sharedLink != null) {
            etLink.setText(sharedLink);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fomenu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((SajatListViewAdapter)lv.getAdapter()).getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_saved : {
                startActivity(new Intent(this, SavedHirekActivity.class));
                break;
            }
            case R.id.menu_export : {
                fileController = new FileController(FileController.IRAS, MainActivity.this);
                fileController.execute(viewModel.getAllLinks().getValue());
                break;
            }
            case R.id.menu_import : {
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
                break;
            }
            case R.id.menu_ujhir :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    showCreateLinkDialog(null,false,0);
                } else {
                    showToast("Nem támogatott funkc.");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
                showCreateLinkDialog(null,true, kijeloltRssLinkPoz);
                break;
            case MENU_DELETE:
                showAlertDialog(kijeloltRssLinkPoz);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            doWeatherImage(doWeatherImpl,
                    BitmapFactory.decodeResource(getResources(), R.mipmap.iss), true);
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            doWeatherImage(doWeatherImpl,
                    BitmapFactory.decodeResource(getResources(), R.mipmap.magyarzaszlo), true);
        }
        return true;
    }

    public ListView getLv() {
        return lv;
    }

    @Override
    public TextView getTvDesc() {
        return null;
    }

    @Override
    public ImageView getImIcon() {
        return imIcon;
    }
}
