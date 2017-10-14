package com.example.android.inventoryapp.utils;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by avishai on 10/14/2017.
 */

public final class ValidationUtils {

    private ValidationUtils() {}

    public static void editTextIncrease(EditText edt) {
        String value = edt.getText().toString();

        if (!TextUtils.isEmpty(value)) {
            int valAsInt = Integer.valueOf(value);
            edt.setText(String.valueOf(++valAsInt));
        } else {
            edt.setText("1");
        }
    }

    public static void editTextDecrease(EditText edt) {
        String value = edt.getText().toString();

        if (!TextUtils.isEmpty(value)) {
            int valAsInt = Integer.valueOf(value);
            if (valAsInt != 0) {
                edt.setText(String.valueOf(--valAsInt));
            }
        } else {
            edt.setText("0");
        }
    }

    public static Boolean isItemValidBeforeSave(EditText edtName,
                                                EditText edtContactName, EditText edtPrice,
                                                EditText edtQuantity,
                                                ImageView image) {

        return !(TextUtils.isEmpty(edtName.getText().toString()) ||
                TextUtils.isEmpty(edtContactName.getText().toString()) ||
                TextUtils.isEmpty(edtPrice.getText().toString()) ||
                TextUtils.isEmpty(edtQuantity.getText().toString()) ||
                image.getDrawable() == null
                );
    }
}
