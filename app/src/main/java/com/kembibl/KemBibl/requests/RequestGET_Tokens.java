package com.kembibl.KemBibl.requests;

import android.content.Context;
import android.content.Intent;
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

import com.kembibl.KemBibl.ExemplActivity;
import com.kembibl.KemBibl.LoginActivity;
import com.kembibl.KemBibl.data.KemBible_DbHelper;
import com.kembibl.KemBibl.data.KemBible_data;
//Класс для получения токенов, которые необходимы для бронирования книги
public class RequestGET_Tokens extends AsyncTask<String, String, String> {

    Context ctx;

    ExemplActivity ae;
    String IdBook;
    String annexe;
    String empty_cookies="";


    public RequestGET_Tokens(Context context) { ctx=context; this.ae = (ExemplActivity) ctx; }

    @Override
    protected String doInBackground(String... uri) {
        IdBook=uri[1];
        annexe=uri[2];
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


        empty_cookies=cake_cookie;
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

        if (result==null || empty_cookies.equals("")){
            Toast toast = Toast.makeText(ctx, "Войдите в личный кабинет!", Toast.LENGTH_SHORT);
            toast.show();
            startActivity_login(ctx);
        } else {
            Document doc = null;
            String descr="";

            doc = Jsoup.parse(result, "UTF-8");
            Elements inputs=doc.getElementsByTag("input");
            Elements token_key=inputs.eq(1);
            String token_key_value=token_key.attr("value");

            Elements token_fields=inputs.eq(3);
            String token_fields_value=token_fields.attr("value");

            RequestPOST_bron requestPOST_bron=new RequestPOST_bron(ctx);
            requestPOST_bron.execute("http://catalog.kembibl.ru/books/do_reservation/"+IdBook+"/default",token_key_value,annexe,token_fields_value);

        }

    }
    public void startActivity_login(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
