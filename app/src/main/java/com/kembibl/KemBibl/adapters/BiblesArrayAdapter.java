package com.kembibl.KemBibl.adapters;

import android.app.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kembibl.KemBibl.models.Bibles_locat_Model;
import com.kembibl.testbible3.R;

import java.util.ArrayList;
import java.util.List;

public class BiblesArrayAdapter extends ArrayAdapter<Bibles_locat_Model> {
    private Context context;
    private List<Bibles_locat_Model> Bibles_locat_Model;

    // Конструктор, вызывается при создании
    public BiblesArrayAdapter(Context context, int resource, ArrayList<Bibles_locat_Model> objects) {
        super(context, resource, objects);

        this.context = context;
        this.Bibles_locat_Model = objects;
    }




    // вызывается, когда генерируется список
    public View getView(int position, View convertView, ViewGroup parent) {




        // получаем свойство, которое потом отображаем
        Bibles_locat_Model bible_locat = Bibles_locat_Model.get(position);

        // получаем контекст со страницы библиотек
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bibles_layout , null);
        // получаем элементы, необходимые для работы
        TextView bible_name = (TextView) view.findViewById(R.id.bible_name);
        TextView bible_address = (TextView) view.findViewById(R.id.bible_address);
        ImageView image = (ImageView) view.findViewById(R.id.image);

        //устанавливаем необходимые значения
        String completeName = bible_locat.getName_bible();
        bible_name.setText(completeName);
        bible_address.setText("Адрес: "+bible_locat.getAddress_bible());

        //получаем и устанавливаем изображение
        int imageID = context.getResources().getIdentifier(bible_locat.getImage(), "drawable", context.getPackageName());
        image.setImageResource(imageID);

        return view;
    }
}
