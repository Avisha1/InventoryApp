package com.example.android.inventoryapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.InventoryContract;

import org.w3c.dom.Text;

/**
 * Created by avishai on 10/7/2017.
 */

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //TBD
        //implement this to perform click on view and notify the view parent
//        final ViewGroup view = parent;
//        LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.item_layout);
//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((ListView)view).performItemClick(v, position, 0);
//            }
//        });

        //Button btn = (Button)convertView.findViewById(R.id.bt)


        return super.getView(position, convertView, parent);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView tvName = (TextView)view.findViewById(R.id.name);
        TextView tvPrice = (TextView)view.findViewById(R.id.price);
        TextView tvQuantity = (TextView)view.findViewById(R.id.quantity);
//        Button btn = (Button) view.findViewById(R.id.item_sale_button);
//        btn.per(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "check button", Toast.LENGTH_SHORT).show();
//            }
//        });

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
