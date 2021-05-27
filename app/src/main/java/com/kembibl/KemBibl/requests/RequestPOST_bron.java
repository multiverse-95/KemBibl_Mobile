package com.kembibl.KemBibl.requests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kembibl.KemBibl.ExemplActivity;
import com.kembibl.KemBibl.data.KemBible_DbHelper;
import com.kembibl.KemBibl.data.KemBible_data;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
//Класс для реализации бронирования книги
public class RequestPOST_bron extends AsyncTask<String, String, String> {

    Context ctx;

    ExemplActivity ae;
    String empty_cookie;

    public RequestPOST_bron(Context context) { ctx=context; this.ae = (ExemplActivity) ctx; }

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

        empty_cookie=cake_cookie;
        //Добавление куки к заголовку
        httpPost.addHeader("Cookie" , "CAKEPHP="+cake_cookie);

        HttpResponse response;
        String responseString = null;
        String statusline=null;

        //httpPost.setHeader("Cookie" , "CAKEPHP="+cake_cookie);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("data[_Token][key]", uri[1]));
        urlParameters.add(new BasicNameValuePair("data[Reservation][IdAnnexe]", uri[2]));
        urlParameters.add(new BasicNameValuePair("data[Reservation][IdArmoire]","1"));
        urlParameters.add(new BasicNameValuePair("data[_Token][fields]",uri[3]));


        //Устанавливаем параметры запроса в кодировку UTF-8
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

            Toast toast = Toast.makeText(ctx, "Экземпляр заказан!", Toast.LENGTH_SHORT);
            toast.show();

    }

}
