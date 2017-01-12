package com.example.vladzakharo.androidproject;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private FragmentManager fm;
    //private Car mCar;
    private int mCarId;

    private CollapsingToolbarLayout mCollapsingToolbar;
    private ImageView mImageView;
    private Toolbar mToolbar;

    private ImageManager sImageManager;

    private static final int LOADER_ID = 2;
    private static final String KEY_CAR_ID = "keyCarId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getIntent() != null){
            mCarId = getIntent().getIntExtra(CarAdapter.CAR_ID, 1);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_CAR_ID, mCarId);

        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.detail_fragment_container, DetailFragment.newInstance(mCarId))
                .commit();

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);

        /*mCollapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle(mCar.getTitle());
        mImageView = (ImageView) findViewById(R.id.toolbar_image);

        sImageManager = ImageManager.getInstance();
        sImageManager.getImageLoader(getApplicationContext())
                .from(mCar.getNamePicture())
                .to(mImageView)
                .load();*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_ID) {
            return null;
        }
        return new CursorLoader(this, CarsProvider.CAR_CONTENT_URI, null, DataBaseConstants.CAR_ID + " = ?", new String[]{String.valueOf(args.get(KEY_CAR_ID))}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Car car = Car.getCarFromCursor(data);

        mCollapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle(car.getTitle());
        mImageView = (ImageView) findViewById(R.id.toolbar_image);

        sImageManager = ImageManager.getInstance();
        sImageManager.getImageLoader(this)
                .from(car.getNamePicture())
                .to(mImageView)
                .load();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
