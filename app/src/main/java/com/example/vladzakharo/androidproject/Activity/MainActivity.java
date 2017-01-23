package com.example.vladzakharo.androidproject.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.vladzakharo.androidproject.adapters.CarAdapter;
import com.example.vladzakharo.androidproject.dataBase.CarsProvider;
import com.example.vladzakharo.androidproject.decoration.Decorator;
import com.example.vladzakharo.androidproject.R;
import com.example.vladzakharo.androidproject.http.HttpGetJson;
import com.example.vladzakharo.androidproject.images.ImageManager;
import com.example.vladzakharo.androidproject.services.UpdateDataService;
import com.example.vladzakharo.androidproject.sharedPreferences.PrefManager;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    private RecyclerView mCarRecyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CarAdapter mCarAdapter;
    private static final int LOADER_ID = 0;
    private static final String TAG = "Main Activity";
    private static final String RESPONSE = "response";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHOTO = "photo";
    private String mRequestUrl;

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

        mRequestUrl = "https://api.vk.com/method/users.get?user_ids="
                + mPrefManager.getUid() +
                "&fields=photo&v=5.62";

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
        new HeaderUpdate().execute(mRequestUrl);
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
                Intent intent = new Intent(this, FirstActivity.class);
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

    public class HeaderUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String stringJsonObject = HttpGetJson.GET(params[0]);
            return stringJsonObject;
        }


        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject;
            JSONArray jsonArray;
            String firstName = null;
            String lastName = null;
            String photo = null;
            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray(RESPONSE);
                JSONObject object = jsonArray.getJSONObject(0);
                firstName = object.getString(FIRST_NAME);
                lastName = object.getString(LAST_NAME);
                photo = object.getString(PHOTO);
            } catch (JSONException je) {
                Log.e(TAG, "json problems", je);
            }
            mFirstName.setText(firstName);
            mLastName.setText(lastName);
            ImageManager.getInstance()
                    .getImageLoader(getApplicationContext())
                    .from(photo)
                    .to(mImgProfile)
                    .load();
        }
    }
}
