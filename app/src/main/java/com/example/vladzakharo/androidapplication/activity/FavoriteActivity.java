package com.example.vladzakharo.androidapplication.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.adapters.CarAdapter;
import com.example.vladzakharo.androidapplication.database.CarsProvider;
import com.example.vladzakharo.androidapplication.database.DataBaseConstants;
import com.example.vladzakharo.androidapplication.database.FavoritesProvider;
import com.example.vladzakharo.androidapplication.database.UserProvider;
import com.example.vladzakharo.androidapplication.decoration.Decorator;
import com.example.vladzakharo.androidapplication.images.ImageManager;
import com.example.vladzakharo.androidapplication.items.User;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

public class FavoriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 4;

    private RecyclerView mCarRecyclerView;
    private ProgressBar mProgressBar;
    private CarAdapter mCarAdapter;
    private Toolbar mToolbar;
    private RelativeLayout mRelativeLayout;
    private ImageView mImageView;

    private Cursor mCursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.favorite_relative_layout);
        mImageView = (ImageView) findViewById(R.id.favorite_background_image);

        mCarRecyclerView = (RecyclerView) findViewById(R.id.favorite_recycler_view);
        mCarRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mToolbar = (Toolbar) findViewById(R.id.favorite_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.toolbar_title_favorites));
        mProgressBar = (ProgressBar) findViewById(R.id.favorite_progress_bar);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        updateUi();
    }

    private void updateUi() {
        mCarAdapter = new CarAdapter(this, mCursor);
        Decorator decoration = new Decorator(getApplicationContext(), getResources().getColor(R.color.colorPrimary), 0.5f);
        mCarRecyclerView.addItemDecoration(decoration);
        mCarRecyclerView.setAdapter(mCarAdapter);
        mCarRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_ID) {
            return null;
        }
        return new CursorLoader(this, FavoritesProvider.FAVORITE_CAR_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            mRelativeLayout.setBackgroundColor(Color.WHITE);
            mImageView.setVisibility(View.GONE);
        }
        mCursor = data;
        updateUi();
        mProgressBar.setVisibility(View.GONE);
        mCarRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
