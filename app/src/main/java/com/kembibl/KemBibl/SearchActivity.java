package com.kembibl.KemBibl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kembibl.KemBibl.adapters.AutoSuggestAdapter;
import com.kembibl.KemBibl.adapters.BookArrayAdapter;
import com.kembibl.KemBibl.models.BookModel;
import com.kembibl.KemBibl.requests.RequestGET_autocomplete;
import com.kembibl.KemBibl.requests.RequestPOST_Search;
import com.kembibl.testbible3.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Класс отвечает за отображение и логику поиска книг
public class SearchActivity extends AppCompatActivity {

    //private EditText title;
    int flag=0;
    ListView myBooks;
    LinearLayout linearLayout;
    LinearLayout LayoutAuthor, LayoutButton,LayoutTitle, LayoutGenre, LayoutYears, LayoutTypeBook;
    LinearLayout LayoutOneSearch;
    LinearLayout LayoutOnlyFulltext;
    CheckBox OnlyFullText;
    //private EditText author;

    FloatingActionButton fab;
    String Title="";
    //String[] items;
    String Author="";
    Button SearchBooks,OneSearchBooksBut;
    //EditText line;
    String Line="";
    TextView hint;

    EditText date_start_ed, date_finish_ed;
    String genre_book="";
    String date_start_book="";
    String date_finish_book="";
    String type_of_book_all="";
    AutoCompleteTextView title, author,genre_book_ed,line;

    ProgressBar progressBarEdit0, progressBarEdit, progressBarEdit2, progressBarEdit3;

    ArrayList<String> genres_all=new ArrayList<String>();

    String type_of_book1="", type_of_book2="",type_of_book3="",type_of_book4="",type_of_book5="",type_of_book6="",type_of_book7="",type_of_book8="",type_of_book9="",type_of_book10="",type_of_book11="",
    type_of_book12="",type_of_book13="",type_of_book14="",type_of_book15="",type_of_book16="",type_of_book17="";
    boolean flagg=false;

    String[] type_of_documents = {"Все","Книга","Периодическое издание","Выпуск","Статья","Книга (аналит. описание)","Многотомник","Смешанный комплект","Ноты","Ноты (аналит. описание)",
            "Видеозапись","Видеозапись (аналит. описание)","Звукозапись","Звукозапись (аналит. описание)","Электронный ресурс","Электронный ресурс (аналит. описание)","Раздаточный материал","Изоматериалы"};

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler0, handler, handler2, handler3;
    //private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter0, autoSuggestAdapter, autoSuggestAdapter2, autoSuggestAdapter3;



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Поиск литературы");
        myBooks=(ListView)findViewById(R.id.myBooks);
        //title =findViewById(R.id.title);
        title=(AutoCompleteTextView)findViewById(R.id.title);
        author=(AutoCompleteTextView)findViewById(R.id.author);
        View view;
        linearLayout=(LinearLayout)findViewById(R.id.indicator);
        LayoutTitle=(LinearLayout)findViewById(R.id.layout_title);
        LayoutAuthor=(LinearLayout)findViewById(R.id.layout_author);
        LayoutGenre=(LinearLayout)findViewById(R.id.layout_genre);
        LayoutYears=(LinearLayout) findViewById(R.id.layout_years);
        LayoutTypeBook=(LinearLayout)findViewById(R.id.layout_type_book);


        LayoutOneSearch=(LinearLayout) findViewById(R.id.layout_OneSearch);
        LayoutOnlyFulltext=(LinearLayout)findViewById(R.id.layout_OnlyFullText);

        LayoutButton=(LinearLayout)findViewById(R.id.layout_button);

        SearchBooks=(Button)findViewById(R.id.SimpleSearch);
        OneSearchBooksBut=(Button)findViewById(R.id.OneSearchButton);
        line=(AutoCompleteTextView) findViewById(R.id.one_search_text);
        OnlyFullText=(CheckBox) findViewById(R.id.OnlyFullText);

        hint=(TextView)findViewById(R.id.short_description);

        hint.setText("Введите автора и/или название книги в кавычках. Например: Уэллс Герберт \"Машина времени\"\n\nУстановите флажок \"Только полный текст\", чтобы найти книги, которые можно скачать на устройство.");

        genre_book_ed=(AutoCompleteTextView) findViewById(R.id.genre);
        date_start_ed=(EditText)findViewById(R.id.year_start);
        date_finish_ed=(EditText)findViewById(R.id.year_finish);

