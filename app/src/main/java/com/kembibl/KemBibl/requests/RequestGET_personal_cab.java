package com.kembibl.KemBibl.requests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

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
//Класс для отображения главной страницы личного кабинета
public class RequestGET_personal_cab extends AsyncTask<String, String, String> {

    Context ctx;

    Personal_cabinet_Activity per_cab;
    ArrayList<BookModel> bookModels = new ArrayList<>();
    ArrayList<BookModel> books2 = new ArrayList<>();

    public RequestGET_personal_cab(Context context) { ctx=context; this.per_cab = (Personal_cabinet_Activity) ctx; }

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
       // Toast toast = Toast.makeText(ctx, "cookie: " +result, Toast.LENGTH_LONG);
        //toast.show();

        //form.text();
        if (result==null){
            per_cab.finish();
            Toast.makeText(ctx, "Неверный логин или пароль! ", Toast.LENGTH_LONG).show();
            //Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        }

        Document doc = null;
        String descr="";

        doc = Jsoup.parse(result, "UTF-8");
        //String title = doc.title();
        Elements div= doc.getElementsByClass("user");
        String div_text=div.text();

        String name_reader="";
        String reader_ticket="";

        //String REGEX = "\\(.*?\\)";
        String REGEX = "\\Читательский[\\s\\S]*";
        String INPUT = div_text;
        String REPLACE = "";

        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(INPUT);   // get a matcher object
        if (m.find( )) {
            reader_ticket=m.group(0);
        }else {

        }
        INPUT = m.replaceAll(REPLACE);
        name_reader=INPUT;


        this.per_cab.ViewPersonalInfo(name_reader+"\n\n"+reader_ticket);

        //Парсинг сайта, и вытягивании информации из личного кабинета
        String Image="book_icon_2";
        if (doc.getElementsByClass("liber").isEmpty()){

        } else {
            int Size_liber=doc.getElementsByClass("liber").size();
            Elements tables = doc.getElementsByClass("liber");
            if (Size_liber>1){
                Elements tr= tables.eq(0).select("tr");
                String[] bron_info=new String[tr.size()-1];
                String[] addPolka=new String[tr.size()-1];
                for (int i=0; i<tr.size()-1;i++) {
                    Elements tds=tr.eq(i+1).select("td");

                    String Code=tds.eq(0).text();
                    String Author = tds.eq(1).text();
                    String Name = tds.eq(2).text();
                    addPolka[i]="http://catalog.kembibl.ru"+tds.eq(2).select("a").attr("href");
                    String Date_book = tds.eq(3).text();
                    String Date_finish = tds.eq(4).text();
                    bron_info[i]=">Штрих-код: "+Code+"\n"+">Автор: "+Author+"\n\n"+">Название: "+Name+"\n"+">Год: "+Date_book+"\n"+ ">Бронь кончается: "+Date_finish;
                    if (Author.equals("")){
                        Author="(автор не указан)";
                    }
                    bookModels.add(
                            new BookModel(Author, Name, Date_book, "", Image, "",addPolka[i],"",Date_finish,Code,""));
                }
                this.per_cab.ViewBronInfo(bookModels, bron_info);


                Elements tr2= tables.eq(1).select("tr");
                String[] bron_info2=new String[tr2.size()-1];
                String[] addPolka2=new String[tr2.size()-1];
                for (int i=0; i<tr2.size()-1;i++) {
                    Elements tds2=tr2.eq(i+1).select("td");

                    String Code=tds2.eq(0).text();
                    String Author = tds2.eq(1).text();
                    String Name = tds2.eq(2).text();
                    addPolka2[i]="http://catalog.kembibl.ru"+tds2.eq(2).select("a").attr("href");
                    String Date_book = tds2.eq(3).text();
                    String Date_start = tds2.eq(4).text();
                    String Date_finish = tds2.eq(5).text();
                    String Filial = tds2.eq(6).text();
                    bron_info2[i]=">Штрих-код: "+Code+"\n"+">Автор: "+Author+"\n\n"+">Название: "+Name+"\n"+">Год: "+Date_book+"\n"+ ">Дата выдачи: "+Date_start+"\n"+ ">Сдать до: "+Date_finish+"\n"+ ">Филиал: "+Filial;
                    if (Author.equals("")){
                        Author="(автор не указан)";
                    }
                    books2.add(
                            new BookModel(Author, Name, Date_book, "", Image, "",addPolka2[i],Date_start,Date_finish,Code,Filial));
                }
                this.per_cab.ViewMyDeliv(books2, bron_info2);



            } else{
                Elements th= tables.eq(0).select("th");
                //Toast.makeText(ctx,  "size= "+tr.size(), Toast.LENGTH_LONG).show();
                if (th.size()==5){
                    Elements tr= tables.eq(0).select("tr");
                    String[] bron_info=new String[tr.size()-1];
                    String[] addPolka=new String[tr.size()-1];
                    for (int i=0; i<tr.size()-1;i++) {
                        Elements tds=tr.eq(i+1).select("td");

                        String Code=tds.eq(0).text();
                        String Author = tds.eq(1).text();
                        String Name = tds.eq(2).text();
                        addPolka[i]="http://catalog.kembibl.ru"+tds.eq(2).select("a").attr("href");
                        String Date_book = tds.eq(3).text();
                        String Date_finish = tds.eq(4).text();
                        bron_info[i]=">Штрих-код: "+Code+"\n"+">Автор: "+Author+"\n\n"+">Название: "+Name+"\n"+">Год: "+Date_book+"\n"+ ">Бронь кончается: "+Date_finish;
                        if (Author.equals("")){
                            Author="(автор не указан)";
                        }
                        bookModels.add(
                                new BookModel(Author, Name, Date_book, "", Image, "",addPolka[i],"",Date_finish,Code,""));
                    }
                    this.per_cab.ViewBronInfo(bookModels, bron_info);
                } else {
                    Elements tr= tables.eq(0).select("tr");
                    String[] bron_info=new String[tr.size()-1];
                    String[] addPolka=new String[tr.size()-1];
                    for (int i=0; i<tr.size()-1;i++) {
                        Elements tds=tr.eq(i+1).select("td");

                        String Code=tds.eq(0).text();
                        String Author = tds.eq(1).text();
                        String Name = tds.eq(2).text();
                        addPolka[i]="http://catalog.kembibl.ru"+tds.eq(2).select("a").attr("href");
                        String Date_book = tds.eq(3).text();
                        String Date_start = tds.eq(4).text();
                        String Date_finish = tds.eq(5).text();
                        String Filial = tds.eq(6).text();
                        bron_info[i]=">Штрих-код: "+Code+"\n"+">Автор: "+Author+"\n\n"+">Название: "+Name+"\n"+">Год: "+Date_book+"\n"+ ">Дата выдачи: "+Date_start+"\n"+ ">Сдать до: "+Date_finish+"\n"+ ">Филиал: "+Filial;
                        if (Author.equals("")){
                            Author="(автор не указан)";
                        }
                        bookModels.add(
                                new BookModel(Author, Name, Date_book, "", Image, "",addPolka[i],Date_start,Date_finish,Code,Filial));
                    }
                    this.per_cab.ViewMyDeliv(bookModels,bron_info);
                }
            }

        }

    }
}
