package com.kembibl.KemBibl.data;

import android.provider.BaseColumns;

public class KemBible_data {
    private KemBible_data(){
    };
// Атрибуты таблиц
    public static final class UserTable implements BaseColumns {
        public final static String TABLE_NAME="user_data";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_LOGIN = "login";
        public final static String COLUMN_PASSWORD = "password";
    }

    public static final class CookieTable implements BaseColumns{
        public final static String TABLE_NAME="cookie_data";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_VALUE = "cookie_value";

    }
}
