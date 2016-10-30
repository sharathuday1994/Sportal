package com.nsn.sportal;


import android.annotation.TargetApi;
import android.app.LocalActivityManager;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nsn.adapters.CustomAdapter;

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

public class HomeActivity extends AppCompatActivity {

    SearchView searchView;
    private final UserProfile mUserProfile = new UserProfile();
    ViewPager viewPager;
    LocalActivityManager localActivityManager = new LocalActivityManager(this,false);
    LinearLayout homeLayout,searchLayout;
    HomeActivity homeActivity = null;
    ArrayList<ListModel> myArrayList = new ArrayList<ListModel>();
    ListView list;
    CustomAdapter customAdapter;
    TextView textView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       /* viewPager = (ViewPager) findViewById(R.id.view_pager);
        List<android.support.v4.app.Fragment> listFragments = new ArrayList<>();
        listFragments.add(new Fragment1());
        listFragments.add(new Fragment2());
        listFragments.add(new Fragment3());

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),listFragments);
        viewPager.setAdapter(myFragmentPagerAdapter);
*/
        homeLayout = (LinearLayout) findViewById(R.id.main_home_layout);
        searchLayout = (LinearLayout) findViewById(R.id.search_home_layout);
        searchLayout.setVisibility(View.INVISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Sportal");
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        localActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(localActivityManager);

        TabHost.TabSpec tabSpecHome = tabHost.newTabSpec("Home");
        tabSpecHome.setContent(R.id.home);
        tabSpecHome.setIndicator("", getDrawable(R.drawable.ic_home_black_36dp));
        tabHost.addTab(tabSpecHome);
        tabSpecHome.setContent(new Intent(this, NewsFeed.class));

        TabHost.TabSpec tabSpecPortfolio = tabHost.newTabSpec("Portfolio");
        tabSpecPortfolio.setContent(R.id.portfolio);
        tabSpecPortfolio.setIndicator("", getDrawable(R.drawable.ic_person_black_36dp));
        tabHost.addTab(tabSpecPortfolio);
        Intent intent = new Intent(this, PortfolioActivity.class);
        intent.putExtra("userid", LoginActivity.globalUserID);
        tabSpecPortfolio.setContent(intent);

        TabHost.TabSpec tabSpecEvents = tabHost.newTabSpec("Events");
        tabSpecEvents.setContent(R.id.events);
        tabSpecEvents.setIndicator("", getDrawable(R.drawable.ic_event_black_36dp));
        tabHost.addTab(tabSpecEvents);
        tabSpecEvents.setContent(new Intent(this, EventActivity.class));

        homeActivity = this;
        myArrayList.clear();
        customAdapter = new CustomAdapter(homeActivity,myArrayList);
        list = (ListView) findViewById(R.id.home_search_list);
        list.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.home_search_text);
        textView.setVisibility(View.INVISIBLE);


        //FragmentManager fragmentManager = getFragmentManager();
        // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.add(R.id.tabcontent,)
        // fragmentTransaction.commit();



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItemCompat.OnActionExpandListener onActionExpandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                homeLayout.setVisibility(View.INVISIBLE);
                searchLayout.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchLayout.setVisibility(View.INVISIBLE);
                list.setVisibility(View.INVISIBLE);
                homeLayout.setVisibility(View.VISIBLE);
                return true;
            }
        };
        MenuItem item = menu.findItem(R.id.home_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        MenuItemCompat.setOnActionExpandListener(item, onActionExpandListener);


        SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                list.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                myArrayList.clear();
                if(!TextUtils.isEmpty(query)) {
                    new UserSearchTask().execute(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                list.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                myArrayList.clear();
                if(!TextUtils.isEmpty(newText)) {
                    new UserSearchTask().execute(newText);
                }
                return true;
            }
        };
        searchView.setOnQueryTextListener(onQueryTextListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_logout:
                attemptLogout();
                return true;
            case R.id.home_friend_requests:
                startActivity(new Intent(HomeActivity.this,FriendRequestView.class));
                return true;
            default:
                super.onOptionsItemSelected(item);
                return true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.nsn.sportal/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.nsn.sportal/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    public void onPause(){
        super.onPause();
        localActivityManager.dispatchPause(isFinishing());

    }
    public void onResume(){
        super.onResume();
        localActivityManager.dispatchResume();
    }

    public void onItemClick(int mPosition)
    {
        ListModel tempValues = myArrayList.get(mPosition);
        String userid = tempValues.getId();

        if(userid.equals(LoginActivity.globalUserID)){
            searchLayout.setVisibility(View.INVISIBLE);
            homeLayout.setVisibility(View.VISIBLE);
            searchView.clearFocus();
        }
        else{
            Intent tempIntent = new Intent(this,PortfolioActivity.class);
            tempIntent.putExtra("userid",userid);
            startActivity(tempIntent);

        }



        // SHOW ALERT

        //Toast.makeText(homeActivity, "" + tempValues.getId() + " " + tempValues.getName(), Toast.LENGTH_LONG).show();
    }

    public class UserSearchTask extends AsyncTask<String,Void,String>{
        private String search_url = "http://10.0.2.2/Miniproject/search.php";

        String searchKey;
        @Override
        protected String doInBackground(String... params) {
            searchKey = params[0];

            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            String error = "";
            int temp;
            try {

                URL url = new URL(search_url);
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
                parameters.put("search",searchKey);
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
                        customAdapter.setActivity(homeActivity);
                        customAdapter.setArrayList(myArrayList);
                        list.setAdapter(customAdapter);
                        list.setVisibility(View.VISIBLE);


                    }


                }
                else
                {
                    Toast.makeText(HomeActivity.this,"Sorry. Something went wrong",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void attemptLogout(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
