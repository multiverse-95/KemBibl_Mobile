package com.kembibl.KemBibl.requests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kembibl.KemBibl.Personal_cabinet_Activity;
import com.kembibl.KemBibl.data.KemBible_DbHelper;
import com.kembibl.KemBibl.data.KemBible_data;
import com.kembibl.KemBibl.models.BookModel;
// Класс реализации получения история бронирования
public class RequestGET_history extends AsyncTask<String, String, String> {

    Context ctx;

    Personal_cabinet_Activity per_cab;
    ArrayList<BookModel> bookModels = new ArrayList<>();
    ArrayList<BookModel> books2 = new ArrayList<>();

    public RequestGET_history(Context context) { ctx=context; this.per_cab = (Personal_cabinet_Activity) ctx; }

    protected void onPreExecute() {
        super.onPreExecute();
        this.per_cab.view_progress_bar_history();

    }
// выполнение запросы к сайту
    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(uri[0]);
        String cake_cookie="";

        KemBible_DbHelper mDbHelper = new KemBible_DbHelper(ctx);

        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db=mDbHelper.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {
                KemBible_data.CookieTable._ID,
                KemBible_data.CookieTable.COLUMN_VALUE };

        // Делаем запрос
        Cursor cursor=db.query(
                KemBible_data.CookieTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                "_ID = ?",         // столбцы для условия WHERE
                new String[] {Integer.toString(1)},     // значения для условия WHERE
                null,         // Don't group the rows
                null,          // Don't filter by row groups
                null);        // порядок сортировки



