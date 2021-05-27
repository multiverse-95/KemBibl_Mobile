package com.kembibl.KemBibl.requests;

import android.content.Context;
import android.os.AsyncTask;

import com.kembibl.KemBibl.ExemplActivity;
import com.kembibl.KemBibl.models.Book_Exempl_Model;

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
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
// Класс реализации доступных для заказа экземпляра
public class RequestGET_ExemplList extends AsyncTask<String, String, String> {

    Context ctx;

    ExemplActivity ae;
    ArrayList<Book_Exempl_Model> Books_Exempl = new ArrayList<>();

    public RequestGET_ExemplList(Context context) { ctx=context; this.ae = (ExemplActivity) ctx; }
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


        Element div_Exempl=doc.getElementById("ExempList");
        String empty_exempl=div_Exempl.text();
        if (empty_exempl.equals("Нет экземпляров для данного описания. Воспользуйтесь вкладкой \"Связи\" для перехода к экземплярам")) {
            empty_exempl = "Нет экземпляров";
            //Toast toast = Toast.makeText(ctx, empty_exempl, Toast.LENGTH_SHORT);
            //toast.show();
            this.ae.ViewExemplEmpty(empty_exempl);
        } else {

            empty_exempl = "Экземпляры:";
            this.ae.ViewExemplEmpty(empty_exempl);
            Elements adress_el = doc.getElementsByClass("annexe");
            Elements fonds_el = doc.getElementsByClass("fond");
            Elements total_el = doc.getElementsByClass("Available");

            String[] adress_mas = new String[adress_el.size()];
            String[] adress_for_book = new String[adress_el.size()];
            String[] fonds_ex = new String[adress_el.size()];
            String Image="touch_5";

            for (int i = 0; i < adress_el.size(); i++) {
                adress_mas[i] = "Адрес: " + adress_el.eq(i).text() + "\n" + "Фонд: " + fonds_el.eq(i).text() + "\n" + "Кол-во: " + total_el.eq(i).text()+"\n\n"+">>Нажмите для заказа<<";
                Books_Exempl.add(
                        new Book_Exempl_Model(adress_el.eq(i).text(), fonds_el.eq(i).text(), total_el.eq(i).text(), Image));
                adress_for_book[i]=adress_el.eq(i).text();
                fonds_ex[i]=fonds_el.eq(i).text();

            }

            this.ae.ViewExempl(Books_Exempl, adress_mas,adress_for_book,fonds_ex);


        }

    }
}
