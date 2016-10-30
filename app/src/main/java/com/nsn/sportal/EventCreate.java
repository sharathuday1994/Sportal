package com.nsn.sportal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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


public class EventCreate extends AsyncTask<String,Void,String> {

    public String event_url = "http://10.0.2.2/Miniproject/events.php";
    String name,eventid,date,sport,loc,desc,userid,inventoryState;
    Context ctx;

    public EventCreate(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        eventid = params[0];
        name = params[1];
        date = params[2];
        sport = params[3];
        loc = params[4];
        desc = params[5];
        userid = LoginActivity.globalUserID;
        inventoryState = params[6];


        HttpURLConnection httpURLConnection=null;
        InputStream IS=null;
        String error="";
        int temp;
        try{
            URL url = new URL(event_url);
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
            parameters.put("eventid", eventid);
            parameters.put("name", name);
            parameters.put("date", date);
            parameters.put("location", loc);
            parameters.put("description", desc);
            parameters.put("sport", sport);
            parameters.put("userid", userid);
            parameters.put("inventorystate", inventoryState);
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
        return error;
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
    protected void onPostExecute(String success) {
        super.onPostExecute(success);
        if(success!=null){
            Toast.makeText(ctx, success, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(ctx,"Failed to Add Event",Toast.LENGTH_LONG).show();
        }

    }
}
