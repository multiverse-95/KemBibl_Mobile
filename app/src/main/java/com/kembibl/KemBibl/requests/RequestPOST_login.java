package com.kembibl.KemBibl.requests;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kembibl.KemBibl.LoginActivity;
import com.kembibl.KemBibl.Personal_cabinet_Activity;
import com.kembibl.KemBibl.data.KemBible_DbHelper;
import com.kembibl.KemBibl.data.KemBible_data;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//Класс для реализации входа в личный кабинет
public class RequestPOST_login extends AsyncTask<String,String , String>  {

    Context ctx;

    LoginActivity lc;
    String reqS = "";
    String codeResp="";

    String login="";
    String password="";
    int code_response;
    String[] head;
    String[] head_answer;
    ArrayList<String> list = new ArrayList<>();

    public RequestPOST_login(Context context) { ctx=context; this.lc = (LoginActivity) ctx; }

    String[] items;
    String[] IdBooks;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }



    @Override
    protected String doInBackground(String... uri) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(uri[0]);
        //String reqS = "";
        String cook="";
        // Добавляем параметры и их значения для запроса
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("data[User][CodbarU]", uri[1]));
        urlParameters.add(new BasicNameValuePair("data[User][MotPasse]", uri[2]));
        login=uri[1];
        password=uri[2];


        //Устанавливаем параметры запроса в кодировку UTF-8
        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {


            HttpResponse response = httpclient.execute(post);
            code_response=response.getStatusLine().getStatusCode();
            Header[] headers = response.getAllHeaders();
            for (Header header: headers) {
                list.add(header.getName());
                //header.getName();
                //header.getValue();
            }

            CookieStore cookieStore = httpclient.getCookieStore();
            List<Cookie> cookies = cookieStore.getCookies();
            Cookie c= cookies.get(0);
            cook=c.getValue();
            reqS = EntityUtils.toString(response.getEntity());


        } catch (IOException e) {
            e.printStackTrace();
        }




        return cook;

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);



        int id = 1;
        String idString = "1";

        //Проверка для аутентификации
        if (reqS.equals("")) {
            Toast toast = Toast.makeText(ctx, "Вход выполнен!  ", Toast.LENGTH_LONG);
            toast.show();


            KemBible_DbHelper kemBible_dbHelper = new KemBible_DbHelper(ctx);

            SQLiteDatabase db = kemBible_dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KemBible_data.CookieTable._ID, id);
            values.put(KemBible_data.CookieTable.COLUMN_VALUE, result);


            long newRowId = db.update(KemBible_data.CookieTable.TABLE_NAME, values, KemBible_data.CookieTable._ID + "= ?", new String[]{idString});

            addUserLoginToBase();


            startActivity_perc(ctx);


        } else {


            if (list.size() == 7) {
                Toast toast = Toast.makeText(ctx, "Неверный логин или пароль!", Toast.LENGTH_LONG);
                toast.show();
            } else {
                KemBible_DbHelper kemBible_dbHelper = new KemBible_DbHelper(ctx);

                SQLiteDatabase db = kemBible_dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(KemBible_data.CookieTable._ID, id);
                values.put(KemBible_data.CookieTable.COLUMN_VALUE, result);


                long newRowId = db.update(KemBible_data.CookieTable.TABLE_NAME, values, KemBible_data.CookieTable._ID + "= ?", new String[]{idString});
                addUserLoginToBase();

                Toast toast = Toast.makeText(ctx, "Вход выполнен! ", Toast.LENGTH_LONG);
                toast.show();
                startActivity_perc(ctx);

            }


        }

    }


        public void addUserLoginToBase(){
            KemBible_DbHelper kemBible_dbHelper = new KemBible_DbHelper(ctx);

            SQLiteDatabase db = kemBible_dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            String idString="1";
            int id = 1;
            values.put(KemBible_data.UserTable._ID, id);
            values.put(KemBible_data.UserTable.COLUMN_LOGIN, login);
            values.put(KemBible_data.UserTable.COLUMN_PASSWORD, password);

            long newRowId = db.update(KemBible_data.UserTable.TABLE_NAME, values, KemBible_data.UserTable._ID + "= ?", new String[]{idString});
            // Выводим сообщение в успешном случае или при ошибке
            if (newRowId == -1) {
                // Если ID  -1, значит произошла ошибка

            } else {

            }

        }

        public void startActivity_perc(Context context) {
            Intent intent = new Intent(context, Personal_cabinet_Activity.class);
            context.startActivity(intent);
        }

}

