package com.example.vladzakharo.androidproject.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.*;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladzakharo.androidproject.Adapters.CarAdapter;
import com.example.vladzakharo.androidproject.DataBase.CarsProvider;
import com.example.vladzakharo.androidproject.Decoration.Decorator;
import com.example.vladzakharo.androidproject.Images.ImageManager;
import com.example.vladzakharo.androidproject.R;
import com.example.vladzakharo.androidproject.Services.UpdateDataService;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mCarRecyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CarAdapter mCarAdapter;
    private static final int LOADER_ID = 0;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private View mNavHeader;
    private ImageView mImgNavHeaderBg, mImgProfile;
    private TextView mTextName;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        Intent intentService = new Intent(this, UpdateDataService.class);
        startService(intentService);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavHeader = mNavigationView.getHeaderView(0);
        mImgNavHeaderBg = (ImageView) findViewById(R.id.img_header_bg);
        mImgProfile = (ImageView) findViewById(R.id.img_profile);
        mTextName = (TextView) findViewById(R.id.name);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        mCarRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mCarRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intentService = new Intent(getApplicationContext(), UpdateDataService.class);
                startService(intentService);
            }
        });

        updateUi();

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void updateUi(){
        mCarAdapter = new CarAdapter(this, null);
        Decorator decoration = new Decorator(this, getResources().getColor(R.color.colorPrimary), 0.5f);
        mCarRecyclerView.addItemDecoration(decoration);
        mCarRecyclerView.setAdapter(mCarAdapter);
        mCarRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_ID) {
            return null;
        }
        return new CursorLoader(this, CarsProvider.CAR_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCarAdapter.changeCursor(cursor);
        mProgressBar.setVisibility(View.GONE);
        mCarRecyclerView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                Toast.makeText(this, "Домой", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Выходим", Toast.LENGTH_SHORT).show();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
