package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryContract;

import org.w3c.dom.Text;

/**
 * Created by avishai on 10/7/2017.
 */

public class InventoryCursorAdapter extends CursorAdapter {

    public InventoryCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView tvName = (TextView)view.findViewById(R.id.name);
        TextView tvPrice = (TextView)view.findViewById(R.id.price);
        TextView tvQuantity = (TextView)view.findViewById(R.id.quantity);

        // Extract properties from cursor
        String itemName = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_NAME));
        String itemPrice = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRICE));
        String itemQuantity = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_QUANTITY));

        // Populate fields with extracted properties
        tvName.setText(itemName);
        tvPrice.setText(itemPrice);
        tvQuantity.setText(itemQuantity);
    }
}
