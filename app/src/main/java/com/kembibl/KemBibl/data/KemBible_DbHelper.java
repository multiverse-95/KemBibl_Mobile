package com.kembibl.KemBibl.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kembibl.KemBibl.data.KemBible_data.UserTable;
import com.kembibl.KemBibl.data.KemBible_data.CookieTable;

public class KemBible_DbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG=KemBible_DbHelper.class.getSimpleName();
    /**
     * Имя файла базы данных
     */

    private static final String DATABASE_NAME="kembible.db";

    /**
     * Версия базы данных. При изменении схемы увеличить на единицу
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Конструктор {@link KemBible_DbHelper}.
     *
     */
    public KemBible_DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    /**
     * Вызывается при создании базы данных
     */



    @Override
    public void onCreate(SQLiteDatabase db) {

        // Строка для создания таблиц
        String SQL_CREATE_USER_TABLE ="CREATE TABLE "+ UserTable.TABLE_NAME+ " ("
                + UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserTable.COLUMN_LOGIN + " TEXT NOT NULL, "
                + UserTable.COLUMN_PASSWORD + " TEXT NOT NULL );";

        String SQL_CREATE_COOKIE_TABLE ="CREATE TABLE "+ CookieTable.TABLE_NAME+ " ("
                + CookieTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CookieTable.COLUMN_VALUE + " TEXT NOT NULL );";


        // Запускаем создание таблиц
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_COOKIE_TABLE);

        // Начальные значения User_Table
        ContentValues values = new ContentValues();
        values.put(UserTable.COLUMN_LOGIN, "");
        values.put(UserTable.COLUMN_PASSWORD, "");

        db.insert(UserTable.TABLE_NAME, null, values);

        // Начальные значения Cookie_Table
        ContentValues values2 = new ContentValues();
        values2.put(CookieTable.COLUMN_VALUE, "");


        db.insert(CookieTable.TABLE_NAME, null, values2);


    }

    /**
     * Вызывается при обновлении схемы базы данных
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w("SQlite","Обновляемся с версии " + oldVersion + " на версию " + newVersion);
        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF EXISTS " +UserTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +CookieTable.TABLE_NAME);
        onCreate(db);
    }
}
