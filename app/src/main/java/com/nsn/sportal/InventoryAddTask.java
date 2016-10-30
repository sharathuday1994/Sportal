package com.nsn.sportal;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by nihalpradeep on 17/05/16.
 */
public class InventoryAddTask extends AsyncTask<String,Void,String> {


    Context ctx;
    String inventory_url = "http://10.0.2.2/Miniproject/inventory.php";
    String title,sport,quantity;

    public InventoryAddTask(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        title = params[0];
        sport = params[1];
        quantity = params[2];

        HttpURLConnection httpURLConnection = null;
        InputStream IS = null;
        String error = "";
        int temp;
        try {

            URL url = new URL(inventory_url);
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
            parameters.put("userid",LoginActivity.globalUserID);
            parameters.put("title", title);
            parameters.put("sport", sport);
            parameters.put("quantity", quantity);
            String data = getQuery(parameters);




            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();

            IS = httpURLConnection.getInputStream();

            while((temp=IS.read())!=-1){
                error += (char)temp;
            }

            return error;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Exception: "+e.getMessage();
        } catch (ProtocolException e) {
            e.printStackTrace();
            return "Exception: "+e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception: "+e.getMessage();
        } finally {
            if(httpURLConnection!=null)

            {
                httpURLConnection.disconnect();

            }
            try {
                if(IS!=null) {
                    IS.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


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

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(ctx,s, Toast.LENGTH_LONG).show();
    }
}
