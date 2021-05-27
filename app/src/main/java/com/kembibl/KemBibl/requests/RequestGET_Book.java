package com.kembibl.KemBibl.requests;

import android.content.Context;
import android.os.AsyncTask;

import com.kembibl.KemBibl.ExemplActivity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Класс для реализации получения информации о книге
public class RequestGET_Book extends AsyncTask<String, String, String> {

    Context ctx;

    ExemplActivity ae;

    public RequestGET_Book(Context context) { ctx=context; this.ae = (ExemplActivity) ctx; }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.ae.view_progress_bar();

    }
// Выполнение асинхронного запроса к сайту
    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        String statusline=null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            statusline=response.getStatusLine().toString();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        //return responseString;
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Document doc = null;
        String descr="";

        doc = Jsoup.parse(result, "UTF-8");
        Element form = doc.getElementById("ISBD");
        String htmlISBD=form.html();
        //Далее идет работа с регулярными выражениями
        //Поиск и замена ссылок
        String REGEX22 = "<a href=(\\w*\\s+\"|:?)(.*?)>";
        String INPUT22 = htmlISBD;
        String REPLACE22 = "";

        Pattern p22 = Pattern.compile(REGEX22);
        Matcher m22 = p22.matcher(INPUT22);   // get a matcher object
        INPUT22 = m22.replaceAll(REPLACE22);
        htmlISBD=INPUT22;
        htmlISBD=htmlISBD.replace("</a>", "");
        //Toast toast = Toast.makeText(ctx, htmlISBD, Toast.LENGTH_LONG);
        //toast.show();


        String Text_exempl=form.text();
        //String Text_descr=form.text();
        String Text_descr=htmlISBD;
        // Поиск и удаление лишней информации связанной с вертикальными и горизонтальными связями
        String INPUT0 = Text_descr;
        String REGEX0 = "\\ББК[\\s\\S]*";
        String REPLACE0 = "";

        Pattern p0 = Pattern.compile(REGEX0);
        Matcher m0 = p0.matcher(INPUT0);   // get a matcher object
        INPUT0 = m0.replaceAll(REPLACE0);
        Text_descr=INPUT0;


       // this.ae.ViewDescr(form.text());
        // Поиск сылок для перехода к пдф-версии книги
        String INPUT = Text_exempl;
        String REGEX = "http\\:[\\s\\S]*";
        String REPLACE = "";
        String pdfUri="";

        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(INPUT);   // get a matcher object
        //INPUT = m.replaceAll(REPLACE);
        if (m.find( )) {
            pdfUri=m.group(0);
        }else {

        }

        // обрезание ссылки, в связи с различными ситуациями
        String REGEX2 = "\\, [\\s\\S]*";
        String INPUT2 = pdfUri;
        String REPLACE2 = "";

        Pattern p2 = Pattern.compile(REGEX2);
        Matcher m2 = p2.matcher(INPUT2);   // get a matcher object
        INPUT2 = m2.replaceAll(REPLACE2);
        pdfUri=INPUT2;

        String REGEX3 = "\\ \\..[\\s\\S]*";
        String INPUT3 = pdfUri;
        String REPLACE3 = "";

        Pattern p3 = Pattern.compile(REGEX3);
        Matcher m3 = p3.matcher(INPUT3);   // get a matcher object
        INPUT3 = m3.replaceAll(REPLACE3);
        pdfUri=INPUT3;

        String REGEX4 = "\\ :.[\\s\\S]*";
        String INPUT4 = pdfUri;
        String REPLACE4 = "";

        Pattern p4 = Pattern.compile(REGEX4);
        Matcher m4 = p4.matcher(INPUT4);   // get a matcher object
        INPUT4 = m4.replaceAll(REPLACE4);
        pdfUri=INPUT4; // получение итоговой ссылки


        // если ссылки нет, то ничего не делать, если есть, то запустить функцию отображения пдф
        if(pdfUri.equals("")){

        } else {
            this.ae.ViewPdfBut(pdfUri);
        }

       this.ae.ViewDescr(Text_descr);
        //this.ae.ViewDescr(stylecss+Text_descr);

    }
}
