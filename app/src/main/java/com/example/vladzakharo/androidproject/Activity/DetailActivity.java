package com.example.vladzakharo.androidproject.Activity;

import android.app.Activity;
import android.database.Cursor;
import android.support.design.widget.CollapsingToolbarLayout;
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

import com.example.vladzakharo.androidproject.Items.Car;
import com.example.vladzakharo.androidproject.Adapters.CarAdapter;
import com.example.vladzakharo.androidproject.DataBase.CarsProvider;
import com.example.vladzakharo.androidproject.DataBase.DataBaseConstants;
import com.example.vladzakharo.androidproject.Images.ImageManager;
import com.example.vladzakharo.androidproject.Links.LinkTransformationMethod;
import com.example.vladzakharo.androidproject.R;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private int mCarId;
    private TextView mTvDescription;
    private ProgressBar mProgressBar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private ImageView mImageView;
    private Toolbar mToolbar;
    private Activity mActivity;

    private static final int LOADER_ID = 2;
    private static final String KEY_CAR_ID = "keyCarId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getIntent() != null){
            mCarId = getIntent().getIntExtra(CarAdapter.CAR_ID, 0);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_CAR_ID, mCarId);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mCollapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mImageView = (ImageView) findViewById(R.id.toolbar_image);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_description);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTvDescription = (TextView) findViewById(R.id.detail_text_view);
        mActivity = this;

        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
        mProgressBar.setVisibility(View.VISIBLE);
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
        mProgressBar.setVisibility(View.GONE);
        if (data == null || !data.moveToFirst()) {
            return;
        }
        Car car = Car.getCarFromCursor(data);
        mCollapsingToolbar.setTitle(car.getTitle());
        mTvDescription.setText(car.getDescription());
        mTvDescription.setTransformationMethod(new LinkTransformationMethod(mActivity));
        mTvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        ImageManager.getInstance().getImageLoader(this)
                .from(car.getNamePicture())
                .to(mImageView)
                .load();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
