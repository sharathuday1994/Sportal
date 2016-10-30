package com.nsn.sportal;

import android.annotation.TargetApi;
import android.app.LocalActivityManager;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

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


public class PortfolioActivity extends AppCompatActivity {

    TextView mUserName;
    TextView mDOB;
    TextView mSport;
    TextView mCountry;
    TextView mGender;
    Button bFriends;

    LocalActivityManager localActivityManager = new LocalActivityManager(this,false);
    public String portfolio_url = "http://10.0.2.2/Miniproject/portfolio.php";
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.portfolio_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        Intent intent = getIntent();
        final String user_id = intent.getStringExtra("userid");

        new PortfolioFill(user_id).execute();
        mUserName = (TextView) findViewById(R.id.user_full_name);
        mDOB = (TextView) findViewById(R.id.date_of_birth);
        mSport = (TextView) findViewById(R.id.sport_name);
        mCountry = (TextView) findViewById(R.id.country_name);
        mGender = (TextView) findViewById(R.id.gender_text);
        bFriends = (Button) findViewById(R.id.friendbutton);
        if(user_id.equals(LoginActivity.globalUserID)){
            ab.hide();
            bFriends.setVisibility(View.INVISIBLE);
        }
        else{
           new FriendDetect().execute(user_id);
        }
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        localActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(localActivityManager);

        TabHost.TabSpec tabSpecAchievement = tabHost.newTabSpec("Achievement");
        tabSpecAchievement.setContent(R.id.achievements);
        tabSpecAchievement.setIndicator("", getDrawable(R.drawable.achievement_small));
        tabHost.addTab(tabSpecAchievement);
        Intent intent1 = new Intent(this,AchievementActivity.class);
        intent1.putExtra("userid",user_id);
        tabSpecAchievement.setContent(intent1);

        TabHost.TabSpec tabSpecPhotos = tabHost.newTabSpec("Photos");
        tabSpecPhotos.setContent(R.id.photos);
        tabSpecPhotos.setIndicator("", getDrawable(R.drawable.ic_photo_camera_black_24dp));
        tabHost.addTab(tabSpecPhotos);

