package com.example.vladzakharo.androidapplication.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.*;
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
import android.widget.TextView;
import android.widget.Toast;


import com.example.vladzakharo.androidapplication.loaders.UserLoader;
import com.example.vladzakharo.androidapplication.adapters.CarAdapter;
import com.example.vladzakharo.androidapplication.database.CarsProvider;
import com.example.vladzakharo.androidapplication.decoration.Decorator;
import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.images.ImageManager;
import com.example.vladzakharo.androidapplication.items.User;
import com.example.vladzakharo.androidapplication.services.UpdateDataService;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    private RecyclerView mCarRecyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CarAdapter mCarAdapter;

    private static final int LOADER_ID = 0;
    private static final int LOADER_USER_ID = 1;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private View mNavHeader;
    private ImageView mImgProfile;
    private TextView mFirstName;
    private TextView mLastName;
    private Toolbar toolbar;
    private PrefManager mPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        Intent intentService = new Intent(this, UpdateDataService.class);
        startService(intentService);

        mPrefManager = new PrefManager(this);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavHeader = mNavigationView.getHeaderView(0);
        mImgProfile = (ImageView) mNavHeader.findViewById(R.id.img_profile);
        mImgProfile.setOnClickListener(this);
        mFirstName = (TextView) mNavHeader.findViewById(R.id.first_name);
        mLastName = (TextView) mNavHeader.findViewById(R.id.last_name);
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
        getSupportLoaderManager().initLoader(LOADER_USER_ID, null, new LoaderManager.LoaderCallbacks<User>() {
            @Override
            public Loader<User> onCreateLoader(int id, Bundle args) {
                if (id != LOADER_USER_ID) {
                    return null;
                }
                return new UserLoader(getApplicationContext());
            }

            @Override
            public void onLoadFinished(Loader<User> loader, User data) {
                updateHeader(data);
            }

            @Override
            public void onLoaderReset(Loader<User> loader) {

            }
        });
    }

    private void updateUi() {
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
                mPrefManager.deleteToken();
                mPrefManager.deleteUid();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void updateHeader(User user) {
        mFirstName.setText(user.getFirstName());
        mLastName.setText(user.getLastName());
        ImageManager.getInstance()
                .getImageLoader(getApplicationContext())
                .from(user.getPicture())
                .to(mImgProfile)
                .transform(true)
                .load();
    }
}
