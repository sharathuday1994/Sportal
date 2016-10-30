package com.nsn.sportal;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

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

public class AchievementAdd extends AppCompatActivity {
    public EditText mTitle, mDate, mDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitle = (EditText) findViewById(R.id.title_achievement);
        mDate = (EditText) findViewById(R.id.date_achievement);
        mDesc = (EditText) findViewById(R.id.description_achievement);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_achievement);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAdd();
            }
        });
    }

    //Checking valid field entries
    public void attemptAdd(){
        String title = mTitle.getText().toString();
        String date = mDate.getText().toString();
        String desc = mDesc.getText().toString();
        View focusview = null;
        boolean cancel = false;

        mTitle.setError(null);
        mDate.setError(null);
        mDesc.setError(null);


        if(TextUtils.isEmpty(title)){
            cancel = true;
            focusview = mTitle;
            mTitle.setError(getString(R.string.error_field_required));
        }
        if(TextUtils.isEmpty(desc)){
            cancel = true;
            focusview = mDesc;
            mTitle.setError(getString(R.string.error_field_required));
        }

        if(cancel){
            focusview.requestFocus();
        }else{
            //Calling AsyncTask to add new achievement
            AchievementAddTask achievementAddTask = new AchievementAddTask(this);
            achievementAddTask.execute(title,date,desc);
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();

        }
    }


}
