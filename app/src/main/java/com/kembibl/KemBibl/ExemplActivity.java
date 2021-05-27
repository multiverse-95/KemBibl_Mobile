package com.kembibl.KemBibl;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kembibl.KemBibl.adapters.BookExemplAdapter;
import com.kembibl.KemBibl.data.KemBible_DbHelper;
import com.kembibl.KemBibl.data.KemBible_data;
import com.kembibl.KemBibl.models.BiblesModel;
import com.kembibl.KemBibl.models.Book_Exempl_Model;
import com.kembibl.KemBibl.requests.RequestGET_Book;
import com.kembibl.KemBibl.requests.RequestGET_ExemplList;
import com.kembibl.KemBibl.requests.RequestGET_Tokens;
import com.kembibl.KemBibl.requests.Request_AddToFavour;
import com.kembibl.testbible3.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// Класс отвечает за отображение экземпляра книги
public class ExemplActivity extends AppCompatActivity {
    //Перечисление переменных
    String IdBook;

    String user_login="";
    String user_password="";
    String nameBook;
    String authorBook;
    String typeBook;
    String dateBook;
    String urlBook;
    String Image="";
    String annexe;
    String fond_number;
    Button openPdf;
    Button addFav;
    EditText biblDescr;
    EditText biblExempl;
    //TextView biblDescr;
    EditText exempl;
    //EditText test_html;
    ListView myExempl;
    TabHost tabHost;
    EditText showDescr;

    TextView author;
    TextView title;
    TextView date;
    TextView type_book;
    ImageView image;

    private WebView webView;

