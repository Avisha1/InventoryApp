package com.example.android.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by avishai on 10/6/2017.
 */

public class InventoryContract {

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.inventoryapp/items/ is a valid path for
     * looking at item data. content://com.example.android.inventoryapp/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_INVENTORY = "items";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String TEXT_TYPE          = " TEXT";
    private static final String INT_TYPE          = " INTEGER";
    private static final String BLOB_TYPE          = " BLOB";
    private static final String NOTNULL_CONSTRAINT = " NOT NULL";
    private static final String COMMA_SEP          = ",";

    private InventoryContract(){}

    public static final class InventoryEntry implements BaseColumns{

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /** The content URI to access the item data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        /** Name of database table for inventory */
        public final static String TABLE_NAME = "inventory";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME ="name";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_ORDER_CONTACT_EMAIL = "order_contact_email";
        public static final String COLUMN_PICTURE = "picture";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + INT_TYPE + " PRIMARY KEY " + " AUTOINCREMENT " + COMMA_SEP +
                COLUMN_NAME + TEXT_TYPE + NOTNULL_CONSTRAINT + COMMA_SEP +
                COLUMN_PRICE + INT_TYPE + NOTNULL_CONSTRAINT + COMMA_SEP +
                COLUMN_QUANTITY + INT_TYPE + NOTNULL_CONSTRAINT + COMMA_SEP +
                COLUMN_ORDER_CONTACT_EMAIL + TEXT_TYPE + NOTNULL_CONSTRAINT + COMMA_SEP +
                COLUMN_PICTURE + BLOB_TYPE + NOTNULL_CONSTRAINT + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        /**
         * sanity check for the price parameter
         * @param price
         * @return
         */
        public static boolean isPriceValid(int price) {
            return price >= 0;
        }

        /**
         * sanity check for the quantity parameter
         * @param quantity
         * @return
         */
        public static boolean isQuantityValid(int quantity) {
            return quantity >= 0;
        }
    }
}
