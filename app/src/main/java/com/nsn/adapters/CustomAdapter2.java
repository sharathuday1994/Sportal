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

import com.nsn.sportal.FriendRequestView;
import com.nsn.sportal.HomeActivity;
import com.nsn.sportal.ListModel;
import com.nsn.sportal.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomAdapter2 extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data = null;
    private static LayoutInflater inflater=null;
    ListModel tempValues=null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter2(Activity a, ArrayList d) {

        /********** Take passed values **********/
        activity = a;
        data=d;


        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/

    public void setArrayList(ArrayList<ListModel> data){
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

        public TextView text_id;
        public TextView text_name;
        public TextView text_country;
        public TextView text_sport;


    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.custom_list, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text_name = (TextView) vi.findViewById(R.id.list_text_name);
            holder.text_country = (TextView) vi.findViewById(R.id.list_text_country);
            holder.text_sport = (TextView) vi.findViewById(R.id.list_text_sport);


            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text_name.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (ListModel) data.get( position );

            /************  Set Model values in Holder elements ***********/


            holder.text_name.setText(tempValues.getName());
            holder.text_country.setText( tempValues.getCountry() );
            holder.text_sport.setText(tempValues.getSport());

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


            FriendRequestView sct = (FriendRequestView)activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }
}
