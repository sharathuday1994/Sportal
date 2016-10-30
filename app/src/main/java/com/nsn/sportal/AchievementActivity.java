package com.nsn.sportal;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsn.adapters.AchievementAdapter;

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
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

public class AchievementActivity extends AppCompatActivity {


    String user_id;
    ArrayList<AchievementModel> myArrayList;
    AchievementAdapter achievementAdapter;
    AchievementActivity achievementActivity = null;
    TextView textView;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        Intent i = getIntent();
        user_id = i.getStringExtra("userid");
        myArrayList = new ArrayList<AchievementModel>();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_achievement);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AchievementActivity.this,AchievementAdd.class);
                startActivity(intent);
            }
        });
        achievementActivity = this;
        textView = (TextView) findViewById(R.id.achievements_default_text);
        listView = (ListView) findViewById(R.id.achievements_list);
        listView.setVisibility(View.INVISIBLE);
        achievementAdapter = new AchievementAdapter(achievementActivity,myArrayList);

        new GetAchievement().execute();

    }

    public void onItemClick(int mPosition) {
        AchievementModel tempValues = myArrayList.get(mPosition);
        Toast.makeText(AchievementActivity.this,"Achievement No:" + tempValues.getId() + "  " + tempValues.getDesc(),Toast.LENGTH_LONG).show();
    }

    public class GetAchievement extends AsyncTask<Void,Void,String>{
        String achievement_url = "http://10.0.2.2/Miniproject/achievementRetrieve.php";

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            String error = "";
            int temp;
            try {

                URL url = new URL(achievement_url);
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
                parameters.put("userid",user_id);
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
        protected void onPostExecute(String result) {
            String validate;
            int count;
            try {
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                JSONObject temp = user_data.getJSONObject("0");
                validate = temp.getString("validate");
                if(validate.equals("Successful")){
                    count = Integer.parseInt(temp.getString("count"));
                    for(int i=1;i<=count;i++){
                        temp = user_data.getJSONObject(""+i);
                        AchievementModel achievementModel = new AchievementModel();
                        achievementModel.setDate(temp.getString("date"));
                        achievementModel.setId(Integer.parseInt(temp.getString("slno")));
                        achievementModel.setName(temp.getString("name"));
                        achievementModel.setDesc(temp.getString("desc"));
                        myArrayList.add(achievementModel);
                    }
                    achievementAdapter.setActivity(achievementActivity);
                    achievementAdapter.setArrayList(myArrayList);
                    textView.setVisibility(View.INVISIBLE);
                    listView.setAdapter(achievementAdapter);
                    listView.setVisibility(View.VISIBLE);

                }else {
                    Toast.makeText(AchievementActivity.this,"Add Achievements To Highlight Your Profile",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
