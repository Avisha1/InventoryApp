package com.example.android.inventoryapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.android.inventoryapp.listeners.CursorClickListener;

/**
 * Created by avishai on 10/7/2017.
 */

public class ItemCursorAdapter extends CursorAdapter {

    private CursorClickListener listener;
    public ItemCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
        listener = (CursorClickListener)context;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView tvName = (TextView)view.findViewById(R.id.name);
        TextView tvPrice = (TextView)view.findViewById(R.id.price);
        TextView tvQuantity = (TextView)view.findViewById(R.id.quantity);

        // Extract properties from cursor
        String itemName = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_NAME));
        String itemPrice = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_PRICE));
        final String itemQuantity = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_QUANTITY));
        final int item_id = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryEntry._ID));

        // Populate fields with extracted properties
        tvName.setText(itemName);
        tvPrice.setText(itemPrice);
        tvQuantity.setText(itemQuantity);

        Button saleButton = (Button)view.findViewById(R.id.item_sale_button);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickRowItem(v, item_id, Integer.valueOf(itemQuantity));
            }
        });

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.item_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickRow(v, item_id);
            }
        });
    }
}
