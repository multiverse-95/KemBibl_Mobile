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

public class BookGettingArrayAdapter extends ArrayAdapter<BookModel> {
    private Context context;
    private List<BookModel> bookModels;

    // Конструктор, вызывается при создании
    public BookGettingArrayAdapter(Context context, int resource, ArrayList<BookModel> objects) {
        super(context, resource, objects);

        this.context = context;
        this.bookModels = objects;
    }



    // вызывается, когда генерируется список
    public View getView(int position, View convertView, ViewGroup parent) {




        // получаем свойство, которое потом отображаем
        BookModel bookModel = bookModels.get(position);

        // получаем контекст со страницы ранее взятых книг
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.book_history_layout, null);
        // получаем необходимые поля
        TextView author = (TextView) view.findViewById(R.id.author_book);
        TextView title = (TextView) view.findViewById(R.id.title_book);
        TextView date = (TextView) view.findViewById(R.id.date_book);
        TextView type_book = (TextView) view.findViewById(R.id.type_book);
        TextView date_start=(TextView) view.findViewById(R.id.date_start);
        TextView date_finish=(TextView) view.findViewById(R.id.date_finish);
        ImageView image = (ImageView) view.findViewById(R.id.image);

        //установка параметров
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

        //установка параметров
        date.setText("Дата выпуска: " + String.valueOf(bookModel.getDate()));
        type_book.setText("Тип: "+ String.valueOf(bookModel.getTypeBook()));
        date_start.setText("Дата выдачи: "+String.valueOf(bookModel.getDate_start()));
        date_finish.setText("Дата возврата: "+String.valueOf(bookModel.getDate_finish()));


        //получаем и устанавливаем изображение
        int imageID = context.getResources().getIdentifier(bookModel.getImage(), "drawable", context.getPackageName());
        image.setImageResource(imageID);

        return view;
    }
}
