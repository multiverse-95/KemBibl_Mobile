package com.kembibl.KemBibl;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kembibl.testbible3.BuildConfig;
import com.kembibl.testbible3.R;
//Класс, который отвечает за отображение информации о приложении
public class AboutAppActivity extends AppCompatActivity {
    EditText SiteMibsSearch;
    EditText SiteMibs;
    EditText SiteKemsu;
    TextView versionApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        SiteMibsSearch=(EditText)findViewById(R.id.mibs_site);
        SiteMibs=(EditText)findViewById(R.id.mibs_bibl_site);
        SiteKemsu=(EditText)findViewById(R.id.kemsu_site);
        versionApp=(TextView)findViewById(R.id.versionApp);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("О приложении");

        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        versionApp.setText("КемБибл: Поиск книг\nВерсия: "+versionName+versionCode);
        //Установка ссылок кликабельными
        SiteMibsSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://catalog.kembibl.ru/"));
                startActivity(browserIntent);
            }
        });
        SiteMibs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://библиотеки.кемеровские.рф"));
                startActivity(browserIntent);

            }
        });
        SiteKemsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kemsu.ru/"));
                startActivity(browserIntent);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //this.finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
