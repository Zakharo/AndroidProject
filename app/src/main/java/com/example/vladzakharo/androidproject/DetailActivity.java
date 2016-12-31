package com.example.vladzakharo.androidproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class DetailActivity extends AppCompatActivity {
    private FragmentManager fm;
    private Car mCar;

    private CollapsingToolbarLayout mCollapsingToolbar;
    private ImageView mImageView;
    private Toolbar mToolbar;
    private static ImageCache mCache;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        activity = this;

        if (getIntent() != null){
            mCar = getIntent().getParcelableExtra(CarAdapter.CAR_PARCELABLE);
        }

        mCache = ImageCache.getInstance();

        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.detail_fragment_container, DetailFragment.newInstance(mCar))
                .commit();

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle(mCar.getTitle());

        mImageView = (ImageView) findViewById(R.id.toolbar_image);
        Bitmap bitmap = mCache.getBitmapFromMemoryCache(mCar.getNamePicture());
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        } else {
            Loader.getImageLoader()
                    .from(mCar.getNamePicture())
                    .to(mImageView)
                    .load();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
