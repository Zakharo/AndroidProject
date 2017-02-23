package com.example.vladzakharo.androidapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.*;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.vladzakharo.androidapplication.cache.DiskCache;
import com.example.vladzakharo.androidapplication.constants.Constants;
import com.example.vladzakharo.androidapplication.fragments.DateFragment;
import com.example.vladzakharo.androidapplication.fragments.LikesFragment;
import com.example.vladzakharo.androidapplication.images.RoundTransformer;
import com.example.vladzakharo.androidapplication.loaders.UserLoader;
import com.example.vladzakharo.androidapplication.database.CarsProvider;
import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.images.ImageManager;
import com.example.vladzakharo.androidapplication.items.User;
import com.example.vladzakharo.androidapplication.services.UpdateDataService;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        MenuItem.OnMenuItemClickListener {

    private static final int LOADER_POSTS_ID = 0;
    private static final int LOADER_USER_ID = 1;
    private static final int REQUEST_CODE_PREFERENCES = 0;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private View mNavHeader;
    private ImageView mImgProfile;
    private TextView mFirstName;
    private TextView mLastName;
    private Toolbar toolbar;
    private PrefManager mPrefManager;
    private File cacheDir;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private Cursor mCursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        Intent intentService = new Intent(this, UpdateDataService.class);
        startService(intentService);

        cacheDir = DiskCache.getDiskCacheDir(getApplicationContext(), Constants.DISK_CACHE_SUBDIR);

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
        getSupportActionBar().setTitle(R.string.toolbar_title_main);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        getSupportLoaderManager().initLoader(LOADER_POSTS_ID, null, this);
        if (isNetworkConnected()) {
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

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(DateFragment.newInstance(mCursor), getResources().getString(R.string.date));
        adapter.addFragment(LikesFragment.newInstance(mCursor), getResources().getString(R.string.likes));
        viewPager.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_POSTS_ID) {
            return null;
        }
        return new CursorLoader(this, CarsProvider.CAR_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == LOADER_POSTS_ID) {
            mCursor = cursor;
            setupViewPager(mViewPager);
            mTabLayout.setupWithViewPager(mViewPager);
        }


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
            case R.id.nav_favorite:
                Intent intentFav = new Intent(this, FavoriteActivity.class);
                startActivity(intentFav);
                break;
            case R.id.nav_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, REQUEST_CODE_PREFERENCES);
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
        ImageManager.getInstance(cacheDir)
                .getImageLoader(getApplicationContext())
                .from(user.getPicture())
                .to(mImgProfile)
                .transform(new RoundTransformer(this))
                .load();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PREFERENCES) {
            loadNewAmountOfCars();
        }
    }

    private void loadNewAmountOfCars() {
        Intent intentService = new Intent(this, UpdateDataService.class);
        startService(intentService);
        getSupportLoaderManager().initLoader(LOADER_POSTS_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        searchItem.setOnMenuItemClickListener(this);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_search:
                if (isNetworkConnected()) {
                    startActivity(new Intent(this, SearchActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Check Internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
