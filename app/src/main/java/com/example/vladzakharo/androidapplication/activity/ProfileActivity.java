package com.example.vladzakharo.androidapplication.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.loaders.UserLoader;
import com.example.vladzakharo.androidapplication.images.ImageManager;
import com.example.vladzakharo.androidapplication.items.User;

public class ProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<User> {

    private TextView mCity;
    private TextView mBirthday;
    private ProgressBar mProgressBar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private ImageView mImageView;
    private Toolbar mToolbar;

    private static final int PROFILE_LOADER_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mCity = (TextView) findViewById(R.id.profile_city_textview);
        mBirthday = (TextView) findViewById(R.id.profile_birthday_textview);
        mProgressBar = (ProgressBar) findViewById(R.id.profile_progress_bar);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.profile_collapsing_toolbar);
        mImageView = (ImageView) findViewById(R.id.profile_toolbar_image);
        mToolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportLoaderManager().initLoader(PROFILE_LOADER_ID, null, this);
        mProgressBar.setVisibility(View.VISIBLE);
        mCity.setVisibility(View.INVISIBLE);
        mBirthday.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<User> onCreateLoader(int id, Bundle args) {
        if (id != PROFILE_LOADER_ID) {
            return null;
        }
        return new UserLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<User> loader, User data) {
        updateInfo(data);
    }

    @Override
    public void onLoaderReset(Loader<User> loader) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    private void updateInfo(User user) {
        mProgressBar.setVisibility(View.GONE);
        mCity.setVisibility(View.VISIBLE);
        mBirthday.setVisibility(View.VISIBLE);
        mCity.setText(user.getHomeTown());
        mBirthday.setText(user.getDateOfBirth());
        mCollapsingToolbar.setTitle(user.toString());
        ImageManager.getInstance()
                .getImageLoader(this)
                .from(user.getFullPhoto())
                .to(mImageView)
                .load();
    }
}
