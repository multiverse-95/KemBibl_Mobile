package com.kembibl.KemBibl.requests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.apache.http.Header;
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

import com.kembibl.KemBibl.Personal_cabinet_Activity;
import com.kembibl.KemBibl.data.KemBible_DbHelper;
import com.kembibl.KemBibl.data.KemBible_data;
import com.kembibl.KemBibl.models.BookModel;
//Класс для отображения данных в полке личного кабинета
public class RequestGET_polka extends AsyncTask<String, String, String> {

    Context ctx;

    Personal_cabinet_Activity per_cab;
    String contentType="";
    String responseString = null;
    ArrayList<BookModel> bookModels = new ArrayList<>();

    public RequestGET_polka(Context context) { ctx=context; this.per_cab = (Personal_cabinet_Activity) ctx; }
    protected void onPreExecute() {
        super.onPreExecute();
        this.per_cab.view_progress_bar_polka();

    }

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



        httpPost.addHeader("Cookie" , "CAKEPHP="+cake_cookie);
        HttpResponse response;
        String statusline=null;
        try {
            //response = httpclient.execute(new HttpGet(uri[0]));
            response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            statusline=response.getStatusLine().toString();
            Header[] contentTypeHeader = response.getHeaders("Location");
            contentType = null;
            // Check for null and empty
            if (contentTypeHeader != null && contentTypeHeader.length > 0) {
                contentType = contentTypeHeader[0].getValue();
            }

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
        //return responseString;
        return contentType;
        //return responseString;
        //return cake_cookie;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
         //Toast toast = Toast.makeText(ctx, "cookie: " +result, Toast.LENGTH_LONG);
        //toast.show();

        Document doc = null;
        String descr="";
        String Image="book_icon_2";
        //String del_url="";


        if(contentType==null){
            //Toast toast = Toast.makeText(ctx, "response: " +responseString, Toast.LENGTH_LONG);
            //toast.show();
            doc = Jsoup.parse(responseString, "UTF-8");

        if (doc.getElementsByClass("liber").isEmpty()){
            this.per_cab.ViewPolka(bookModels);
            this.per_cab.close_progress_bar_polka();

        } else {
            Elements tables = doc.getElementsByClass("liber");
            Elements tr= tables.eq(0).select("tr");
            String[] polka_books=new String[tr.size()-1];
            String[] addPolka=new String[tr.size()-1];

            for (int i=0; i<tr.size()-1;i++){
                Elements tds=tr.eq(i+1).select("td");
                String Name=tds.eq(1).text();
                addPolka[i]="http://catalog.kembibl.ru"+tds.eq(1).select("a").attr("href");
                String del_url=tds.eq(5).select("a").attr("href");
                String Author=tds.eq(2).text();
                String Date_book=tds.eq(3).text();
                polka_books[i]=">Автор: "+Author+"\n"+">Название: "+Name+"\n\n"+">Дата выпуска: "+Date_book;
                if (Author.equals("")){
                    Author="(автор не указан)";
                }
                bookModels.add(
                        new BookModel(Author, Name, Date_book, "", Image, "http://catalog.kembibl.ru"+del_url,addPolka[i],"","","",""));
            }

            this.per_cab.ViewPolka(bookModels);
            this.per_cab.close_progress_bar_polka();
        }


        } else {

            RequestGET_polka RequestGET_polka=new RequestGET_polka(ctx);
            RequestGET_polka.execute(contentType);

        }

    }
}
