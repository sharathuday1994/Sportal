package com.nsn.sportal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nsn.adapters.EventAdapter;
import com.nsn.adapters.ListViewAdapter;

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
import java.util.List;
import java.util.Map;

public class EventActivity extends AppCompatActivity {

    ListView eventList;
    ArrayList<EventModel> myArrayList;
    EventActivity eventActivity = null;
    TextView textView;
    EventAdapter eventAdapter;
    LinearLayout linearLayoutList,linearLayoutText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);
        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.eventadd);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this,EventAdd.class);
                startActivity(intent);
            }
        });

        myArrayList = new ArrayList<EventModel>();
        linearLayoutText = (LinearLayout) findViewById(R.id.layout_text);
        linearLayoutList = (LinearLayout) findViewById(R.id.layout_list);
        linearLayoutList.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.event_text);
        eventList = (ListView) findViewById(R.id.event_list);
        eventList.setVisibility(View.INVISIBLE);
        eventActivity = this;


        new EventListCreate().execute();

        eventList.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                floatingActionButton.setVisibility(View.INVISIBLE);
                return false;

            }
        });


    }

    /*private ArrayList<Model> generateData() {
        ArrayList<Model> models = new ArrayList<Model>();
        models.add(new Model("Group Title"));
        models.add(new Model("Menu Item 1", "1"));
        models.add(new Model("Menu Item 2", "2"));
        models.add(new Model("Menu Item 3", "12"));

        return models;
    }*/

    public void onItemClick(int mPosition) {
        EventModel tempValues = myArrayList.get(mPosition);
        Intent intent = new Intent(EventActivity.this,EventView.class);
        intent.putExtra("eventid",tempValues.getEventID());
        intent.putExtra("userid",tempValues.getEventUserID());
        startActivity(intent);
    }
    public class EventListCreate extends AsyncTask<Void,Void,String>{
        private String event_url = "http://10.0.2.2/Miniproject/eventlist.php";
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            String error = "";
            int temp;
            try {

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
                String wait = "testing";
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
            int count;
            String validate;
            try {
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                JSONObject temp = user_data.getJSONObject("0");
                validate = temp.getString("success");
                if(validate.equals("Successful")) {
                    count = Integer.parseInt(temp.getString("count"));
                    for(int i=1;i<=count;i++){
                        temp = user_data.getJSONObject(""+i);
                        EventModel eventModel = new EventModel();
                        eventModel.setEventID(temp.getString("eid"));
                        eventModel.setEventUserID(temp.getString("uid"));
                        eventModel.setEventDate(temp.getString("date"));
                        eventModel.setEventName(temp.getString("eventname"));
                        eventModel.setEventUser(temp.getString("username"));
                        eventModel.setEventSport(temp.getString("sport"));
                        eventModel.setEventCountry(temp.getString("location"));
                        myArrayList.add(eventModel);
                    }

                    eventAdapter = new EventAdapter(eventActivity,myArrayList);
                    eventAdapter.setActivity(eventActivity);
                    eventAdapter.setArrayList(myArrayList);
                    eventList.setAdapter(eventAdapter);
                    linearLayoutText.setVisibility(View.INVISIBLE);
                    linearLayoutList.setVisibility(View.VISIBLE);
                    eventList.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
