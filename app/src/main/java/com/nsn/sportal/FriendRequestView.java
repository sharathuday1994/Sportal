package com.nsn.sportal;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsn.adapters.CustomAdapter2;

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

public class FriendRequestView extends AppCompatActivity {
    ArrayList<ListModel> myArrayList;
    FriendRequestView friendRequestView = null;
    CustomAdapter2 customAdapter;
    ListView listView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myArrayList = new ArrayList<ListModel>();
        friendRequestView = this;
        listView = (ListView) findViewById(R.id.friend_request_list);
        listView.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.friend_request_default_text);
        customAdapter = new CustomAdapter2(friendRequestView,myArrayList);
        new RequestFetch().execute();

    }

    public void onItemClick(int mPosition) {
        ListModel tempValues = myArrayList.get(mPosition);
        String userid = tempValues.getId();
        Intent tempIntent = new Intent(this,PortfolioActivity.class);
        tempIntent.putExtra("userid",userid);
        startActivity(tempIntent);
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
                startActivity(new Intent(FriendRequestView.this,HomeActivity.class));
                return true;
            case R.id.portfolio_logout:
                attemptLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void attemptLogout(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public class RequestFetch extends AsyncTask<Void,Void,String>{

        private String request_url = "http://10.0.2.2/Miniproject/friendRequest.php";
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            String error = "";
            int temp;
            try {

                URL url = new URL(request_url);
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
            try {
                int count;
                JSONObject retrieve;
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                JSONObject check = user_data.getJSONObject("0");
                if(check.getString("validate").equals("Successful")) {
                    count = Integer.parseInt(check.getString("count"));
                    if(count==0)
                    {
                        textView.setText("No Entries Found");
                        textView.setVisibility(View.VISIBLE);
                        //Toast.makeText(HomeActivity.this,"No Results Found",Toast.LENGTH_LONG).show();
                    }

                    else{
                        for(int i = 1;i<=count;i++){
                            retrieve = user_data.getJSONObject(""+i);
                            ListModel listModel = new ListModel();
                            listModel.setName(retrieve.getString("firstname") + " " + retrieve.getString("lastname"));
                            listModel.setId(retrieve.getString("userid"));
                            listModel.setCountry(retrieve.getString("country"));
                            listModel.setSport(retrieve.getString("sport"));
                            myArrayList.add(listModel);
                        }
                        customAdapter.setActivity(friendRequestView);
                        customAdapter.setArrayList(myArrayList);
                        listView.setAdapter(customAdapter);
                        textView.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                    }


                }
                else
                {
                    Toast.makeText(FriendRequestView.this, "Sorry. Something went wrong", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
