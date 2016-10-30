package com.nsn.sportal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class EventAdd extends AppCompatActivity implements OnItemClickListener,OnItemSelectedListener{
    EditText mename, medate, medesc, meloc;
    AutoCompleteTextView mesport;
    CheckBox allowInventory;
    ContentValues sportpair = new ContentValues();

    String sportArray[] = new String[30];
    ArrayAdapter adapterSport;

    public String sport;

    private View eventForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mename = (EditText) findViewById(R.id.ename);
        medate = (EditText) findViewById(R.id.edate);
        mesport = (AutoCompleteTextView) findViewById(R.id.esport);
        medesc = (EditText) findViewById(R.id.edesc);
        meloc = (EditText) findViewById(R.id.elocation);
        allowInventory = (CheckBox) findViewById(R.id.allowInventory);
        allowInventory.setChecked(false);

        eventForm = (LinearLayout) findViewById(R.id.eventadd_form);
        eventForm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = EventAdd.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.eventcreate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreation();
            }
        });
        allowInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state = allowInventory.isChecked();
                state = !state;
                allowInventory.setChecked(!state);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new SportFetch().execute();
    }

    private void attemptCreation() {
        mename.setError(null);
        medesc.setError(null);
        medate.setError(null);
        mesport.setError(null);
        meloc.setError(null);

        boolean cancel = false;
        View focusView = null;
        int inventoryState = 0;

        String name = mename.getText().toString();
        String date = medate.getText().toString();
        String desc = medesc.getText().toString();
        String loc = meloc.getText().toString();
        sport = mesport.getText().toString();
        sport = sportpair.getAsString(sport);
        String eventid = "E";

        if(allowInventory.isEnabled()){
            inventoryState = 1;
        }

        if(TextUtils.isEmpty(name)){
            mename.setError(getString(R.string.error_field_required));
            cancel = true;
            focusView = mename;

        }
        if(TextUtils.isEmpty(date)){
            medate.setError(getString(R.string.error_field_required));
            cancel = true;
            focusView = medate;

        }
        if(TextUtils.isEmpty(sport)){
            mesport.setError(getString(R.string.error_field_required));
            cancel = true;
            focusView = mesport;

        }
        if(TextUtils.isEmpty(loc)){
            meloc.setError(getString(R.string.error_field_required));
            cancel = true;
            focusView = meloc;

        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else{

            RetrieveCount retrieveCount = new RetrieveCount("Event",this,eventid,name,date,sport,loc,desc,inventoryState);
            retrieveCount.execute("events");
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();


        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.esport:
                sport = sportpair.getAsString(parent.getItemAtPosition(position).toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (view.getId()){
            case R.id.esport:
                sport = sportpair.getAsString(parent.getItemAtPosition(position).toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

    public class SportFetch extends AsyncTask<Void,Void,String>{

        String sport_url = "http://10.0.2.2/Miniproject/sport.php";
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection httpURLConnection=null;
            InputStream IS=null;
            int temp;
            String error = "";


            try
            {
                URL url = new URL(sport_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);

                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


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
        protected void onPostExecute(String result) {
            int count;
            try {
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                JSONObject validate = user_data.getJSONObject("0");
                count = Integer.parseInt(validate.getString("count"));
                for(int i=1;i<=count;i++){
                    validate = user_data.getJSONObject(""+i);
                    sportpair.put(validate.getString("name"), validate.getString("code"));
                    sportArray[i-1]=validate.getString("name");
                }
                adapterSport = new ArrayAdapter<String>(EventAdd.this, android.R.layout.simple_dropdown_item_1line,sportArray);
                mesport.setThreshold(1);

                //Set adapter to AutoCompleteTextView
                mesport.setAdapter(adapterSport);
                mesport.setOnItemSelectedListener(EventAdd.this);
                mesport.setOnItemClickListener(EventAdd.this);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
