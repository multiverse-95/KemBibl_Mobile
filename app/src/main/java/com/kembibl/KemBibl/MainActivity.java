package com.kembibl.KemBibl;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kembibl.KemBibl.data.KemBible_DbHelper;
import com.kembibl.KemBibl.data.KemBible_data;
import com.kembibl.KemBibl.requests.Request_AutoLogin;
import com.kembibl.testbible3.R;
// Класс отвечает за главный экран приложения
public class MainActivity extends AppCompatActivity {

    private Button search;

    String cake_cookie="";
    String user_login="";
    String user_password="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("КемБибл: Поиск книг");
        search=(Button) findViewById(R.id.SimpleSearch);

        GetUserdata();

        Boolean hasInternet;
        //Проверка на наличие интернета
        hasInternet=hasConnection(MainActivity.this);
        if (!hasInternet){
            EnableInternetCon();

        }




    }
    //Для менюшки сверху
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainpage, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                //this.finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            case R.id.action_about:
                startActivity(new Intent(getApplicationContext(), AboutAppActivity.class));
                return true;
            case R.id.action_help:
                startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Проверка, есть ли соединение с интернетом
    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }
    //Получение данных пользователя
    public void GetUserdata(){

        KemBible_DbHelper mDbHelper = new KemBible_DbHelper(this);

        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db=mDbHelper.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {
                KemBible_data.UserTable._ID,
                KemBible_data.UserTable.COLUMN_LOGIN,
                KemBible_data.UserTable.COLUMN_PASSWORD };

        // Делаем запрос
        Cursor cursor=db.query(
                KemBible_data.UserTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                "_ID = ?",         // столбцы для условия WHERE
                new String[] {Integer.toString(1)},     // значения для условия WHERE
                null,         // Don't group the rows
                null,          // Don't filter by row groups
                null);        // порядок сортировки



        try {


            // Узнаем индекс каждого столбца

            int idColumnIndex=cursor.getColumnIndex(KemBible_data.UserTable._ID);
            int valueUserLogin = cursor.getColumnIndex(KemBible_data.UserTable.COLUMN_LOGIN);
            int valueUserPassword = cursor.getColumnIndex(KemBible_data.UserTable.COLUMN_PASSWORD);

            // Проходим через все ряды
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                user_login = cursor.getString(valueUserLogin);
                user_password=cursor.getString(valueUserPassword);
                //Toast.makeText(this, cake_cookies[0], Toast.LENGTH_SHORT).show();


            }

        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }



    }

    //При нажатии на кнопку поиск
    public void goSearch(View view) {
        Request_AutoLogin auto_loginRequest =new Request_AutoLogin(this);
        auto_loginRequest.execute("http://catalog.kembibl.ru/users/login", user_login,user_password, "Search");

        startActivity(new Intent(getApplicationContext(),SearchActivity.class));

    }
    //При нажатии на кнопку личный кабинет
    public void my_cab(View view) {

        Toast toast = Toast.makeText(getApplicationContext(), "Вход в личный кабинет...", Toast.LENGTH_SHORT);
        toast.show();

        Request_AutoLogin auto_loginRequest =new Request_AutoLogin(this);
        auto_loginRequest.execute("http://catalog.kembibl.ru/users/login", user_login,user_password,"");

    }



//при нажатии на кнопку список библиотек
    public void bibles(View view) {

        startActivity(new Intent(getApplicationContext(), Kem_bibles_Activity.class));
    }

    @Override
    public void onBackPressed() {
        //onClick();

    }


    public void GetCookieData(){

        KemBible_DbHelper mDbHelper = new KemBible_DbHelper(this);

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
                //Toast.makeText(this, cake_cookies[0], Toast.LENGTH_SHORT).show();


            }

        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }

        Toast.makeText(this, "Cookie: "+cake_cookie, Toast.LENGTH_SHORT).show();

    }
    //Отображение сообщения, что нет интернета
    public void EnableInternetCon() {
        String title = "Нет подключения к интернету";
        String message = "Для работы приложения необходимо включить интернет";
        String button1String = "Ок";
        //String button2String = "Нет";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);  // заголовок
        builder.setMessage(message); // сообщение
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                finish();


            }
        });

        builder.setCancelable(false);
        //setCanceledOnTouchOutside(true);


        builder.show();
    }


}
