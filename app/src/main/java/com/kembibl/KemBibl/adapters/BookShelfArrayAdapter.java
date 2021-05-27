package com.kembibl.KemBibl.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kembibl.KemBibl.models.BookModel;
import com.kembibl.KemBibl.ExemplActivity;
import com.kembibl.KemBibl.requests.RequestGET_delFromPolka;
import com.kembibl.testbible3.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookShelfArrayAdapter extends ArrayAdapter<BookModel> {
    private Context context;
    private List<BookModel> bookModels;

    // Конструктор, вызывается при создании
    public BookShelfArrayAdapter(Context context, int resource, ArrayList<BookModel> objects) {
        super(context, resource, objects);

        this.context = context;
        this.bookModels = objects;
    }



    // вызывается, когда генерируется список
    public View getView(int position, View convertView, ViewGroup parent) {





        // получаем свойство, которое потом отображаем
        final BookModel bookModel = bookModels.get(position);

        // получаем контекст со страницы полки
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bookshelf_layout, null);
        // получаем необходимые поля
        TextView author = (TextView) view.findViewById(R.id.author_book);
        TextView title = (TextView) view.findViewById(R.id.title_book);
        TextView date = (TextView) view.findViewById(R.id.date_book);
        Button deleteBook=(Button) view.findViewById(R.id.DeleteBook);
        Button openBook=(Button) view.findViewById(R.id.OpenBook);
        ImageView image = (ImageView) view.findViewById(R.id.image);


        //устанавливаем атрибуты
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

        //установка атрибутов
        date.setText("Дата выпуска: " + String.valueOf(bookModel.getDate()));
        // слушатель на удаление книги
        deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //Toast toast = Toast.makeText(context, bookModel.getDel_Url(), Toast.LENGTH_SHORT);
                    //toast.show();
                    String urlDel="";
                    urlDel= bookModel.getDel_Url();
                    RequestGET_delFromPolka RequestGET_delFromPolka=new RequestGET_delFromPolka(context);
                    RequestGET_delFromPolka.execute(urlDel);

            }
        });
        //слушатель на открытие книги
        openBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String REGEX = "(?<=/IdNotice:).*";
                String INPUT = bookModel.getAdd_Url();
                String IdBook = "";
                Pattern p = Pattern.compile(REGEX);
                Matcher m = p.matcher(INPUT);   // get a matcher object
                if (m.find()) {
                    IdBook = m.group(0);
                } else {

                }

                String text="";
                text= bookModel.getAdd_Url();
                // Instantiate the RequestQueue.

                final String finalIdBook = IdBook;
                context.startActivity(new Intent(context, ExemplActivity.class).putExtra("idBook",IdBook).putExtra("NameBook", bookModel.getTitle()).putExtra("AuthorBook", bookModel.getAuthor()).putExtra("TypeBook","").putExtra("DateBook", bookModel.getDate()).putExtra("urlBook", bookModel.getAdd_Url()));


            }
        });


        //получаем и устанавливаем изображение
        int imageID = context.getResources().getIdentifier(bookModel.getImage(), "drawable", context.getPackageName());
        image.setImageResource(imageID);

        return view;
    }
}
