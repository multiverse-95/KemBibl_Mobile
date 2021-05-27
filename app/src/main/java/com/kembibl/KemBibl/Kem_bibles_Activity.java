package com.kembibl.KemBibl;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kembibl.KemBibl.adapters.BiblesArrayAdapter;
import com.kembibl.KemBibl.models.BiblesModel;
import com.kembibl.KemBibl.models.Bibles_locat_Model;
import com.kembibl.testbible3.R;

import java.util.ArrayList;
//Класс отвечает за отображение списка библиотек
public class Kem_bibles_Activity extends AppCompatActivity {

    ListView mBibles_list;
    int position =0;
    private ArrayAdapter<BiblesModel> mAdapter;
    // final String[] catNamesArray = new String[] { };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kem_bibles);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Библиотеки");

        mBibles_list=(ListView)findViewById(R.id.myBibles);
        ArrayList<Bibles_locat_Model> bibles_locatModel = new ArrayList<>();
        for (int i = 0; i< BiblesModel.BIBLES.length; i++){
            String short_name= BiblesModel.BIBLES[i].getName();
            String address_bible= BiblesModel.BIBLES[i].getAddress();
            String Image= BiblesModel.BIBLES[i].getImage();
            bibles_locatModel.add(
                    new Bibles_locat_Model(short_name,address_bible,Image));
        }
        ArrayAdapter<Bibles_locat_Model> mAdapter = new BiblesArrayAdapter(this, 0, bibles_locatModel);

       // mAdapter = new ArrayAdapter<BiblesModel>(this, android.R.layout.simple_list_item_1, BiblesModel.BIBLES);
        //Получение списка книг и установка адаптера
        mBibles_list.setAdapter(mAdapter);
        mBibles_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //String songName=mDrink_Category.getItemAtPosition(position).toString();
                String geoURI= BiblesModel.BIBLES[position].getCoord();
                Uri geo = Uri.parse(geoURI);
                Intent geoIntent = new Intent(Intent.ACTION_VIEW, geo);

                if (geoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(geoIntent);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