        //mAutoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.author);
        progressBarEdit0=(ProgressBar)findViewById(R.id.progress_bar_edit0);
        progressBarEdit=(ProgressBar)findViewById(R.id.progress_bar_edit);
        progressBarEdit2=(ProgressBar)findViewById(R.id.progress_bar_edit2);
        progressBarEdit3=(ProgressBar)findViewById(R.id.progress_bar_edit3);



        //Слушатель на кнопку поиска
        SearchBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goSearchBooks();
                if ((LayoutTitle.getVisibility() == View.VISIBLE) && (LayoutAuthor.getVisibility() == View.GONE) ) {
                    // действия, когда виджет видим
                    goSearchBooksTitle();

                } else if ((LayoutTitle.getVisibility() == View.GONE) && (LayoutAuthor.getVisibility() == View.VISIBLE)) {
                    // действия, когда невидим.
                    goSearchBooksAuthor();
                } else if((LayoutTitle.getVisibility() == View.VISIBLE) && (LayoutAuthor.getVisibility() == View.VISIBLE)){
                    goSearchBooksSimple();
                } else if (LayoutOneSearch.getVisibility()==View.VISIBLE){
                    goSearchBooksOneSearch();
                }

            }
        });

        OneSearchBooksBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goSearchBooksOneSearch();
            }
        });
        //Плавающая кнопка со стрелкой вверх
        fab=(FloatingActionButton) findViewById(R.id.fab1);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // myBooks.smoothScrollToPosition(0);
                myBooks.setSelection(0);
            }
        });

        myBooks.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem < 2) {
                    fab.setVisibility(View.GONE);


                }else {
                    fab.setVisibility(View.VISIBLE);


                }


            }
        });

        setBookType();
        autoSuggestAdapter0 = new AutoSuggestAdapter(this,
                android.R.layout.simple_list_item_1);
        title.setThreshold(1);
        title.setAdapter(autoSuggestAdapter0);


        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_list_item_1);
        author.setThreshold(1);
        author.setAdapter(autoSuggestAdapter);

        autoSuggestAdapter2 = new AutoSuggestAdapter(this,
               android.R.layout.simple_list_item_1);
        genre_book_ed.setThreshold(1);
        genre_book_ed.setAdapter(autoSuggestAdapter2);

        autoSuggestAdapter3 = new AutoSuggestAdapter(this,
                android.R.layout.simple_list_item_1);
        line.setThreshold(1);
        line.setAdapter(autoSuggestAdapter3);

        //Добавление всплывающих подсказок
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler0.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler0.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        author.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        genre_book_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler2.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler2.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });




        line.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler3.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler3.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //Если старая версия андроид (до 6 андроида), то подсказки работают по другому принципу
        if (Build.VERSION.SDK_INT<21){
            handler0 = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == TRIGGER_AUTO_COMPLETE) {
                        if (!TextUtils.isEmpty(title.getText())) {
                            //openBar0();
                            //goAutoCompleteTitle(title.getText().toString());
                        } else {
                            //openBar();
                            //goAutoCompleteTitle(title.getText().toString());
                        }
                    }
                    return false;
                }
            });

            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == TRIGGER_AUTO_COMPLETE) {
                        if (!TextUtils.isEmpty(author.getText())) {
                            openBar();
                            goAutoCompleteAuthor(author.getText().toString());
                        } else {
                            //openBar();
                            //goAutoCompleteAuthor(author.getText().toString());
                        }
                    }
                    return false;
                }
            });

            handler2 = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == TRIGGER_AUTO_COMPLETE) {
                        if (!TextUtils.isEmpty(genre_book_ed.getText())) {
                            openBar2();
                            goAutoCompleteGenre(genre_book_ed.getText().toString());

                        } else {
                            //openBar2();
                            //goAutoCompleteGenre(genre_book_ed.getText().toString());
                        }
                    }
                    return false;
                }
            });

            handler3 = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == TRIGGER_AUTO_COMPLETE) {
                        if (!TextUtils.isEmpty(line.getText())) {
                            openBar3();
                            goAutoCompleteOneLine(line.getText().toString());
                        } else {
                            //openBar3();
                            //goAutoCompleteOneLine(line.getText().toString());
                        }
                    }
                    return false;
                }
            });


        } else {
            handler0 = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == TRIGGER_AUTO_COMPLETE) {
                        if (!TextUtils.isEmpty(title.getText())) {

                        } //else {
                            //openBar();
                            //makeApiCallTitle(title.getText().toString());
                        //}
                    }
                    return false;
                }
            });

            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == TRIGGER_AUTO_COMPLETE) {
                        if (!TextUtils.isEmpty(author.getText())) {
                            openBar();
                            makeApiCallAuthor(author.getText().toString());
                        } else {
                            //openBar();
                            //goAutoCompleteAuthor(author.getText().toString());
                        }
                    }
                    return false;
                }
            });

            handler2 = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == TRIGGER_AUTO_COMPLETE) {
                        if (!TextUtils.isEmpty(genre_book_ed.getText())) {
                            openBar2();
                            makeApiCallGenre(genre_book_ed.getText().toString());
                            //goAutoCompleteGenre(genre_book_ed.getText().toString());

                        } else {
                            //openBar2();
                            //goAutoCompleteGenre(genre_book_ed.getText().toString());
                        }
                    }
                    return false;
                }
            });

            handler3 = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == TRIGGER_AUTO_COMPLETE) {
                        if (!TextUtils.isEmpty(line.getText())) {
                            openBar3();
                            makeApiCallOneLine(line.getText().toString());
                        } else {
                            //openBar3();
                            //goAutoCompleteOneLine(line.getText().toString());
                        }
                    }
                    return false;
                }
            });


        }



    }
    //Отправка запросов для подсказок(автор)
    public void goAutoCompleteAuthor(String Line){
        // Прописываем то, что надо выполнить после изменения текста

        String typeComplete="author";

        Line=Line.replaceAll("[^A-Za-zА-Яа-я0-9]", "+");

        RequestGET_autocomplete request_GET_autocomplete =new RequestGET_autocomplete(SearchActivity.this);
        request_GET_autocomplete.execute("http://catalog.kembibl.ru/SearchForms/autocomplete/source:default?q="+Line+"&limit=10&Field=200_F",typeComplete);


    }
    //Отправка запросов для подсказок(жанр)
    public void goAutoCompleteGenre(String Line){
        // Прописываем то, что надо выполнить после изменения текста

        String typeComplete="genre";
        Line=Line.replaceAll("[^A-Za-zА-Яа-я0-9]", "+");
        RequestGET_autocomplete request_GET_autocomplete =new RequestGET_autocomplete(SearchActivity.this);
        request_GET_autocomplete.execute("http://catalog.kembibl.ru/SearchForms/autocomplete/source:default?q="+Line+"&limit=10&Field=SUJET",typeComplete);


    }
    //Отправка запросов для подсказок(одна строка)
    public void goAutoCompleteOneLine(String Line){
        // Прописываем то, что надо выполнить после изменения текста

        String typeComplete="oneline";

        Line=Line.replaceAll("[^A-Za-zА-Яа-я0-9]", "+");

        RequestGET_autocomplete request_GET_autocomplete =new RequestGET_autocomplete(SearchActivity.this);
        request_GET_autocomplete.execute("http://catalog.kembibl.ru/SearchForms/autocomplete/source:default?q="+Line+"&limit=10&Field=200_F",typeComplete);


    }



    public void setMult(List<String> mlistData,String typeSearch){
        if(typeSearch.equals("author")){

            autoSuggestAdapter.setData(mlistData);
            autoSuggestAdapter.notifyDataSetChanged();
        } else if(typeSearch.equals("genre")){

            autoSuggestAdapter2.setData(mlistData);
            autoSuggestAdapter2.notifyDataSetChanged();
        } else {

            autoSuggestAdapter3.setData(mlistData);
            autoSuggestAdapter3.notifyDataSetChanged();
        }


    }

    public void setMult2(List<String> mlistData ){
        autoSuggestAdapter0.setData(mlistData);
        autoSuggestAdapter0.notifyDataSetChanged();
        closeBar0();

    }
    //Отображение подсказок при вводе навзания(используется гугл апи, вырезано, т.к. у гугла ограничение на кол-во вызовов)
    private void makeApiCallTitle(String text) {
        text=text.replaceAll("[^A-Za-zА-Яа-я0-9]", "+");

        text="https://www.googleapis.com/books/v1/volumes?q="+text+"&key=ApiKey";

        ApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //parsing logic, please change it as per your requirement

                List<String> stringList = new ArrayList<>();
                String firstName="";
                JSONObject jsonObject= null;
                JSONArray lang=null;
                try {
                    jsonObject = new JSONObject(response);
                    //JSONObject responseObject = new JSONObject(response);
                    lang= (JSONArray) jsonObject.get("items");
                    //JSONArray array = responseObject.getJSONArray("results");
                    for(int i=0; i<10; i++){
                        firstName=lang.getJSONObject(i).getJSONObject("volumeInfo").getString("title");
                        stringList.add(firstName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //IMPORTANT: set data here and notify
                autoSuggestAdapter0.setData(stringList);
                autoSuggestAdapter0.notifyDataSetChanged();
                closeBar0();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });


    }
    //Отправка запросов для подсказок(автор) - для новых версий андроида с 7 версии
    private void makeApiCallAuthor(String text) {

        text=text.replaceAll("[^A-Za-zА-Яа-я0-9]", "+");
        text="http://catalog.kembibl.ru/SearchForms/autocomplete/source:default?q="+text+"&limit=10&Field=200_F";
        // Instantiate the RequestQueue.

        ApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                JSONArray jsonArray=null;
                try {
                    jsonArray=new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++){
                        stringList.add(jsonArray.getJSONObject(i).getString("result"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
                closeBar();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

    }
    //Отправка запросов для подсказок(жанр) - для новых версий андроида с 7 версии
    private void makeApiCallGenre(String text) {

        text=text.replaceAll("[^A-Za-zА-Яа-я0-9]", "+");
        text="http://catalog.kembibl.ru/SearchForms/autocomplete/source:default?q="+text+"&limit=10&Field=SUJET";
        // Instantiate the RequestQueue.

        ApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                JSONArray jsonArray=null;
                try {
                    jsonArray=new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++){
                        stringList.add(jsonArray.getJSONObject(i).getString("result"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter2.setData(stringList);
                autoSuggestAdapter2.notifyDataSetChanged();
                closeBar2();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

    }


    //Отправка запросов для подсказок(одна строка) - для новых версий андроида с 7 версии
    private void makeApiCallOneLine(String text) {
        //убирание лишних символов с помощью регулярок
        text=text.replaceAll("[^A-Za-zА-Яа-я0-9]", "+");
        text="http://catalog.kembibl.ru/SearchForms/autocomplete/source:default?q="+text+"&limit=10&Field=200_F";
        // Instantiate the RequestQueue.

        ApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                JSONArray jsonArray=null;
                try {
                    jsonArray=new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++){
                        stringList.add(jsonArray.getJSONObject(i).getString("result"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter3.setData(stringList);
                autoSuggestAdapter3.notifyDataSetChanged();
                closeBar3();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

    }



    //Закрытие и открытие бара
    public void closeBar0(){
        progressBarEdit0.setVisibility(View.GONE);

    }
    public void openBar0(){
        progressBarEdit0.setVisibility(View.VISIBLE);

    }

    public void closeBar(){
        progressBarEdit.setVisibility(View.GONE);

    }
    public void openBar(){
        progressBarEdit.setVisibility(View.VISIBLE);

    }

    public void closeBar2(){
        progressBarEdit2.setVisibility(View.GONE);

    }
    public void openBar2(){
        progressBarEdit2.setVisibility(View.VISIBLE);

    }

    public void closeBar3(){
        progressBarEdit3.setVisibility(View.GONE);

    }
    public void openBar3(){
        progressBarEdit3.setVisibility(View.VISIBLE);

    }
    //Логика для фильтра документов по типам
    public void setBookType(){
        Spinner spinner = (Spinner) findViewById(R.id.type_document);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type_of_documents);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                switch (position){
                    case 0:
                        type_of_book_all="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 1:
                        type_of_book1="1";
                        type_of_book_all=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 2:
                        type_of_book2="1";
                        type_of_book1=""; type_of_book_all="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 3:
                        type_of_book3="1";
                        type_of_book1=""; type_of_book2="";type_of_book_all="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 4:
                        type_of_book4="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book_all="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 5:
                        type_of_book5="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book_all="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 6:
                        type_of_book6="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book_all=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 7:
                        type_of_book7="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book_all="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 8:
                        type_of_book8="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book_all="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 9:
                        type_of_book9="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book_all="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 10:
                        type_of_book10="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book_all="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 11:
                        type_of_book11="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book_all="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 12:
                        type_of_book12="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book_all="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 13:
                        type_of_book13="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book_all="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 14:
                        type_of_book14="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book_all="";type_of_book15="";type_of_book16="";type_of_book17="";
                        break;
                    case 15:
                        type_of_book15="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book_all="";type_of_book16="";type_of_book17="";
                        break;
                    case 16:
                        type_of_book16="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book_all="";type_of_book17="";
                        break;
                    case 17:
                        type_of_book17="1";
                        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book_all="";
                        break;

                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

    }



//Логика для меню сверху
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.action_but_search:
                //finish();
                //startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                LayoutOneSearch.setVisibility(View.VISIBLE);
                hint.setVisibility(View.GONE);
                LayoutGenre.setVisibility(View.GONE);
                LayoutYears.setVisibility(View.GONE);
                LayoutTypeBook.setVisibility(View.GONE);
                LayoutAuthor.setVisibility(View.GONE);
                LayoutTitle.setVisibility(View.GONE);
                LayoutButton.setVisibility(View.VISIBLE);
                LayoutOnlyFulltext.setVisibility(View.VISIBLE);
                //ClearList();

                return true;
            case R.id.action_TitleSearch:
                LayoutOneSearch.setVisibility(View.GONE);
                LayoutAuthor.setVisibility(View.GONE);
                LayoutGenre.setVisibility(View.GONE);
                LayoutYears.setVisibility(View.GONE);
                LayoutTypeBook.setVisibility(View.GONE);
                LayoutTitle.setVisibility(View.VISIBLE);
                LayoutButton.setVisibility(View.VISIBLE);
                LayoutOnlyFulltext.setVisibility(View.VISIBLE);

                type_of_book_all="1";
                type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                //ClearList();
                return true;

            case R.id.action_AuthorSearch:
                LayoutOneSearch.setVisibility(View.GONE);
                LayoutAuthor.setVisibility(View.VISIBLE);
                LayoutGenre.setVisibility(View.GONE);
                LayoutYears.setVisibility(View.GONE);
                LayoutTypeBook.setVisibility(View.GONE);
                LayoutTitle.setVisibility(View.GONE);
                LayoutButton.setVisibility(View.VISIBLE);
                LayoutOnlyFulltext.setVisibility(View.VISIBLE);

                closeBar();

                type_of_book_all="1";
                type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                //ClearList();
                return true;
            case R.id.action_SimpleSearch:
                LayoutOneSearch.setVisibility(View.GONE);
                LayoutGenre.setVisibility(View.VISIBLE);
                LayoutYears.setVisibility(View.VISIBLE);
                LayoutTypeBook.setVisibility(View.VISIBLE);
                LayoutAuthor.setVisibility(View.VISIBLE);
                LayoutTitle.setVisibility(View.VISIBLE);
                LayoutButton.setVisibility(View.VISIBLE);
                LayoutOnlyFulltext.setVisibility(View.VISIBLE);
                closeBar2();
                closeBar();

                ClearList();
                setBookType();


                return true;
            case R.id.action_OneSearch:
                LayoutOneSearch.setVisibility(View.VISIBLE);
                hint.setVisibility(View.GONE);
                LayoutGenre.setVisibility(View.GONE);
                LayoutYears.setVisibility(View.GONE);
                LayoutTypeBook.setVisibility(View.GONE);
                LayoutAuthor.setVisibility(View.GONE);
                LayoutTitle.setVisibility(View.GONE);
                LayoutButton.setVisibility(View.VISIBLE);
                LayoutOnlyFulltext.setVisibility(View.VISIBLE);
                closeBar();



                type_of_book_all="1";
                type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
                type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";
                return true;
            case android.R.id.home:

                this.finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    //при нажатии на пустое место экрана клавиатура пропадает
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }

        return super.dispatchTouchEvent(event);
    }

    //Поиск книг по названию
    public void goSearchBooksTitle() {

        Title=title.getText().toString();
        String IfFullText="0";

        if (OnlyFullText.isChecked()){
            IfFullText="1";
        } else {
            IfFullText="0";
        }


        Author="";
        genre_book="";
        date_start_book="";
        date_finish_book="";
        type_of_book_all="1";
        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";




        if (title.getText().length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "Введите данные", Toast.LENGTH_SHORT);
            toast.show();

        }
        else {

            RequestPOST_Search requestPOSTSearch =new RequestPOST_Search(SearchActivity.this);
            requestPOSTSearch.execute("http://catalog.kembibl.ru/SearchForms/simpleSearch", Title, Author,genre_book,date_start_book,date_finish_book, IfFullText, type_of_book_all,type_of_book1,type_of_book2,type_of_book3,
                    type_of_book4,type_of_book5,type_of_book6,type_of_book7,type_of_book8,type_of_book9,type_of_book10,type_of_book11,type_of_book12,type_of_book13,type_of_book14,type_of_book15,type_of_book16,type_of_book17);


            LayoutGenre.setVisibility(View.GONE);
            LayoutYears.setVisibility(View.GONE);
            LayoutTypeBook.setVisibility(View.GONE);
            LayoutAuthor.setVisibility(View.GONE);
            LayoutTitle.setVisibility(View.GONE);
            LayoutButton.setVisibility(View.GONE);
            LayoutOnlyFulltext.setVisibility(View.GONE);
        }

        Title="";


    }
    //Поиск книг по автору
    public void goSearchBooksAuthor() {

        //Title=title.getText().toString();
        Author=author.getText().toString();


        String IfFullText="0";

        if (OnlyFullText.isChecked()){
            IfFullText="1";
        } else {
            IfFullText="0";
        }

        Title="";
        genre_book="";
        date_start_book="";
        date_finish_book="";
        type_of_book_all="1";
        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";



        if (author.getText().length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "Введите данные", Toast.LENGTH_SHORT);
            toast.show();

        }
        else {

            RequestPOST_Search requestPOSTSearch =new RequestPOST_Search(SearchActivity.this);
            requestPOSTSearch.execute("http://catalog.kembibl.ru/SearchForms/simpleSearch", Title, Author,genre_book,date_start_book,date_finish_book, IfFullText, type_of_book_all,type_of_book1,type_of_book2,type_of_book3,
                    type_of_book4,type_of_book5,type_of_book6,type_of_book7,type_of_book8,type_of_book9,type_of_book10,type_of_book11,type_of_book12,type_of_book13,type_of_book14,type_of_book15,type_of_book16,type_of_book17);


            LayoutGenre.setVisibility(View.GONE);
            LayoutYears.setVisibility(View.GONE);
            LayoutTypeBook.setVisibility(View.GONE);
            LayoutAuthor.setVisibility(View.GONE);
            LayoutTitle.setVisibility(View.GONE);
            LayoutButton.setVisibility(View.GONE);
            LayoutOnlyFulltext.setVisibility(View.GONE);
        }

        Author="";


    }
    //Поиск - точный поиск
    public void goSearchBooksSimple() {

        Title=title.getText().toString();
        Author=author.getText().toString();
        genre_book=genre_book_ed.getText().toString();
        date_start_book=date_start_ed.getText().toString();
        date_finish_book=date_finish_ed.getText().toString();
        String IfFullText="0";

        if (OnlyFullText.isChecked()){
            IfFullText="1";
        } else {
            IfFullText="0";
        }

        if (title.getText().length()==0 && author.getText().length()==0 && genre_book_ed.getText().length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "Введите данные", Toast.LENGTH_SHORT);
            toast.show();

        }

        else {

            RequestPOST_Search requestPOSTSearch =new RequestPOST_Search(SearchActivity.this);
            requestPOSTSearch.execute("http://catalog.kembibl.ru/SearchForms/simpleSearch", Title, Author,genre_book,date_start_book,date_finish_book, IfFullText, type_of_book_all,type_of_book1,type_of_book2,type_of_book3,
                    type_of_book4,type_of_book5,type_of_book6,type_of_book7,type_of_book8,type_of_book9,type_of_book10,type_of_book11,type_of_book12,type_of_book13,type_of_book14,type_of_book15,type_of_book16,type_of_book17);


            LayoutGenre.setVisibility(View.GONE);
            LayoutYears.setVisibility(View.GONE);
            LayoutTypeBook.setVisibility(View.GONE);
            LayoutAuthor.setVisibility(View.GONE);
            LayoutTitle.setVisibility(View.GONE);
            LayoutButton.setVisibility(View.GONE);
            LayoutOnlyFulltext.setVisibility(View.GONE);
        }

        Title="";
        Author="";
        genre_book="";
        date_start_book="";
        date_finish_book="";
        type_of_book_all="1";
        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";


    }

    //Поиск одной строкой
    public void goSearchBooksOneSearch() {

        Line=line.getText().toString();

        String IfFullText="0";

        if (OnlyFullText.isChecked()){
            IfFullText="1";
        } else {
            IfFullText="0";
        }

        //Использование регулярок, чтобы определить, где автор и где название
        if (line.getText().length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "Введите данные", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {

            String name_book="";
            String author_book="";

            //String REGEX = "\\(.*?\\)";
            String REGEX = "(?<=[\"'])[^\"']+";
            String INPUT = Line;
            String REPLACE = "";

            Pattern p = Pattern.compile(REGEX);
            Matcher m = p.matcher(INPUT);   // get a matcher object
            if (m.find( )) {
                name_book=m.group(0);
            }else {

            }

            String REGEX2 = "\"(\\w*\\s+\"|:?)(.*?)\"";
            String INPUT2 = Line;
            String REPLACE2 = "";

            Pattern p2 = Pattern.compile(REGEX2);
            Matcher m2 = p2.matcher(INPUT2);   // get a matcher object
            INPUT2 = m2.replaceAll(REPLACE2);
            author_book=INPUT2;
            if (author_book.equals(" ")){
                author_book="";
            }


            RequestPOST_Search requestPOSTSearch =new RequestPOST_Search(SearchActivity.this);
            //requestPOSTSearch.execute("http://catalog.kembibl.ru/SearchForms/simpleSearch", name_book, author_book);
            requestPOSTSearch.execute("http://catalog.kembibl.ru/SearchForms/simpleSearch", name_book, author_book,genre_book,date_start_book,date_finish_book, IfFullText, type_of_book_all,type_of_book1,type_of_book2,type_of_book3,
                    type_of_book4,type_of_book5,type_of_book6,type_of_book7,type_of_book8,type_of_book9,type_of_book10,type_of_book11,type_of_book12,type_of_book13,type_of_book14,type_of_book15,type_of_book16,type_of_book17);

            LayoutOneSearch.setVisibility(View.GONE);
            LayoutButton.setVisibility(View.GONE);
            LayoutOnlyFulltext.setVisibility(View.GONE);

        }

        Line="";
        Title="";
        Author="";
        genre_book="";
        date_start_book="";
        date_finish_book="";
        type_of_book_all="1";
        type_of_book1=""; type_of_book2="";type_of_book3="";type_of_book4="";type_of_book5="";type_of_book6=""; type_of_book7="";type_of_book8="";type_of_book9="";type_of_book10="";type_of_book11="";
        type_of_book12="";type_of_book13="";type_of_book14="";type_of_book15="";type_of_book16="";type_of_book17="";



    }
    //Отображение прогресс бара
    public void view_progress_bar(){
        linearLayout.setVisibility(LinearLayout.VISIBLE);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }
    //Закрытие прогресс бара
    public void close_progress_bar(){
        linearLayout.setVisibility(LinearLayout.GONE);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }
    //Отображение списка книг
    public void ViewList(ArrayList<BookModel> bookModels, String[] items, final String[] IdBooks , final String[] nameBook, final String[] authorBook, final String[] typeBook, final String[] dateBook){
        //final ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
        ArrayAdapter<BookModel> myAdapter = new BookArrayAdapter(this, 0, bookModels);
        myBooks.setAdapter(myAdapter);
        myBooks.setFastScrollEnabled(true);
        myBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String IdBook=IdBooks[position];
                String NameBook=nameBook[position];
                String AuthorBook=authorBook[position];
                String TypeBook=typeBook[position];
                String DateBook=dateBook[position];

                //String BookID=myBooks.getItemAtPosition(position).toString();
                startActivity(new Intent(getApplicationContext(), ExemplActivity.class).putExtra("idBook",IdBook).putExtra("NameBook",NameBook).putExtra("AuthorBook",AuthorBook).putExtra("TypeBook",TypeBook).putExtra("DateBook",DateBook).putExtra("urlBook",""));

            }
        });


    }
    //Очистка списка
    public void ClearList(){
        ArrayList<BookModel> bookModels = new ArrayList<>();
        ArrayAdapter<BookModel> myAdapter = new BookArrayAdapter(this, 0, bookModels);
        myBooks.setAdapter(myAdapter);



    }
}
