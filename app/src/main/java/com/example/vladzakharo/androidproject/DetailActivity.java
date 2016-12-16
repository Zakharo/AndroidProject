package com.example.vladzakharo.androidproject;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;


public class DetailActivity extends AppCompatActivity {
    private FragmentManager fm;
    private Car mCar;

    private CollapsingToolbarLayout mCollapsingToolbar;
    private ImageView mImageView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getIntent() != null){
            mCar = (Car)getIntent().getSerializableExtra(CarAdapter.CAR_SERIALIZABLE);
        }

        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.detail_fragment_container, DetailFragment.newInstance(mCar))
                .commit();

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mCollapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle(mCar.getTitle());

        mImageView = (ImageView) findViewById(R.id.toolbar_image);
        mImageView.setImageBitmap(ImageCatcher.getBitmapFromAssets(getApplicationContext(), mCar.getNamePicture()));
    }
}
