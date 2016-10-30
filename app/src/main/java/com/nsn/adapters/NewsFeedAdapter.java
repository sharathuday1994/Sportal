package com.nsn.adapters;

/**
 * Created by nihalpradeep on 24/06/16.
 */
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nsn.sportal.AchievementActivity;
import com.nsn.sportal.AchievementModel;
import com.nsn.sportal.HomeActivity;
import com.nsn.sportal.ListModel;
import com.nsn.sportal.NewsFeed;
import com.nsn.sportal.NewsFeedModel;
import com.nsn.sportal.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class NewsFeedAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data = null;
    private static LayoutInflater inflater=null;
    NewsFeedModel tempValues=null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public NewsFeedAdapter(Activity a, ArrayList d) {

        /********** Take passed values **********/
        activity = a;
        data=d;


        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/

    public void setArrayList(ArrayList<NewsFeedModel> data){
        this.data = data;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView text_type;
        public TextView text_message;



    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.custom_list_newsfeed, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text_message = (TextView) vi.findViewById(R.id.list_newsfeed_display);
            holder.text_type = (TextView) vi.findViewById(R.id.list_newsfeed_type);



            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text_message.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (NewsFeedModel) data.get( position );

            /************  Set Model values in Holder elements ***********/



            holder.text_message.setText(tempValues.getDisplay());
            holder.text_type.setText(tempValues.getType());


            /******** Set Item Click Listner for LayoutInflater for each row *******/

            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            NewsFeed sct = (NewsFeed)activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }
}
