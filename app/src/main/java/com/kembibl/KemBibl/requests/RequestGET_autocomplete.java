package com.kembibl.KemBibl.requests;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kembibl.KemBibl.SearchActivity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//Класс для реализации автоподсказок при вводе запроса
public class RequestGET_autocomplete extends AsyncTask<String, String, String> {

    Context ctx;

    SearchActivity sa;
    String contentType = "";
    String responseString = null;

    String typeComplete="";
    List<String> mlistData = new ArrayList<>();

    public RequestGET_autocomplete(Context context) {
        ctx = context;
        this.sa = (SearchActivity) ctx;
    }

    protected void onPreExecute() {
        super.onPreExecute();

    }
    // Выполнение асинхронного запроса к сайту
    @Override
    protected String doInBackground(String... uri) {

        typeComplete=uri[1];
        HttpClient httpclient = new DefaultHttpClient();
        //HttpPost httpPost = new HttpPost(uri[0]);
        HttpGet httpGet = new HttpGet(uri[0]);
        String cake_cookie = "";


        HttpResponse response;
        String statusline = null;

            try {
                    response = httpclient.execute(httpGet);
                    StatusLine statusLine = response.getStatusLine();
                    statusline = response.getStatusLine().toString();
                    //Header[] contentTypeHeader = response.getHeaders("Location");

                    if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        response.getEntity().writeTo(out);
                        responseString = out.toString();
                        out.close();
                    } else {
                        //Closes the connection.
                        response.getEntity().getContent().close();
                        throw new IOException(statusLine.getReasonPhrase());
                    }

            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;

        }




    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Toast toast = Toast.makeText(ctx, "cookie: " +result, Toast.LENGTH_LONG);
        // toast.show();

        /////////////////////////////////////////////////////

        if(result==null){

        } else {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if ((jsonArray.length() == 0) || (jsonArray==null)) {

                this.sa.closeBar();
                this.sa.closeBar2();
                this.sa.closeBar3();
                this.sa.setMult(mlistData,typeComplete);

            } else {
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mlistData.add(jsonArray.getJSONObject(i).getString("result"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                this.sa.setMult(mlistData,typeComplete);
                this.sa.closeBar();
                this.sa.closeBar2();
                this.sa.closeBar3();

            }

        }

    }
    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast toast = Toast.makeText(ctx, "cancel: ", Toast.LENGTH_SHORT);
        toast.show();


    }

}
