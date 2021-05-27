package com.kembibl.KemBibl.requests;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kembibl.KemBibl.LoginActivity;
import com.kembibl.KemBibl.MainActivity;
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
// Асинхронный класс для авто-авторизации
public class Request_AutoLogin extends AsyncTask<String,String , String>  {

    Context ctx;

    MainActivity mainActiv;
    String reqS = "";
    ArrayList<String> list = new ArrayList<>();

    public Request_AutoLogin(Context context) { ctx=context; this.mainActiv = (MainActivity) ctx; }

    String[] items;
    String[] IdBooks;
    String Search="";
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
        Search=uri[3];
        // Добавляем параметры и их значения для запроса
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("data[User][CodbarU]", uri[1]));
        urlParameters.add(new BasicNameValuePair("data[User][MotPasse]", uri[2]));

        KemBible_DbHelper kemBible_dbHelper = new KemBible_DbHelper(ctx);

        SQLiteDatabase db = kemBible_dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        String idString="1";
        int id = 1;
        values.put(KemBible_data.UserTable._ID, id);
        values.put(KemBible_data.UserTable.COLUMN_LOGIN, uri[1]);
        values.put(KemBible_data.UserTable.COLUMN_PASSWORD, uri[2]);

        long newRowId = db.update(KemBible_data.UserTable.TABLE_NAME, values, KemBible_data.UserTable._ID + "= ?", new String[]{idString});
        // Выводим сообщение в успешном случае или при ошибке
        if (newRowId == -1) {
            // Если ID  -1, значит произошла ошибка

        } else {

        }


        //Устанавливаем параметры запроса в кодировку UTF-8
        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {

            //cook=cookies.get(0).getValue().toString();
            HttpResponse response = httpclient.execute(post);
            Header[] headers = response.getAllHeaders();
            for (Header header: headers) {
                list.add(header.getName());

            }
            CookieStore cookieStore = httpclient.getCookieStore();
            List<Cookie> cookies = cookieStore.getCookies();
            Cookie c= cookies.get(0);
            cook=c.getValue();
            reqS = EntityUtils.toString(response.getEntity());
            System.out.println(reqS);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return cook;

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        int id=1;
        String idString="1";


        if (reqS.equals("")){




            KemBible_DbHelper kemBible_dbHelper = new KemBible_DbHelper(ctx);

            SQLiteDatabase db = kemBible_dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KemBible_data.CookieTable._ID, id);
            values.put(KemBible_data.CookieTable.COLUMN_VALUE, result);


            long newRowId=db.update(KemBible_data.CookieTable.TABLE_NAME, values, KemBible_data.CookieTable._ID + "= ?", new String[]{idString});

            if (Search.equals("Search")){
                //Toast toast = Toast.makeText(ctx, "Search! ", Toast.LENGTH_SHORT);
                //toast.show();
            } else {
                Toast toast = Toast.makeText(ctx, "Вход выполнен! ", Toast.LENGTH_SHORT);
                toast.show();
                startActivity_perc(ctx);
            }


        } else {


            if (list.size()==7){
                if (Search.equals("Search")){
                    //Toast toast = Toast.makeText(ctx, "Search! ", Toast.LENGTH_SHORT);
                    //toast.show();
                } else {
                    Toast toast = Toast.makeText(ctx, "Выполните вход!", Toast.LENGTH_LONG);
                    toast.show();
                    startActivity_login_cab(ctx);
                }

            } else {
                KemBible_DbHelper kemBible_dbHelper = new KemBible_DbHelper(ctx);

                SQLiteDatabase db = kemBible_dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(KemBible_data.CookieTable._ID, id);
                values.put(KemBible_data.CookieTable.COLUMN_VALUE, result);


                long newRowId=db.update(KemBible_data.CookieTable.TABLE_NAME, values, KemBible_data.CookieTable._ID + "= ?", new String[]{idString});

                if (Search.equals("Search")){
                    //Toast toast = Toast.makeText(ctx, "Search! ", Toast.LENGTH_SHORT);
                    //toast.show();
                } else {
                    Toast toast = Toast.makeText(ctx, "Вход выполнен! ", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity_perc(ctx);
                }


            }




        }

    }

    public void startActivity_perc(Context context) {
        Intent intent = new Intent(context, Personal_cabinet_Activity.class);
        context.startActivity(intent);
    }

    public void startActivity_login_cab(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

}

