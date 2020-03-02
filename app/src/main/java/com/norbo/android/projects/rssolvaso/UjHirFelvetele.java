package com.norbo.android.projects.rssolvaso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class UjHirFelvetele extends AppCompatActivity {
    public static final String EXTRA_CSAT_NEV = "com.norbo.android.projects.rssolvaso.EXTRA_REPLY_CSAT_NEV";
    public static final String EXTRA_CSAT_LINK = "com.norbo.android.projects.rssolvaso.EXTRA_REPLY_CSAT_LINK";

    private EditText etCsatNev;
    private EditText etCsatLink;

    private boolean menuEdit;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uj_hir_felvetele);

        etCsatNev = findViewById(R.id.etCsatornaNeve);
        etCsatLink = findViewById(R.id.etCsatornaLink);

        if(getIntent().getStringExtra(MainActivity.CSAT_NEV) != null)
            etCsatNev.setText(getIntent().getStringExtra(MainActivity.CSAT_NEV) );

        if(getIntent().getStringExtra(MainActivity.CSAT_LINK) != null)
            etCsatLink.setText(getIntent().getStringExtra(MainActivity.CSAT_LINK) );

        if(getIntent().getBooleanExtra(MainActivity.MENU_EDIT, false))
            menuEdit = true;

        id = getIntent().getIntExtra(MainActivity.CSAT_ID, 0);

        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(TextUtils.isEmpty(etCsatNev.getText()) || TextUtils.isEmpty(etCsatLink.getText())) {
                    setResult(RESULT_CANCELED, intent);
                } else {
                    String nev = etCsatNev.getText().toString();
                    String link = etCsatLink.getText().toString();
                    intent.putExtra(EXTRA_CSAT_NEV, nev);
                    intent.putExtra(EXTRA_CSAT_LINK, link);
                    if(menuEdit)
                        intent.putExtra(MainActivity.MENU_EDIT, true);

                    if(id != 0)
                        intent.putExtra(MainActivity.CSAT_ID, id);

                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

    }
}
