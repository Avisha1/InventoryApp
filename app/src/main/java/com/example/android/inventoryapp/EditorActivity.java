package com.example.android.inventoryapp;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_ITEM_LOADER = 0;
    private static final int RESULT_LOAD_IMAGE = 403;

    private Uri mCurrentItemUri;

    private boolean mItemHasChanged = false;

    @BindView(R.id.editor_picture)
    ImageView mItemPictureImageView;
    @BindView(R.id.editor_increase_price)
    Button mIncreasePriceButton;
    @BindView(R.id.editor_decrease_price)
    Button mDecreasePriceButton;
    @BindView(R.id.editor_increase_quantity)
    Button mIncreaseQuantityButton;
    @BindView(R.id.editor_decrease_quantity)
    Button mDecreaseQuantityButton;
    @BindView(R.id.editor_contact)
    Button mContactButton;
    @BindView(R.id.editor_save)
    Button mItemSaveButton;

    @BindView(R.id.editor_item_name)
    EditText mItemName;
    @BindView(R.id.editor_item_price)
    EditText mItemPrice;
    @BindView(R.id.editor_item_quantity)
    EditText mItemQuantity;
    @BindView(R.id.editor_item_contact)
    EditText mItemContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        if (mCurrentItemUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_item));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_item));

            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
        }

        setButtonListeners();
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    /**
     * set button click listeners
     */
    private void setButtonListeners() {

        //Set touch listeners
        mItemPictureImageView.setOnTouchListener(mTouchListener);
        mIncreasePriceButton.setOnTouchListener(mTouchListener);
        mDecreasePriceButton.setOnTouchListener(mTouchListener);
        mIncreaseQuantityButton.setOnTouchListener(mTouchListener);
        mDecreaseQuantityButton.setOnTouchListener(mTouchListener);
        mItemName.setOnTouchListener(mTouchListener);
        mItemPrice.setOnTouchListener(mTouchListener);
        mItemQuantity.setOnTouchListener(mTouchListener);
        mItemContact.setOnTouchListener(mTouchListener);

        //set button listeners
        mItemPictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        mIncreasePriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String priceAsString = mItemPrice.getText().toString();
                int price = Integer.valueOf(priceAsString);

                mItemPrice.setText(String.valueOf(++price));
            }
        });

        mDecreasePriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String priceAsString = mItemPrice.getText().toString();
                int price = Integer.valueOf(priceAsString);

                if (price == 0)
                    return;

                mItemPrice.setText(String.valueOf(--price));
            }
        });

        mIncreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String priceAsString = mItemQuantity.getText().toString();
                int price = Integer.valueOf(priceAsString);
                mItemQuantity.setText(String.valueOf(++price));
            }
        });

        mDecreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String priceAsString = mItemQuantity.getText().toString();
                int price = Integer.valueOf(priceAsString);

                if (price == 0)
                    return;

                mItemQuantity.setText(String.valueOf(--price));
            }
        });

        mContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = mItemContact.getText().toString();
                showEmailContact(contact);
            }
        });

        mItemSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mItemPictureImageView.setImageBitmap(bmp);
        }
    }

    private Bitmap getBitmapFromUri(@NonNull Uri uri) throws IOException {
        //checkNotNull(uri);
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /**
     * open an external email app to send an email to the given email address
     *
     * @param address
     */
    public void showEmailContact(String address) {

        if (!TextUtils.isEmpty(address)) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_text_subject));
            i.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text_body) + " " + mItemName.getText().toString());
            try {
                startActivity(Intent.createChooser(i, getString(R.string.email_choose_text)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new item, hide the "Delete" menu item.
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_item);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_delete_item:

                return true;

            case android.R.id.home:
                // If the item hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the item.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void saveItem() {

        String itemName = mItemName.getText().toString();
        String itemContact = mItemContact.getText().toString();
        int itemPrice = Integer.valueOf(mItemPrice.getText().toString());
        int itemQuantity = Integer.valueOf(mItemQuantity.getText().toString());

        //TBD
        //validate all values
        if(TextUtils.isEmpty(itemName) || TextUtils.isEmpty(itemContact)){

            //put relevant message
            return;
        }

        //get the byte array out of the drawable
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) mItemPictureImageView.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        ContentValues cv = new ContentValues();
        cv.put(InventoryEntry.COLUMN_NAME, itemName);
        cv.put(InventoryEntry.COLUMN_ORDER_CONTACT_EMAIL, itemContact);
        cv.put(InventoryEntry.COLUMN_QUANTITY, itemQuantity);
        cv.put(InventoryEntry.COLUMN_PRICE, itemPrice);
        cv.put(InventoryEntry.COLUMN_PICTURE, stream.toByteArray());


        if (mCurrentItemUri == null) {

            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, cv);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_error_save_message),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, R.string.editor_success_save_message,
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentItemUri, cv, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, R.string.editor_error_update_message,
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, R.string.editor_success_update_message,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_PICTURE,
                InventoryEntry.COLUMN_ORDER_CONTACT_EMAIL};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentItemUri,         // Query the content URI for the current item
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()) {
            int itemNameColIndex = data.getColumnIndex(InventoryEntry.COLUMN_NAME);
            int itemPriceColIndex = data.getColumnIndex(InventoryEntry.COLUMN_PRICE);
            int itemQuantityColIndex = data.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
            int itemImageColIndex = data.getColumnIndex(InventoryEntry.COLUMN_PICTURE);
            int itemContactColIndex = data.getColumnIndex(InventoryEntry.COLUMN_ORDER_CONTACT_EMAIL);

            int price = data.getInt(itemPriceColIndex);
            int quantity = data.getInt(itemQuantityColIndex);
            String name = data.getString(itemNameColIndex);
            String contact = data.getString(itemContactColIndex);

            byte[] imageAsByte = data.getBlob(itemImageColIndex);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);
            Drawable drawable = new BitmapDrawable(getResources(),
                    bitmap);

            mItemName.setText(name);
            mItemPrice.setText(Integer.toString(price));
            mItemQuantity.setText(Integer.toString(quantity));
            mItemContact.setText(contact);
            mItemPictureImageView.setImageDrawable(drawable);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mItemName.setText("");
        mItemPrice.setText("");
        mItemQuantity.setText("");
        mItemContact.setText("");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher);
        Drawable drawable = new BitmapDrawable(getResources(),
                bitmap);
        mItemPictureImageView.setImageDrawable(drawable);
    }
}
