package com.kembibl.KemBibl.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kembibl.KemBibl.models.BookModel;
import com.kembibl.testbible3.R;

import java.util.ArrayList;
import java.util.List;

public class BookArrayAdapter extends ArrayAdapter<BookModel> {
    private Context context;
    private List<BookModel> bookModels;

    // Конструктор, вызывается при создании
    public BookArrayAdapter(Context context, int resource, ArrayList<BookModel> objects) {
        super(context, resource, objects);

        this.context = context;
        this.bookModels = objects;
    }



    // вызывается, когда генерируется список
    public View getView(int position, View convertView, ViewGroup parent) {




        // получаем свойство, которое потом отображаем
        BookModel bookModel = bookModels.get(position);

        // получаем контекст со страницы описания книги
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.book_layout, null);

        TextView author = (TextView) view.findViewById(R.id.author_book);
        TextView title = (TextView) view.findViewById(R.id.title_book);
        TextView date = (TextView) view.findViewById(R.id.date_book);
        TextView type_book = (TextView) view.findViewById(R.id.type_book);
        ImageView image = (ImageView) view.findViewById(R.id.image);

        //устанавливаем нужные значения
        String completeAuthor = bookModel.getAuthor();
        author.setText(completeAuthor);

        // если описание книги слишком большое, ставить многоточия после определенного кол-ва символов
        int titleLength = bookModel.getTitle().length();
        if(titleLength >= 100){
            String descriptionTrim = bookModel.getTitle().substring(0, 100) + "...";
            title.setText(descriptionTrim);
        }else{
            title.setText(bookModel.getTitle());
        }

        // устанавливаем дату и тип книги
        date.setText("Дата выпуска: " + String.valueOf(bookModel.getDate()));
        type_book.setText("Тип: "+ String.valueOf(bookModel.getTypeBook()));


        //получаем и устанавливаем изображение
        int imageID = context.getResources().getIdentifier(bookModel.getImage(), "drawable", context.getPackageName());
        image.setImageResource(imageID);

        return view;
    }
}
