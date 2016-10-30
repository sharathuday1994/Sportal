package com.nsn.sportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class InventoryAdd extends AppCompatActivity {

    EditText eItemName,eItemSport;
    //TextView tItemName,tItemSport,tItemQuantity;
    NumberPicker nItemQuantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_add);
        eItemName = (EditText) findViewById(R.id.name_inventory);
        eItemSport = (EditText) findViewById(R.id.sport_inventory);
        nItemQuantity = (NumberPicker) findViewById(R.id.item_quantity_inventory);
        nItemQuantity.setMinValue(1);
        nItemQuantity.setMaxValue(20);
        nItemQuantity.setWrapSelectorWheel(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_inventory);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAdd();
            }
        });
    }

    private void attemptAdd() {
        eItemName.setError(null);
        eItemSport.setError(null);
        View focusView = null;
        boolean cancel = false;

        String title = eItemName.getText().toString();
        String sport = eItemSport.getText().toString();
        String quantity = "" + nItemQuantity.getValue();

        if(TextUtils.isEmpty(title)){
            focusView = eItemName;
            cancel = true;
            eItemName.setError(getString(R.string.error_field_required));
        }
        if(TextUtils.isEmpty(sport)){
            focusView = eItemSport;
            cancel = true;
            eItemSport.setError(getString(R.string.error_field_required));
        }

        if(cancel){
            focusView.requestFocus();
        }else{
            //Async Task to store values in Database
            InventoryAddTask inventoryAddTask = new InventoryAddTask(this);
            inventoryAddTask.execute(title,sport,quantity);
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        }

    }

}
