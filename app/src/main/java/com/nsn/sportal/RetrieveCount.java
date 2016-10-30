package com.nsn.sportal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

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
public class RetrieveCount extends AsyncTask<String,Void,String> {

    Context ctx;
    String count_url = "http://10.0.2.2/Miniproject/count.php";
    String table;
    String check;
    String eventid,name,date,sport,loc,desc,inventoryState="";
    String first_name,last_name,email,password,userid,date_of_birth,gender,country_name,sport_name;
    String counted ="";
    public RetrieveCount(String check, Context ctx, String first_name,String last_name,String email,String password,String userid, String date_of_birth, String gender,String country_name,String sport_name){
        this.check = check;
        this.ctx = ctx;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.userid = userid;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.country_name = country_name;
        this.sport_name = sport_name;
    }

    public RetrieveCount(String check,Context ctx, String eventid, String name, String date,String sport,String loc,String desc,int inventoryState){

        this.check = check;
        this.ctx = ctx;
        this.eventid = eventid;
        this.name = name;
        this.date = date;
        this.sport = sport;
        this.loc = loc;
        this.desc = desc;
        this.inventoryState += inventoryState;
    }
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpURLConnection=null;
        InputStream IS=null;
        int temp;
        String error = "";
        table = params[0];


        try
        {
            URL url = new URL(count_url);
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
            parameters.put("table", table);
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
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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

    @Override
    protected void onPostExecute(String success) {
        super.onPostExecute(success);
        try{
            JSONObject root = new JSONObject(success);
            JSONObject user_data = root.getJSONObject("user_data");
            if(check.equals("Register")) {
                counted = user_data.getString("count");
                userid += gender + country_name + counted;
                UserRegisterTask mRegisterAuth = new UserRegisterTask(ctx);
                mRegisterAuth.execute(first_name,last_name,email,password,userid,date_of_birth,gender,country_name,sport_name);

            }
            if(check.equals("Event")){
                counted = user_data.getString("count");
                eventid += LoginActivity.globalUserID + counted;
                EventCreate eventCreate = new EventCreate(ctx);
                eventCreate.execute(eventid,name,date,sport,loc,desc,inventoryState);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
