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
import com.nsn.sportal.InventoryActivity;
import com.nsn.sportal.InventoryModel;
import com.nsn.sportal.ListModel;
import com.nsn.sportal.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class InventoryAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data = null;
    private static LayoutInflater inflater=null;
    InventoryModel tempValues=null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public InventoryAdapter(Activity a, ArrayList d) {

        /********** Take passed values **********/
        activity = a;
        data=d;


        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/

    public void setArrayList(ArrayList<InventoryModel> data){
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
        public TextView text_sport;
        public TextView text_quantity;


    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.custom_list_inventory, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text_name = (TextView) vi.findViewById(R.id.list_inventory_name);
            holder.text_id = (TextView) vi.findViewById(R.id.list_inventory_no);
            holder.text_sport = (TextView) vi.findViewById(R.id.list_inventory_sport);
            holder.text_quantity = (TextView) vi.findViewById(R.id.list_inventory_quantity);


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
            tempValues = (InventoryModel) data.get( position );

            /************  Set Model values in Holder elements ***********/

            String temp = "" + tempValues.getSlno();

            holder.text_name.setText(tempValues.getName());
            holder.text_sport.setText( tempValues.getSport() );
            holder.text_id.setText(temp);
            temp = "" + tempValues.getQuantity();
            holder.text_quantity.setText(temp);

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


            InventoryActivity sct = (InventoryActivity)activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }
}