        try {


            // Узнаем индекс каждого столбца

            int idColumnIndex=cursor.getColumnIndex(KemBible_data.CookieTable._ID);
            int valueCookieColumnIndex = cursor.getColumnIndex(KemBible_data.CookieTable.COLUMN_VALUE);

            // Проходим через все ряды
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                cake_cookie = cursor.getString(valueCookieColumnIndex);


            }

        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }


        //Добавление куки к заголовку
        httpPost.addHeader("Cookie" , "CAKEPHP="+cake_cookie);
        HttpResponse response;
        String responseString = null;
        String statusline=null;
        try {
            //response = httpclient.execute(new HttpGet(uri[0]));
            response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            statusline=response.getStatusLine().toString();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
        //return responseString;
        //return cake_cookie;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);


        //Парсинг сайта
        Document doc = null;
        String descr="";
        doc = Jsoup.parse(result, "UTF-8");

        String Image="";


        //Поиск элемента по классу, вытягивание данных из таблиц
        if (doc.getElementsByClass("liber").isEmpty()) {
            this.per_cab.close_progress_bar_history();

        } else {
            int Size_liber=doc.getElementsByClass("liber").size();
            if (Size_liber>1){
                Elements tables = doc.getElementsByClass("liber");
                Elements tr= tables.eq(1).select("tr");

                //Elements tr= doc.getElementsByClass("altrow");
                String[] history_books=new String[tr.size()-1];
                String[] addPolka=new String[tr.size()-1];
                for (int i=0; i<tr.size()-1;i++) {
                    Elements tds=tr.eq(i+1).select("td");
                    //Elements td = tr.eq(i);
                    String Author = tds.eq(0).text();
                    String Name = tds.eq(1).text();
                    addPolka[i]="http://catalog.kembibl.ru"+tds.eq(1).select("a").attr("href");
                    String Date_book = tds.eq(2).text();
                    String Date_start = tds.eq(3).text();
                    String Date_finish = tds.eq(4).text();
                    String Type_book = tds.eq(6).text();
                    history_books[i]=">Автор: "+Author+"\n"+">Название: "+Name+"\n\n"+">Дата выпуска: "+Date_book+"\n"+">Дата выдачи: "+Date_start+"\n"+ ">Дата окончания: "+Date_finish+"\n"+">Тип издания: "+Type_book;
                    if(Type_book.equals("Выпуск") || Type_book.equals("Статья")){
                        Image="newspaper_icon_2";
                    } else if (Type_book.equals("Электронный ресурс ")){
                        Image="disc_icon_3";
                    } else if (Type_book.equals("Видеозапись")){
                        Image="video_icon";
                    } else if (Type_book.equals("Звукозапись")){
                        Image="sound_icon";
                    } else {
                        Image="book_icon_2";
                    }
                    if (Author.equals("")){
                        Author="(автор не указан)";
                    }
                    bookModels.add(
                            new BookModel(Author, Name, Date_book, Type_book, Image, "","",Date_start,Date_finish,"",""));
                }
                this.per_cab.ViewHistory(bookModels,history_books,addPolka);

                //bookModels.clear();

                Elements tr2= tables.eq(0).select("tr");

                //Elements tr= doc.getElementsByClass("altrow");
                String[] history_books2=new String[tr2.size()-1];
                String[] addPolka2=new String[tr2.size()-1];
                for (int i=0; i<tr2.size()-1;i++) {
                    Elements tds=tr2.eq(i+1).select("td");
                    //Elements td = tr.eq(i);
                    String Author = tds.eq(0).text();
                    String Name = tds.eq(1).text();
                    addPolka2[i]="http://catalog.kembibl.ru"+tds.eq(1).select("a").attr("href");
                    String Date_book = tds.eq(2).text();
                    String Date_start = tds.eq(3).text();
                    String Date_finish = tds.eq(4).text();
                    String Type_book = tds.eq(6).text();
                    history_books2[i]=">Автор: "+Author+"\n"+">Название: "+Name+"\n\n"+">Дата выпуска: "+Date_book+"\n"+">Дата выдачи: "+Date_start+"\n"+ ">Дата окончания: "+Date_finish+"\n"+">Тип издания: "+Type_book;
                    if(Type_book.equals("Выпуск") || Type_book.equals("Статья")){
                        Image="newspaper_icon_2";
                    } else if (Type_book.equals("Электронный ресурс ")){
                        Image="disc_icon_3";
                    } else if (Type_book.equals("Видеозапись")){
                        Image="video_icon";
                    } else if (Type_book.equals("Звукозапись")){
                        Image="sound_icon";
                    } else {
                        Image="book_icon_2";
                    }
                    if (Author.equals("")){
                        Author="(автор не указан)";
                    }
                    books2.add(
                            new BookModel(Author, Name, Date_book, Type_book, Image, "","",Date_start,Date_finish,"",""));
                }
                this.per_cab.ViewGettingBooks(books2,history_books2,addPolka2);


                this.per_cab.close_progress_bar_history();
            } else {
                Elements userCard = doc.getElementsByClass("userCard index");
                String REGEX="(У вас нет ранее выданных книг)";
                String INPUT=userCard.first().text();
                String notGettingBook="";
                Pattern p = Pattern.compile(REGEX);
                Matcher m = p.matcher(INPUT);   // get a matcher object
                if (m.find( )) {
                    notGettingBook=m.group(0);
                }else {

                }
                //Toast toast = Toast.makeText(ctx, " " +notGettingBook, Toast.LENGTH_LONG);
                //toast.show();
                if (notGettingBook.equals("")){
                    Elements tables = doc.getElementsByClass("liber");
                    Elements tr= tables.eq(0).select("tr");

                    //Elements tr= doc.getElementsByClass("altrow");
                    String[] history_books=new String[tr.size()-1];
                    String[] addPolka=new String[tr.size()-1];
                    for (int i=0; i<tr.size()-1;i++) {
                        Elements tds=tr.eq(i+1).select("td");
                        //Elements td = tr.eq(i);
                        String Author = tds.eq(0).text();
                        String Name = tds.eq(1).text();
                        addPolka[i]="http://catalog.kembibl.ru"+tds.eq(1).select("a").attr("href");
                        String Date_book = tds.eq(2).text();
                        String Date_start = tds.eq(3).text();
                        String Date_finish = tds.eq(4).text();
                        String Type_book = tds.eq(6).text();
                        history_books[i]=">Автор: "+Author+"\n"+">Название: "+Name+"\n\n"+">Дата выпуска: "+Date_book+"\n"+">Дата выдачи: "+Date_start+"\n"+ ">Дата окончания: "+Date_finish+"\n"+">Тип издания: "+Type_book;
                        if(Type_book.equals("Выпуск") || Type_book.equals("Статья")){
                            Image="newspaper_icon_2";
                        } else if (Type_book.equals("Электронный ресурс ")){
                            Image="disc_icon_3";
                        } else if (Type_book.equals("Видеозапись")){
                            Image="video_icon";
                        } else if (Type_book.equals("Звукозапись")){
                            Image="sound_icon";
                        } else {
                            Image="book_icon_2";
                        }
                        if (Author.equals("")){
                            Author="(автор не указан)";
                        }
                        bookModels.add(
                                new BookModel(Author, Name, Date_book, Type_book, Image, "","",Date_start,Date_finish,"",""));
                    }
                    this.per_cab.ViewGettingBooks(bookModels,history_books,addPolka);
                    this.per_cab.close_progress_bar_history();
                } else {
                    Elements tables = doc.getElementsByClass("liber");
                    Elements tr= tables.eq(0).select("tr");

                    //Elements tr= doc.getElementsByClass("altrow");
                    String[] history_books=new String[tr.size()-1];
                    String[] addPolka=new String[tr.size()-1];
                    for (int i=0; i<tr.size()-1;i++) {
                        Elements tds=tr.eq(i+1).select("td");
                        //Elements td = tr.eq(i);
                        String Author = tds.eq(0).text();
                        String Name = tds.eq(1).text();
                        addPolka[i]="http://catalog.kembibl.ru"+tds.eq(1).select("a").attr("href");
                        String Date_book = tds.eq(2).text();
                        String Date_start = tds.eq(3).text();
                        String Date_finish = tds.eq(4).text();
                        String Type_book = tds.eq(6).text();
                        history_books[i]=">Автор: "+Author+"\n"+">Название: "+Name+"\n\n"+">Дата выпуска: "+Date_book+"\n"+">Дата выдачи: "+Date_start+"\n"+ ">Дата окончания: "+Date_finish+"\n"+">Тип издания: "+Type_book;
                        if(Type_book.equals("Выпуск") || Type_book.equals("Статья")){
                            Image="newspaper_icon_2";
                        } else if (Type_book.equals("Электронный ресурс ")){
                            Image="disc_icon_3";
                        } else if (Type_book.equals("Видеозапись")){
                            Image="video_icon";
                        } else if (Type_book.equals("Звукозапись")){
                            Image="sound_icon";
                        } else {
                            Image="book_icon_2";
                        }
                        if (Author.equals("")){
                            Author="(автор не указан)";
                        }
                        bookModels.add(
                                new BookModel(Author, Name, Date_book, Type_book, Image, "","",Date_start,Date_finish,"",""));
                    }
                    this.per_cab.ViewHistory(bookModels, history_books,addPolka);
                    this.per_cab.close_progress_bar_history();
                }


            }

        }

    }
}
