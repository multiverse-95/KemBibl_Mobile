package com.kembibl.KemBibl.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kembibl.KemBibl.models.Book_Exempl_Model;
import com.kembibl.testbible3.R;

import java.util.ArrayList;
import java.util.List;

public class BookExemplAdapter extends ArrayAdapter<Book_Exempl_Model> {
    private Context context;
    private List<Book_Exempl_Model> Books_Exempl;

    // Конструктор, вызывается при создании
    public BookExemplAdapter(Context context, int resource, ArrayList<Book_Exempl_Model> objects) {
        super(context, resource, objects);

        this.context = context;
        this.Books_Exempl = objects;
    }




    // вызывается, когда генерируется список
    public View getView(int position, View convertView, ViewGroup parent) {




        // получаем свойство, которое потом отображаем
        Book_Exempl_Model book_exemplModel = Books_Exempl.get(position);

        // получаем контекст со страницы экземпляра книги
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.book_exempl_layout, null);

        TextView address = (TextView) view.findViewById(R.id.address_book);
        TextView fond = (TextView) view.findViewById(R.id.fond_book);
        TextView count = (TextView) view.findViewById(R.id.count_book);
        ImageView image = (ImageView) view.findViewById(R.id.image);

        //установка атрибутов
        String completeAddress = book_exemplModel.getAddress_book();
        address.setText(completeAddress);



        fond.setText("Фонд: " + String.valueOf(book_exemplModel.getFond_book()));


        //установка доступных для выдачи книг
        count.setText("Доступно для выдачи: " + String.valueOf(book_exemplModel.getCount_book()));


        //получаем и устанавливаем изображение
        int imageID = context.getResources().getIdentifier(book_exemplModel.getImage(), "drawable", context.getPackageName());
        image.setImageResource(imageID);

        return view;
    }
}
