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

import com.nsn.sportal.EventActivity;
import com.nsn.sportal.EventModel;
import com.nsn.sportal.HomeActivity;
import com.nsn.sportal.ListModel;
import com.nsn.sportal.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class EventAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data = null;
    private static LayoutInflater inflater=null;
    EventModel tempValues=null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public EventAdapter(Activity a, ArrayList d) {

        /********** Take passed values **********/
        activity = a;
        data=d;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        /***********  Layout inflator to call external xml layout () ***********/


    }

    /******** What is the size of Passed Arraylist Size ************/

    public void setArrayList(ArrayList<EventModel> data){
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

        public TextView text_user_name;
        public TextView text_event_name;
        public TextView text_country;
        public TextView text_sport;
        public TextView text_date;



    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.custom_list_events, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text_user_name = (TextView) vi.findViewById(R.id.list_event_user);
            holder.text_country = (TextView) vi.findViewById(R.id.list_event_country);
            holder.text_sport = (TextView) vi.findViewById(R.id.list_event_sport);
            holder.text_date = (TextView) vi.findViewById(R.id.list_event_date);
            holder.text_event_name = (TextView) vi.findViewById(R.id.list_event_name);


            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text_event_name.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (EventModel) data.get( position );

            /************  Set Model values in Holder elements ***********/


            holder.text_event_name.setText(tempValues.getEventName());
            holder.text_sport.setText(tempValues.getEventSport());
            holder.text_user_name.setText(tempValues.getEventUser());
            holder.text_date.setText(tempValues.getEventDate());
            String temp = tempValues.getEventCountry();
            int i = 0;
            while(i<temp.length()){
                if(temp.charAt(i)==','){break;}
                i++;
            }
            temp = temp.substring(0,i-1);
            holder.text_country.setText(temp);


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


            EventActivity sct = (EventActivity)activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }
}