        TabHost.TabSpec tabSpecInventory = tabHost.newTabSpec("Inventory");
        tabSpecInventory.setContent(R.id.inventory);
        tabSpecInventory.setIndicator("", getDrawable(R.drawable.ic_local_grocery_store_black_24dp));
        tabHost.addTab(tabSpecInventory);
        Intent intent3 = new Intent(this,InventoryActivity.class);
        intent3.putExtra("userid",user_id);
        tabSpecInventory.setContent(intent3);

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddFriendTask().execute(bFriends.getText().toString(),user_id);
            }
        };
        bFriends.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_portfolio, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.portfolio_home:
                startActivity(new Intent(PortfolioActivity.this,HomeActivity.class));
                return true;
            case R.id.portfolio_logout:
                attemptLogout();
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    public class PortfolioFill extends AsyncTask<Void,Void,String>{


        String user_id;
        String first_name;
        String last_name;
        String emailid ;
        String gender;
        String dob;
        String sport;
        String country;
        String name;
        public PortfolioFill(String user_id) {
          this.user_id = user_id;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            String error = "";
            int temp;
            try {

                URL url = new URL(portfolio_url);
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
                parameters.put("userid", user_id);
                String data = getQuery(parameters);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                IS = httpURLConnection.getInputStream();

                while((temp=IS.read())!=-1){
                    error += (char) temp;
                }
                Thread.sleep(2000);
                return error;
            } catch (InterruptedException e) {
                error = "Sorry! Cannot Currently Render Service";
            }  catch (MalformedURLException e) {
                e.printStackTrace();
                error = "Sorry! Cannot Currently Render Service";
            } catch (IOException e) {
                e.printStackTrace();
                error = "Sorry! Cannot Currently Render Service";
            }finally {
                if (httpURLConnection != null)

                {
                    httpURLConnection.disconnect();
                }
                try {
                    if (IS != null) {
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
            if (success!=null) {

                if(!success.equals("Sorry! Cannot Currently Render Service")){
                    try {
                        JSONObject root = new JSONObject(success);
                        JSONObject user_data = root.getJSONObject("user_data");
                        first_name = user_data.getString("firstname");
                        last_name = user_data.getString("lastname");
                        emailid = user_data.getString("emailid");
                        gender = "Gender: " + user_data.getString("gender");
                        dob = "Birthday: " + user_data.getString("dob");
                        sport = "Sport: " + user_data.getString("sport");
                        country = "Country: " + user_data.getString("country");

                        name = "Name: " + first_name + " " + last_name;
                        mUserName.setText(name);
                        mGender.setText(gender);
                        mSport.setText(sport);
                        mCountry.setText(country);
                        mDOB.setText(dob);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
                else{
                    Toast.makeText(PortfolioActivity.this,success,Toast.LENGTH_LONG).show();
                }

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
    public void onPause(){
        super.onPause();
        localActivityManager.dispatchPause(isFinishing());
    }

    public void onResume(){
        super.onResume();
        localActivityManager.dispatchResume();
    }
    public class FriendDetect extends AsyncTask<String,Void,String>{
        private String user_id;
        private String friend_url = "http://10.0.2.2/Miniproject/friend.php";
        @Override
        protected String doInBackground(String... params) {
            user_id = params[0];
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            String error = "";
            int temp;
            try {

                URL url = new URL(friend_url);
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
                parameters.put("otheruser", user_id);
                parameters.put("self",LoginActivity.globalUserID);
                String data = getQuery(parameters);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                IS = httpURLConnection.getInputStream();

                while((temp=IS.read())!=-1){
                    error += (char) temp;
                }
                Thread.sleep(2000);
                return error;
            } catch (InterruptedException e) {
                error = "Sorry! Cannot Currently Render Service";
            }  catch (MalformedURLException e) {
                e.printStackTrace();
                error = "Sorry! Cannot Currently Render Service";
            } catch (IOException e) {
                e.printStackTrace();
                error = "Sorry! Cannot Currently Render Service";
            }finally {
                if (httpURLConnection != null)

                {
                    httpURLConnection.disconnect();
                }
                try {
                    if (IS != null) {
                        IS.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return error;
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject root = new JSONObject(result);
                String temp = root.getString("user_data");
                if(temp.equals("Friends")){
                    bFriends.setText(getString(R.string.friends));
                }else if(temp.equals("Request Sent")){
                    bFriends.setText(getString(R.string.requestsent));
                }else if(temp.equals("Approve Request")){
                    bFriends.setText(getString(R.string.approvefriend));
                }else if(temp.equals("Not Friends")){
                    bFriends.setText(getString(R.string.notfriend));
                }
                else
                {
                    bFriends.setText("ERROR 404");
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

    public class AddFriendTask extends AsyncTask<String,Void,String>{
        String buttonStatus;
        String friend_url;
        boolean attempt = true;
        String user_id;
        boolean successful = false;
        @Override
        protected String doInBackground(String... params) {
            buttonStatus = params[0];
            user_id = params[1];
            if(buttonStatus.equals(getString(R.string.notfriend)))
            {
               friend_url =  "http://10.0.2.2/Miniproject/friendAdd.php";
            }else if(buttonStatus.equals(getString(R.string.approvefriend))){
                friend_url = "http://10.0.2.2/Miniproject/friendApprove.php";
            }else if(buttonStatus.equals(getString(R.string.friends))){
                friend_url = "http://10.0.2.2/Miniproject/friendRemove.php";
            }else if(buttonStatus.equals(getString(R.string.requestsent))){
                friend_url = "http://10.0.2.2/Miniproject/friendCancel.php";
            }else{
                Toast.makeText(PortfolioActivity.this,"Internal Error, Please try again later",Toast.LENGTH_LONG).show();
                attempt = false;
            }

            if(attempt){
                HttpURLConnection httpURLConnection = null;
                InputStream IS = null;
                String error = "";
                int temp;
                try {

                    URL url = new URL(friend_url);
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
                    parameters.put("otheruser", user_id);
                    parameters.put("self",LoginActivity.globalUserID);
                    String data = getQuery(parameters);

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();

                    IS = httpURLConnection.getInputStream();

                    while((temp=IS.read())!=-1){
                        error += (char) temp;
                    }
                    Thread.sleep(2000);
                    successful = true;
                    return error;
                } catch (InterruptedException e) {
                    error = "Sorry! Cannot Currently Render Service";
                }  catch (MalformedURLException e) {
                    e.printStackTrace();
                    error = "Sorry! Cannot Currently Render Service";
                } catch (IOException e) {
                    e.printStackTrace();
                    error = "Sorry! Cannot Currently Render Service";
                }finally {
                    if (httpURLConnection != null)

                    {
                        httpURLConnection.disconnect();
                    }
                    try {
                        if (IS != null) {
                            IS.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return error;
            }
            return "";
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

        protected void onPostExecute(String result) {
            String status, message = result;
            try {
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                status = user_data.getString("status");

                if(successful){
                    message = user_data.getString("message");
                }

                if(status.equals("Success")){
                    if(buttonStatus.equals(getString(R.string.notfriend))){
                        bFriends.setText(getString(R.string.requestsent));
                    }else if(buttonStatus.equals(getString(R.string.approvefriend))){
                        bFriends.setText(getString(R.string.friends));
                    }else if(buttonStatus.equals(getString(R.string.friends))){
                        bFriends.setText(getString(R.string.notfriend));
                    }else if(buttonStatus.equals(getString(R.string.requestsent))) {
                        bFriends.setText(getString(R.string.notfriend));
                    }else{

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!TextUtils.isEmpty(result)) {
                Toast.makeText(PortfolioActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void attemptLogout(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
