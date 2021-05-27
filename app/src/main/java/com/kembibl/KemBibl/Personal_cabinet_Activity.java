package com.kembibl.KemBibl;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;

import com.kembibl.KemBibl.adapters.BookBronArrayAdapter;
import com.kembibl.KemBibl.adapters.BookDelivArrayAdapter;
import com.kembibl.KemBibl.adapters.BookHistoryArrayAdapter;
import com.kembibl.KemBibl.adapters.BookShelfArrayAdapter;
import com.kembibl.KemBibl.data.KemBible_DbHelper;
import com.kembibl.KemBibl.data.KemBible_data;
import com.kembibl.KemBibl.models.BookModel;
import com.kembibl.KemBibl.requests.RequestGET_history;
import com.kembibl.KemBibl.requests.RequestGET_personal_cab;
import com.kembibl.KemBibl.requests.RequestGET_polka;
import com.kembibl.testbible3.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Активность отвечает за отображение личного кабинета(логика отображения)
public class Personal_cabinet_Activity extends AppCompatActivity {

    String personal_info="";
    EditText personal_text;
    ListView myBron_Info;
    ListView myDelivery;
    ListView myGetting_Books;
    ListView myHistory_Books;
    ListView myPolka_Books;
    LinearLayout Layotinf, LayoutDeliv, LayoutGetBooks, LayoutHistoryBooks;
    LinearLayout linearHistory, linearPolka;
    String ifLogin="";
    TabHost tabHost;
    Button getNowBronBooks, getNowGetBooks, getBooksBut, getHistoryBut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_cabinet);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Личный кабинет");
        //Получение всех элементов
        linearHistory=(LinearLayout)findViewById(R.id.indicatorHistory);
        linearPolka=(LinearLayout)findViewById(R.id.indicatorPolka);


        myBron_Info=(ListView)findViewById(R.id.myBron);
        myDelivery=(ListView)findViewById(R.id.myDeliv);
        myGetting_Books=(ListView)findViewById(R.id.myGettingBooksList);
        myHistory_Books=(ListView)findViewById(R.id.myHistory);
        myPolka_Books=(ListView)findViewById(R.id.myPolka);
        Layotinf=(LinearLayout) findViewById(R.id.BronLay);
        LayoutDeliv=(LinearLayout) findViewById(R.id.DelivLay);
        LayoutGetBooks=(LinearLayout)findViewById(R.id.myGetBooksLayout);
        LayoutHistoryBooks=(LinearLayout)findViewById(R.id.myHistoryBooksLayout);

        getNowBronBooks=(Button)findViewById(R.id.BronInfoButton);
        getNowGetBooks=(Button)findViewById(R.id.personal_delivButton);
        getBooksBut=(Button)findViewById(R.id.myGettingBooks);
        getHistoryBut=(Button)findViewById(R.id.myHistory_Books);

        tabHost = (TabHost) findViewById(R.id.tabHost_LC);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");

        tabSpec.setContent(R.id.LayoutInfo);
        tabSpec.setIndicator("Текущие заказы");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.LayoutHistory);
        tabSpec.setIndicator("История");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.LayoutPolka);
        tabSpec.setIndicator("Полка");
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);



        personal_text=(EditText) findViewById(R.id.personalInfo);

        RequestGET_personal_cab requestGET_personalcab =new RequestGET_personal_cab(Personal_cabinet_Activity.this);
        requestGET_personalcab.execute("http://catalog.kembibl.ru/user_card");

        //При выборе определенной вкладки
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int i = tabHost.getCurrentTab();
                if (i==0){
                    RequestGET_personal_cab requestGET_personalcab =new RequestGET_personal_cab(Personal_cabinet_Activity.this);
                    requestGET_personalcab.execute("http://catalog.kembibl.ru/user_card");
                    //RequestGET_history RequestGET_history=new RequestGET_history(Personal_cabinet_Activity.this);
                    //RequestGET_history.execute("http://catalog.kembibl.ru/user_card/history");
                }

                if (i==1){
                    RequestGET_history RequestGET_history=new RequestGET_history(Personal_cabinet_Activity.this);
                    RequestGET_history.execute("http://catalog.kembibl.ru/user_card/history");
                }

                if (i==2){
                    RequestGET_polka RequestGET_polka=new RequestGET_polka(Personal_cabinet_Activity.this);
                    RequestGET_polka.execute("http://catalog.kembibl.ru/bookshelves");
                }

            }
        });



    }
    //Показать прогресс бар, история бронирования
    public void view_progress_bar_history(){
        if ((linearHistory.getVisibility() == View.GONE)) {
            // действия, когда виджет видим

        } else {
            // действия, когда невидим.
            linearHistory.setVisibility(LinearLayout.VISIBLE);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarHistory);
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

    }
    //Закрыть прогресс бар, история бронирования
    public void close_progress_bar_history(){
        linearHistory.setVisibility(LinearLayout.GONE);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarHistory);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }
    //Показать прогресс бар, полка
    public void view_progress_bar_polka(){
        if ((linearPolka.getVisibility() == View.GONE)) {
            // действия, когда виджет видим

        } else {
            // действия, когда невидим.
            linearPolka.setVisibility(LinearLayout.VISIBLE);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarPolka);
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

    }
    //Закрыть прогресс бар, полка
    public void close_progress_bar_polka(){
        linearPolka.setVisibility(LinearLayout.GONE);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarPolka);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }


    //Для менюшки сверху
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //this.finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            case R.id.action_change_user:
                try {

                    DeleteUserCookie();
                    DeleteUserData();



                } catch (Throwable t){

                }
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Удалить куки пользователя
    public void DeleteUserCookie(){
        int id=1;
        String idString="1";
        KemBible_DbHelper kemBible_dbHelper = new KemBible_DbHelper(this);

        SQLiteDatabase db = kemBible_dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KemBible_data.CookieTable._ID, id);
        values.put(KemBible_data.CookieTable.COLUMN_VALUE, "");

        long newRowId=db.update(KemBible_data.CookieTable.TABLE_NAME, values, KemBible_data.CookieTable._ID + "= ?", new String[]{idString});

    }
    //Удалить данные о пользователе
    public void DeleteUserData(){
        KemBible_DbHelper kemBible_dbHelper = new KemBible_DbHelper(this);

        SQLiteDatabase db = kemBible_dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        String idString="1";
        int id = 1;
        values.put(KemBible_data.UserTable._ID, id);
        values.put(KemBible_data.UserTable.COLUMN_LOGIN, "");
        values.put(KemBible_data.UserTable.COLUMN_PASSWORD, "");

        db.update(KemBible_data.UserTable.TABLE_NAME, values, KemBible_data.UserTable._ID + "= ?", new String[]{idString});

    }


    //Показать персональную информацию
    public void ViewPersonalInfo(String result){
        personal_text.setText(result);

    }
    //Показать информацию о бронировании
    public void ViewBronInfo(final ArrayList<BookModel> bookModels, String[] personal_info){

        //final ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, personal_info);
        ArrayAdapter<BookModel> myAdapter = new BookBronArrayAdapter(this, 0, bookModels);
        myBron_Info.setAdapter(myAdapter);
        myBron_Info.setFastScrollEnabled(true);
        myBron_Info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BookModel bookModel = bookModels.get(position);
                String REGEX="(?<=/IdNotice:).*";
                String INPUT= bookModel.getAdd_Url();
                String IdBook="";
                Pattern p = Pattern.compile(REGEX);
                Matcher m = p.matcher(INPUT);   // get a matcher object
                if (m.find( )) {
                    IdBook=m.group(0);
                }else {

                }

                startActivity(new Intent(getApplicationContext(), ExemplActivity.class).putExtra("idBook",IdBook).putExtra("NameBook", bookModel.getTitle()).putExtra("AuthorBook", bookModel.getAuthor()).putExtra("TypeBook","").putExtra("DateBook", bookModel.getDate()).putExtra("urlBook", bookModel.getAdd_Url()));


            }
        });

    }
    //Показать информацию о заказах
    public void ViewMyDeliv(final ArrayList<BookModel> bookModels, String[] myDeliv_info){
        //final ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, myDeliv_info);
        ArrayAdapter<BookModel> myAdapter = new BookDelivArrayAdapter(this, 0, bookModels);
        //Toast.makeText(Personal_cabinet_Activity.this, "Дата "+bookModels.get(0), Toast.LENGTH_LONG).show();
        myDelivery.setAdapter(myAdapter);
        myDelivery.setFastScrollEnabled(true);
        myDelivery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BookModel bookModel = bookModels.get(position);
                String REGEX="(?<=/IdNotice:).*";
                String INPUT= bookModel.getAdd_Url();
                String IdBook="";
                Pattern p = Pattern.compile(REGEX);
                Matcher m = p.matcher(INPUT);   // get a matcher object
                if (m.find( )) {
                    IdBook=m.group(0);
                }else {

                }
                startActivity(new Intent(getApplicationContext(), ExemplActivity.class).putExtra("idBook",IdBook).putExtra("NameBook", bookModel.getTitle()).putExtra("AuthorBook", bookModel.getAuthor()).putExtra("TypeBook","").putExtra("DateBook", bookModel.getDate()).putExtra("urlBook", bookModel.getAdd_Url()));


            }
        });

    }

    //Показать информацию о ранее взятых книгах
    public void ViewGettingBooks(final ArrayList<BookModel> bookModels, String[] history_book, final String[] AddToPolka){

        ArrayAdapter<BookModel> myAdapter = new BookHistoryArrayAdapter(this, 0, bookModels);
        myGetting_Books.setAdapter(myAdapter);
        //myHistory_Books.setFastScrollEnabled(true);
        myGetting_Books.setFastScrollAlwaysVisible(true);
        myGetting_Books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        String REGEX="(?<=/IdNotice:).*";
                        String INPUT=AddToPolka[position];
                        String IdBook="";
                        Pattern p = Pattern.compile(REGEX);
                        Matcher m = p.matcher(INPUT);   // get a matcher object
                        if (m.find( )) {
                            IdBook=m.group(0);
                        }else {

                        }
                        final BookModel bookModel = bookModels.get(position);
                startActivity(new Intent(getApplicationContext(), ExemplActivity.class).putExtra("idBook",IdBook).putExtra("NameBook", bookModel.getTitle()).putExtra("AuthorBook", bookModel.getAuthor()).putExtra("TypeBook","").putExtra("DateBook", bookModel.getDate()).putExtra("urlBook",AddToPolka[position]));


            }
        });

    }
    //Показать историю бронирований
    public void ViewHistory(final ArrayList<BookModel> bookModels, String[] history_book, final String[] AddToPolka){


        ArrayAdapter<BookModel> myAdapter = new BookHistoryArrayAdapter(this, 0, bookModels);
        myHistory_Books.setAdapter(myAdapter);
        //myHistory_Books.setFastScrollEnabled(true);
        myHistory_Books.setFastScrollAlwaysVisible(true);
        myHistory_Books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        String REGEX = "(?<=/IdNotice:).*";
                        String INPUT = AddToPolka[position];
                        String IdBook = "";
                        Pattern p = Pattern.compile(REGEX);
                        Matcher m = p.matcher(INPUT);   // get a matcher object
                        if (m.find()) {
                            IdBook = m.group(0);
                        } else {

                        }
                        final BookModel bookModel = bookModels.get(position);

                startActivity(new Intent(getApplicationContext(), ExemplActivity.class).putExtra("idBook",IdBook).putExtra("NameBook", bookModel.getTitle()).putExtra("AuthorBook", bookModel.getAuthor()).putExtra("TypeBook","").putExtra("DateBook", bookModel.getDate()).putExtra("urlBook",AddToPolka[position]));


            }
        });

    }
    //Показать полку
    public void ViewPolka(ArrayList<BookModel> bookModels){
        //final ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, polka_book);
        ArrayAdapter<BookModel> myAdapter = new BookShelfArrayAdapter(this, 0, bookModels);
        myPolka_Books.setAdapter(myAdapter);
        //myPolka_Books.setFastScrollAlwaysVisible(true);
        myPolka_Books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        //biblExempl.setText(booksExempl.toString());
    }

 @Override
    public void onBackPressed() {
        // super.onBackPressed();
     startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
    //Действия когда нажать кнопки открыть список бронировани, закрыть и тд
    public void visibleBron(View view) {
        if ((Layotinf.getVisibility() == View.VISIBLE)) {
            // действия, когда виджет видим
            Layotinf.setVisibility(View.GONE);
            personal_text.setVisibility(View.VISIBLE);
            getNowBronBooks.setText("Показать заброн. документы");
            getNowGetBooks.setVisibility(View.VISIBLE);

        } else {
            // действия, когда невидим.
            Layotinf.setVisibility(View.VISIBLE);
            LayoutDeliv.setVisibility(View.GONE);
            personal_text.setVisibility(View.GONE);
            getNowBronBooks.setText("Скрыть заброн. документы");
            getNowGetBooks.setVisibility(View.GONE);

        }


    }

    public void visibleDeliv(View view) {
        if ((LayoutDeliv.getVisibility() == View.VISIBLE)) {
            // действия, когда виджет видим
            LayoutDeliv.setVisibility(View.GONE);
            personal_text.setVisibility(View.VISIBLE);
            getNowGetBooks.setText("Показать текущие выдачи");
            getNowBronBooks.setVisibility(View.VISIBLE);

        } else {
            // действия, когда невидим.
            LayoutDeliv.setVisibility(View.VISIBLE);
            Layotinf.setVisibility(View.GONE);
            personal_text.setVisibility(View.GONE);
            getNowGetBooks.setText("Скрыть текущие выдачи");
            getNowBronBooks.setVisibility(View.GONE);
        }

    }

    public void visibleMyGetBooks(View view) {
        if ((LayoutGetBooks.getVisibility() == View.VISIBLE)) {
            // действия, когда виджет видим
            LayoutGetBooks.setVisibility(View.GONE);
            getBooksBut.setText("Показать ранее взятые книги");
            getHistoryBut.setVisibility(View.VISIBLE);

        } else {
            // действия, когда невидим.
            LayoutGetBooks.setVisibility(View.VISIBLE);
            LayoutHistoryBooks.setVisibility(View.GONE);
            getBooksBut.setText("Скрыть ранее взятые книги");
            getHistoryBut.setVisibility(View.GONE);
        }

    }

    public void visibleMyHistoryBooks(View view) {
        if ((LayoutHistoryBooks.getVisibility() == View.VISIBLE)) {
            // действия, когда виджет видим
            LayoutHistoryBooks.setVisibility(View.GONE);
            getHistoryBut.setText("Показать историю бронирований");
            getBooksBut.setVisibility(View.VISIBLE);

        } else {
            // действия, когда невидим.
            LayoutHistoryBooks.setVisibility(View.VISIBLE);
            LayoutGetBooks.setVisibility(View.GONE);
            getHistoryBut.setText("Скрыть историю бронирований");
            getBooksBut.setVisibility(View.GONE);
        }

    }

}
