package com.nsn.sportal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView.OnItemClickListener;

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


public class RegisterActivity extends AppCompatActivity implements OnItemClickListener,OnItemSelectedListener {

   // private UserRegisterTask mRegisterAuth = null;

    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mVerifyPasswordView;
    private EditText mDOB;
    private AutoCompleteTextView mCountry;
    private EditText mGender;
    private AutoCompleteTextView mSport;

    private View mRegisterFormView;
    private View mProgressView;

    private ArrayAdapter<String> adapterSport;
    private ArrayAdapter<String> adapterCountry;

    private String sportArray[]= new String[30];
    private String countryArray[]=new String[51];

    ContentValues sportpair = new ContentValues();
    ContentValues countrypair = new ContentValues();

    public String country_name;
    public String sport;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirstNameView = (EditText) findViewById(R.id.user_first_name);
        mLastNameView = (EditText) findViewById(R.id.user_last_name);
        mVerifyPasswordView = (EditText) findViewById(R.id.verify_password);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mDOB = (EditText) findViewById(R.id.user_dob);
        mCountry = (AutoCompleteTextView) findViewById(R.id.user_country);
        mGender = (EditText) findViewById(R.id.user_gender);
        mSport = (AutoCompleteTextView) findViewById(R.id.user_sport);

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });

        new SportFetch().execute();
        new CountryFetch().execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRegisterFormView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = RegisterActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private void attemptRegistration() {
        /*if (mRegisterAuth != null) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mLastNameView.setError(null);
        mFirstNameView.setError(null);
        mVerifyPasswordView.setError(null);
        mDOB.setError(null);
        mCountry.setError(null);
        mGender.setError(null);
        mSport.setError(null);

        // Store values at the time of the register attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String first_name = mFirstNameView.getText().toString();
        final String last_name = mLastNameView.getText().toString();
        String password_review = mVerifyPasswordView.getText().toString();
        final String date_of_birth = mDOB.getText().toString();
        final String userid = "U";
        final String gender = mGender.getText().toString();
        country_name = mCountry.getText().toString();
        country_name = countrypair.getAsString(country_name);
        sport = mSport.getText().toString();
        sport = sportpair.getAsString(sport);

        boolean cancel = false;
        View focusView = null;



        // Check for a valid first name
        if (TextUtils.isEmpty(first_name)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }
        //Check for valid last name
        if (TextUtils.isEmpty(last_name)) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        }
        //Check for valid country name
        if (TextUtils.isEmpty(country_name)) {
            mCountry.setError(getString(R.string.error_field_required));
            focusView = mCountry;
            cancel = true;
        }
        if (TextUtils.isEmpty(sport)) {
            mSport.setError(getString(R.string.error_field_required));
            focusView = mSport;
            cancel = true;
        }
        if(TextUtils.isEmpty(gender)){
            mGender.setError(getString(R.string.error_field_required));
            focusView = mGender;
            cancel = true;

            if(!(gender.equals("M")||gender.equals("F"))){
                mGender.setError(getString(R.string.error_invalid));
                focusView = mGender;
            }
        }
        //Check for valid date of birth
        if (TextUtils.isEmpty(date_of_birth)) {
            mDOB.setError(getString(R.string.error_field_required));
            focusView = mDOB;
            cancel = true;
        }
        //Check for valid email format
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        //Check for valid password
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (password.length() < 6) {
            mPasswordView.setError(getString(R.string.error_short_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        else if(!password_review.equals(password)) {
            mVerifyPasswordView.setError(getString(R.string.error_password_mismatch));
            focusView = mVerifyPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            RetrieveCount retrieveCount = new RetrieveCount("Register",this,first_name,last_name,email,password,userid,date_of_birth,gender,country_name,sport);
            retrieveCount.execute("centraldb");


            startActivity(new Intent(this, LoginActivity.class));

        }
    }

    //to check whether the email is valid or not
    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    //TO check whether the password entered is valid or not
    private boolean isPasswordValid(String password) {

        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String sym = "!@#$%^&*()_-+=,<.>/?':;";
        int test_alpha = 0, test_symbol = 0;
        Boolean flag = false;
        for (int i = 0; i < alphabets.length(); i++) {
            if (password.contains(alphabets.substring(i, i + 1)))
                test_alpha = 1;
        }
        for (int i = 0; i < sym.length(); i++) {
            if (password.contains(sym.substring(i, i + 1)))
                test_symbol = 1;
        }
        if ((test_alpha + test_symbol) == 2)
            flag = true;
        return flag;
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.user_country:
                country_name = countrypair.getAsString(parent.getItemAtPosition(position).toString());
                break;
            case R.id.user_sport:
                sport = sportpair.getAsString(parent.getItemAtPosition(position).toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (view.getId()){
            case R.id.user_country:
                country_name = countrypair.getAsString(parent.getItemAtPosition(position).toString());
                break;
            case R.id.user_sport:
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
                adapterSport = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_dropdown_item_1line,sportArray);
                mSport.setThreshold(1);

                //Set adapter to AutoCompleteTextView
                mSport.setAdapter(adapterSport);
                mSport.setOnItemSelectedListener(RegisterActivity.this);
                mSport.setOnItemClickListener(RegisterActivity.this);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public class CountryFetch extends AsyncTask<Void,Void,String>{

        String country_url = "http://10.0.2.2/Miniproject/country.php";
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection httpURLConnection=null;
            InputStream IS=null;
            int temp;
            String error = "";


            try
            {
                URL url = new URL(country_url);
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
                    countrypair.put(validate.getString("name"), validate.getString("code"));
                    countryArray[i-1]=validate.getString("name");
                }
                adapterCountry = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_dropdown_item_1line,countryArray);
                mCountry.setThreshold(1);

                //Set adapter to AutoCompleteTextView
                mCountry.setAdapter(adapterCountry);
                mCountry.setOnItemSelectedListener(RegisterActivity.this);
                mCountry.setOnItemClickListener(RegisterActivity.this);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
