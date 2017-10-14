package com.example.android.inventoryapp.listeners;

import android.view.View;

/**
 * click listener for whole row of list item and for a single item inside a row
 */
public interface CursorClickListener {

    void onClickRow(View view, int id);

    void onClickRowItem(View view, int id, int value);
}
