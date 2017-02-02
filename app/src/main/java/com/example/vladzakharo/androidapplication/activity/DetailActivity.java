package com.example.vladzakharo.androidapplication.activity;

import android.app.Activity;
import android.database.Cursor;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vladzakharo.androidapplication.items.Car;
import com.example.vladzakharo.androidapplication.adapters.CarAdapter;
import com.example.vladzakharo.androidapplication.database.CarsProvider;
import com.example.vladzakharo.androidapplication.database.DataBaseConstants;
import com.example.vladzakharo.androidapplication.images.ImageManager;
import com.example.vladzakharo.androidapplication.links.LinkTransformationMethod;
import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.services.ApiServices;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private int mCarId;
    private String mCarDescription;
    private TextView mTvDescription;
    private ProgressBar mProgressBar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private ImageView mImageView;
    private Toolbar mToolbar;
    private Activity mActivity;
    private Car mCar;
    private FloatingActionButton mFab;

    private static final int LOADER_ID = 2;
    private static final String KEY_CAR_ID = "keyCarId";
    private static final String KEY_CAR_DESCRIPTION = "keyCarDescription";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getIntent() != null){
            mCarDescription = getIntent().getStringExtra(CarAdapter.CAR_DESCRIPTION);
            //mCarId = getIntent().getIntExtra(CarAdapter.CAR_ID, 0);
        }

        Bundle bundle = new Bundle();
        bundle.putString(KEY_CAR_DESCRIPTION, mCarDescription);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = new ApiServices(getApplicationContext()).addLike(mCar);
                String awe = response;
            }
        });

        mToolbar = (Toolbar)findViewById(R.id.detail_toolbar);
        mCollapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mImageView = (ImageView) findViewById(R.id.toolbar_image);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_description);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTvDescription = (TextView) findViewById(R.id.detail_text_view);
        mActivity = this;

        mProgressBar.setVisibility(View.VISIBLE);
        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
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
        //return new CursorLoader(this, CarsProvider.CAR_CONTENT_URI, null, DataBaseConstants.CAR_ID + " = ?", new String[]{String.valueOf(args.get(KEY_CAR_ID))}, null);
        return new CursorLoader(this, CarsProvider.CAR_CONTENT_URI, null, DataBaseConstants.CAR_DESCRIPTION + " = ?", new String[]{String.valueOf(args.get(KEY_CAR_DESCRIPTION))}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProgressBar.setVisibility(View.GONE);
        if (data == null || !data.moveToFirst()) {
            return;
        }
        mCar = Car.getCarFromCursor(data);
        //mCollapsingToolbar.setTitle(car.getTitle());
        mTvDescription.setText(mCar.getDescription());
        mTvDescription.setTransformationMethod(new LinkTransformationMethod(mActivity));
        mTvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        ImageManager.getInstance().getImageLoader(this)
                .from(mCar.getNamePicture())
                .to(mImageView)
                .load();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
