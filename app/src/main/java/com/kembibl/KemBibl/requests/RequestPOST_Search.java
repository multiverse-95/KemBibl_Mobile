package com.kembibl.KemBibl.requests;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kembibl.KemBibl.ParsingXML;
import com.kembibl.KemBibl.SearchActivity;
import com.kembibl.KemBibl.models.BookModel;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
//Класс для отправки пост-запроса для поиска книг
public class RequestPOST_Search extends AsyncTask<String,String , String>  {

    Context ctx;

    SearchActivity sa;

    public RequestPOST_Search(Context context) { ctx=context; this.sa = (SearchActivity) ctx; }

    String[] items;
    String[] IdBooks;

    String[] nameBook;
    String[] authorBook;
    String[] typeBook;
    String[] dateBook;
    ArrayList<BookModel> bookModels = new ArrayList<>();
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast toast = Toast.makeText(ctx, "Идет поиск...", Toast.LENGTH_LONG);
        toast.show();
        this.sa.view_progress_bar();

    }



    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(uri[0]);
        String reqS = "";
        // Добавляем параметры и их значения для запроса
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("data[1][oper]", "AND"));
        urlParameters.add(new BasicNameValuePair("data[1][field]", "200_A"));
        urlParameters.add(new BasicNameValuePair("data[1][value]", uri[1]));
        urlParameters.add(new BasicNameValuePair("data[2][oper]", "AND"));
        urlParameters.add(new BasicNameValuePair("data[2][field]", "200_F"));
        urlParameters.add(new BasicNameValuePair("data[2][value]", uri[2]));
        urlParameters.add(new BasicNameValuePair("data[3][oper]", "AND"));
        urlParameters.add(new BasicNameValuePair("data[3][field]", "SUJET"));
        urlParameters.add(new BasicNameValuePair("data[3][value]", uri[3]));
        urlParameters.add(new BasicNameValuePair("data[between][210_D][start]", uri[4]));
        urlParameters.add(new BasicNameValuePair("data[between][210_D][end]", uri[5]));
        urlParameters.add(new BasicNameValuePair("data[exist][856_U]", uri[6]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][all]", uri[7]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][1]", uri[8]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][4]", uri[9]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][5]", uri[10]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][6]", uri[11]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][7]", uri[12]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][9]", uri[13]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][10]", uri[14]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][12]", uri[15]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][13]", uri[16]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][18]", uri[17]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][19]", uri[18]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][26]", uri[19]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][27]", uri[20]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][30]", uri[21]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][31]", uri[22]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][37]", uri[23]));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][43]", uri[24]));
        urlParameters.add(new BasicNameValuePair("data[libs][default][all]", "1"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][48]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][32]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][33]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][42]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][36]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][41]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][6]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][7]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][29]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][35]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][44]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][12]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][39]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][14]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][40]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][11]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][51]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][45]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][8]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][43]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][49]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][13]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][4]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][47]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][10]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][34]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][38]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][37]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][46]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][50]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][16]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][2]", "0"));

        //Устанавливаем параметры запроса в кодировку UTF-8
        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
                HttpResponse response = httpclient.execute(post);
                reqS = EntityUtils.toString(response.getEntity());


            //System.out.println(reqS);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return reqS;

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParsingXML parsingXML=new ParsingXML(this.sa);
        //items=parsingXML.ParseXML(result);
        bookModels =parsingXML.ParseXML(result);
        IdBooks=parsingXML.IDBooks();
        nameBook=parsingXML.NameBook();
        authorBook=parsingXML.AuthorBook();
        typeBook=parsingXML.TypeBook();
        dateBook=parsingXML.DateBook();

        this.sa.close_progress_bar();
        this.sa.ViewList(bookModels, items, IdBooks,nameBook,authorBook,typeBook,dateBook);

        //Toast toast = Toast.makeText(getApplicationContext(), Environment.getExternalStorageDirectory().toString(), Toast.LENGTH_SHORT);
        //toast.show();

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast toast = Toast.makeText(ctx, "cancel: ", Toast.LENGTH_SHORT);
        toast.show();
        //tvInfo.setText("Cancel");
        //Log.d(LOG_TAG, "Cancel");

    }
}