    RelativeLayout shortDescr;
    LinearLayout fullDescr, progress;
    int callResume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exempl);

        webView = findViewById(R.id.Description2);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        IdBook = bundle.getString("idBook");
        nameBook = bundle.getString("NameBook");
        authorBook = bundle.getString("AuthorBook");
        typeBook = bundle.getString("TypeBook");
        dateBook = bundle.getString("DateBook");
        urlBook=bundle.getString("urlBook");
        openPdf=(Button) findViewById(R.id.open_pdf);

        addFav=(Button)findViewById(R.id.add_polka_but);

        biblDescr=(EditText) findViewById(R.id.Description);
        biblExempl=(EditText)findViewById(R.id.Exempl);
        exempl=(EditText)findViewById(R.id.Exempl);
        showDescr=(EditText)findViewById(R.id.Descr);
        //test_html=(EditText)findViewById(R.id.Test_html);
        myExempl=(ListView)findViewById(R.id.myExempl);


        author = (TextView) findViewById(R.id.author_book);
        title = (TextView) findViewById(R.id.title_book);
        date = (TextView) findViewById(R.id.date_book);
        type_book = (TextView) findViewById(R.id.type_book);
        image = (ImageView) findViewById(R.id.image);

        progress=(LinearLayout)findViewById(R.id.indicator);
        shortDescr=(RelativeLayout)findViewById(R.id.ShortDescr);
        fullDescr=(LinearLayout)findViewById(R.id.FullDescr);


        setTitle("Книга");

        tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");

        tabSpec.setContent(R.id.linearLayout);
        tabSpec.setIndicator("Библ.описание");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.linearLayout2);
        tabSpec.setIndicator("Экземпляры");
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);

        //Проверка на авторизацию
        GetUserdata();
        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_login.equals("") || user_password.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Для добавления книги на полку, необходимо авторизоваться в личном кабинете!", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Request_AddToFavour Request_AddToFavour =new Request_AddToFavour(ExemplActivity.this);
                    Request_AddToFavour.execute("http://catalog.kembibl.ru/user_card/addbook/IdNotice:"+IdBook+"/Source:default");
                }

            }
        });



        RequestGET_Book requestGETBook =new RequestGET_Book(ExemplActivity.this);
        requestGETBook.execute("http://catalog.kembibl.ru/notices/getIsbdAjax/"+IdBook+"/default?IdNotice="+IdBook+"&source=default");

        RequestGET_ExemplList RequestGET_ExemplList =new RequestGET_ExemplList(ExemplActivity.this);
        RequestGET_ExemplList.execute("http://catalog.kembibl.ru/notices/getExemplaires/"+IdBook+"/default?IdNotice="+IdBook+"&Source=default");


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            //case R.id.action_settings:

                //return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Функция отвечает за отображение описания книги
    public void ViewDescr(final String result){



        if (urlBook.equals("")){
            //Toast.makeText(ExemplActivity.this, "IssssD: ", Toast.LENGTH_LONG).show();
            if(typeBook.equals("Выпуск")){
                Image="newspaper_icon_2";
            } else if (typeBook.equals("Статья")){
                Image="article_icon";
            } else if (typeBook.equals("Электронный ресурс ") || typeBook.equals("Электронный ресурс")){
                Image="disc_icon_3";
            } else if (typeBook.equals("Видеозапись")){
                Image="video_icon";
            } else if (typeBook.equals("Звукозапись")){
                Image="sound_icon";
            } else {
                Image="book_icon_2";
            }
            if (authorBook.equals("")){
                Toast.makeText(ExemplActivity.this, authorBook, Toast.LENGTH_LONG).show();
                authorBook="(автор не указан)";
            }

            author.setText(authorBook);
            int titleLength = nameBook.length();
            if(titleLength >= 100){
                String descriptionTrim = nameBook.substring(0, 100) + "...";
                title.setText(descriptionTrim);
            }else{
                title.setText(nameBook);
            }


            date.setText("Дата выпуска: " + String.valueOf(dateBook));
            type_book.setText("Тип: "+ String.valueOf(typeBook));


            int imageID = getApplicationContext().getResources().getIdentifier(Image, "drawable", getApplicationContext().getPackageName());
            image.setImageResource(imageID);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                biblDescr.setText(Html.fromHtml(result, Html.FROM_HTML_MODE_COMPACT));
            } else {
                biblDescr.setText(Html.fromHtml(result));
            }

            //webView.loadData(text_result , "text/html; charset=UTF-8", null);
            close_progress_bar();
        } else {
            String text="";
            text=urlBook;
            // Instantiate the RequestQueue.

            ApiCall.make(ExemplActivity.this, text, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //parsing logic, please change it as per your requirement
                    Document doc = null;
                    doc = Jsoup.parse(response, "UTF-8");
                    Elements tables = doc.getElementsByClass("bookdetails");
                    //Elements tr= tables.first().select("a");
                    String aText=tables.first().text();
                    String REGEX2 = "( Автор:.+)";
                    String INPUT2 = aText;
                    String REPLACE2 = "";
                    String TypeBook="";

                    Pattern p2 = Pattern.compile(REGEX2);
                    Matcher m2 = p2.matcher(INPUT2);   // get a matcher object
                    INPUT2 = m2.replaceAll(REPLACE2);
                    TypeBook=INPUT2;
                    typeBook=TypeBook;
                    String authorbook0="";

                    if(typeBook.equals("Выпуск")){
                        Image="newspaper_icon_2";
                    } else if (typeBook.equals("Статья")){
                        Image="article_icon";
                    } else if (typeBook.equals("Электронный ресурс ") || typeBook.equals("Электронный ресурс")){
                        Image="disc_icon_3";
                    } else if (typeBook.equals("Видеозапись")){
                        Image="video_icon";
                    } else if (typeBook.equals("Звукозапись")){
                        Image="sound_icon";
                    } else {
                        Image="book_icon_2";
                    }
                    if (authorBook.equals("")){

                        authorBook="(автор не указан)";
                    }
                    author.setText(authorBook);
                    int titleLength = nameBook.length();
                    if(titleLength >= 100){
                        String descriptionTrim = nameBook.substring(0, 100) + "...";
                        title.setText(descriptionTrim);
                    }else{
                        title.setText(nameBook);
                    }

                    //set price and rental attributes
                    date.setText("Дата выпуска: " + String.valueOf(dateBook));
                    type_book.setText("Тип: "+ String.valueOf(typeBook));


                    int imageID = getApplicationContext().getResources().getIdentifier(Image, "drawable", getApplicationContext().getPackageName());
                    image.setImageResource(imageID);
                    //biblDescr.setText(result);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        biblDescr.setText(Html.fromHtml(result, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        biblDescr.setText(Html.fromHtml(result));
                    }



                    close_progress_bar();



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
        }


    }
    //Показывает пдф-версию книги
    public void ViewPdfBut(final String pdfUr){
        openPdf.setVisibility(View.VISIBLE);
        openPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdfUr.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Увы, данный выпуск отсутствует!", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUr));
                    startActivity(browserIntent);
                }

            }
        });

    }


    //Показывает прогресс бар
    public void view_progress_bar(){
        progress.setVisibility(LinearLayout.VISIBLE);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }
    //Закрывает прогресс бар
    public void close_progress_bar(){
        progress.setVisibility(LinearLayout.GONE);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        shortDescr.setVisibility(View.VISIBLE);
        fullDescr.setVisibility(View.VISIBLE);
    }
    // Функция отвечает за отображение экземпляров книги, которые можно заказать
    public void ViewExempl(final ArrayList<Book_Exempl_Model> Books_Exempl, String[] booksExempl, final String[] adress_for_book, final String [] fonds_ex){


        final String title = "Заказ книги";
        final String message = "Вы хотите заказать эту книгу?";
        final String button1String = "Да";
        final String button2String = "Нет";

        //final ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, booksExempl);
        ArrayAdapter<Book_Exempl_Model> myAdapter = new BookExemplAdapter(this, 0, Books_Exempl);
        myExempl.setAdapter(myAdapter);
        myExempl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String count_book=Books_Exempl.get(position).getCount_book();
                if(count_book.equals("-")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Доступных книг в этом филиале нет!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ExemplActivity.this);
                    builder.setTitle(title);  // заголовок
                    builder.setMessage(message); // сообщение
                    builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            for (int i = 0; i< BiblesModel.BIBLES.length; i++){
                                //Toast toast = Toast.makeText(getApplicationContext(), BiblesModel.BIBLES[i].getShort_name(), Toast.LENGTH_SHORT);
                                //toast.show();
                                if (BiblesModel.BIBLES[i].getShort_name().equals(adress_for_book[position])){
                                    annexe= BiblesModel.BIBLES[i].getAnnexe();
                                }

                            }
                            if (fonds_ex[position].equals("книжный")){
                                fond_number="2";
                            } else if (fonds_ex[position].equals("брошюрный")){
                                fond_number="3";
                            } else if (fonds_ex[position].equals("электронный")){
                                fond_number="6";
                            } else if (fonds_ex[position].equals("газетно-журнальный")){
                                fond_number="4";
                            } else if (fonds_ex[position].equals("аудиовизуальный")){
                                fond_number="5";
                            } else {
                                fond_number="1";
                            }
                            if(user_login.equals("")|| user_password.equals("")){
                                Toast toast = Toast.makeText(getApplicationContext(), "Для бронирования книги, необходимо авторизоваться в личном кабинете!", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                RequestGET_Tokens RequestGET_Tokens =new RequestGET_Tokens(ExemplActivity.this);
                                RequestGET_Tokens.execute("http://catalog.kembibl.ru/books/reserve/"+IdBook+"/IdAnnexe:"+annexe+"/IdFonds:"+fond_number, IdBook,annexe);
                            }



                        }
                    });
                    builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.setCancelable(true);

                    builder.show();
                }



            }
        });

    }

    //Если нет экземпляров
    public void ViewExemplEmpty(String booksExempl){

        biblExempl.setText(booksExempl);
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


}
