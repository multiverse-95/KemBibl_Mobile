package com.kembibl.KemBibl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kembibl.testbible3.R;
//Класс с логотипом приветствия
public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 1100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}
