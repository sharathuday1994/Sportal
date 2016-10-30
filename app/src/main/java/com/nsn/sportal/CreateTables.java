package com.nsn.sportal;

import android.content.ContentValues;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by nihalpradeep on 09/05/16.
 */
public class CreateTables extends AsyncTask<String,Void,Void> {
    String tables_url = "http://10.0.2.2/Miniproject/tables.php";
    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection httpURLConnection;
        InputStream IS;
        int temp;
        String error = "";
        String userid = params[0];


        try
        {
            URL url = new URL(tables_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);

            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
            ContentValues parameters = new ContentValues();
            parameters.put("userid", userid);
            String data = getQuery(parameters);

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();

            IS = httpURLConnection.getInputStream();
            while((temp=IS.read())!=-1){
                error += (char)temp;
            }
            Thread.sleep(2000);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;

    }
    private String getQuery(ContentValues parameters) {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(Map.Entry<String,Object> entry : parameters.valueSet()){
            if (first)
                first = false;
            else
                result.append("&");
            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue().toString(),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return result.toString();
    }

}
