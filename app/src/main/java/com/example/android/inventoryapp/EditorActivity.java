package com.example.android.inventoryapp;


import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_ITEM_LOADER = 0;

    private ImageView mItemPictureImageView;
    private TextView mItemNameTextView;
    private TextView mItemPriceTextView;
    private TextView mItemQuantityTextView;
    private Button mIncreasePriceButton;
    private Button mDecreasePriceButton;
    private Button mIncreaseQuantityButton;
    private Button mDecreaseQuantityButton;
    private Button mContactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);



        getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);

    }

    /**
     * set button click listeners
     */
    private void setButtonListeners() {
        //set listener using lambdas
//        mIncreasePriceButton.setOnClickListener(view -> mPresenter.increasePrice());
//        mDecreasePriceButton.setOnClickListener(view -> mPresenter.decreasePrice());
//        mIncreaseQuantityButton.setOnClickListener(view -> mPresenter.increaseQunatity());
//        mDecreaseQuantityButton.setOnClickListener(view -> mPresenter.decreaseQuantity());
//        mContactButton.setOnClickListener(view -> mPresenter.emailContact());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
