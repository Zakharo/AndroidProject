package com.example.vladzakharo.androidproject;

import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.*;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Car>> {

    private RecyclerView mCarRecyclerView;
    private CarAdapter mCarAdapter;
    private List<Car> mCars = new ArrayList<>();
    private static ImageCache mCache;
    public static final String URL = "http://www.mocky.io/v2/586a58e3110000550e261e66";
    public static final String BUNDLE_URL = "bundle_url";
    private static final int LOADER_ID = 0;
    private Loader<List<Car>> mLoader;
    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCache = ImageCache.getInstance();
        mCache.initializeCache();

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_URL, URL);
        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
        mLoader = getSupportLoaderManager().getLoader(LOADER_ID);
        mLoader.forceLoad();

        File cacheDir = DiskCache.getDiskCacheDir(this, DISK_CACHE_SUBDIR);
        new DiskCache.InitDiskCacheTask().execute(cacheDir);

        mCarRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mCarRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void updateUi(){
        mCarAdapter = new CarAdapter(mCars, getApplicationContext());
        SeparatorDecoration decoration = new SeparatorDecoration(this, getResources().getColor(R.color.colorPrimary), 0.5f);
        mCarRecyclerView.addItemDecoration(decoration);
        mCarRecyclerView.setAdapter(mCarAdapter);
    }

    @Override
    public Loader<List<Car>> onCreateLoader(int id, Bundle args) {
        Loader<List<Car>> loader = null;
        if (id == LOADER_ID) {
            loader = new JsonLoader(getApplicationContext());
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Car>> loader, List<Car> data) {
        mCars = data;
        updateUi();
    }

    @Override
    public void onLoaderReset(Loader<List<Car>> loader) {

    }
}
