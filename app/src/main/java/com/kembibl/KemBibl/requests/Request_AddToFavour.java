package com.kembibl.KemBibl.requests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kembibl.KemBibl.ExemplActivity;
import com.kembibl.KemBibl.data.KemBible_DbHelper;
import com.kembibl.KemBibl.data.KemBible_data;
import com.kembibl.KemBibl.models.BookModel;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
// Асинхронный класс для отправки запроса для добавления в избранное
public class Request_AddToFavour extends AsyncTask<String, String, String> {
    Context ctx;

    ExemplActivity ae;
    String contentType="";
    String responseString = null;
    String AddSuccess="0";
    ArrayList<BookModel> bookModels = new ArrayList<>();

    public Request_AddToFavour(Context context) { ctx=context; this.ae = (ExemplActivity) ctx; }
    protected void onPreExecute() {
        super.onPreExecute();


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


        //Отправка Post-запроса вместе с cookie
        httpPost.addHeader("Cookie" , "CAKEPHP="+cake_cookie);
        HttpResponse response;
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

    }
// То, что выполняется после Post-запроса
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast toast = Toast.makeText(ctx, "Книга добавлена на полку!", Toast.LENGTH_SHORT);
        toast.show();




    }
}
