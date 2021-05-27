package com.kembibl.KemBibl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kembibl.KemBibl.requests.RequestPOST_login;
import com.kembibl.testbible3.R;
//Класс отвечает за отображение окна авторизации
public class LoginActivity extends AppCompatActivity {

    private Button login;
    private EditText responseText, login_text, password_text;

    String cake_cookie="";
    String login_info="";
    String password_info="";
    String ifLogin="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cab);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Авторизация");

        login=(Button) findViewById(R.id.submit_but);
        login_text=(EditText) findViewById(R.id.login);
        password_text=(EditText) findViewById(R.id.password);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                //this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }

        return super.dispatchTouchEvent(event);
    }


    public void submit_button(View view) {

        login_info=login_text.getText().toString();
        password_info=password_text.getText().toString();
        Toast.makeText(LoginActivity.this, "Выполняется вход... ", Toast.LENGTH_SHORT).show();


        RequestPOST_login requestPOST_login =new RequestPOST_login(LoginActivity.this);
        requestPOST_login.execute("http://catalog.kembibl.ru/users/login", login_info,password_info);




    }




}
