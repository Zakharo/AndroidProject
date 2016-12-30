package com.example.vladzakharo.androidproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mCarRecyclerView;
    private CarAdapter mCarAdapter;
    private List<Car> mCars = new ArrayList<>();
    private static ImageCache mCache;

    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCache = ImageCache.getInstance();
        mCache.initializeCache();

        //File cacheDir = DiskCache.getDiskCacheDir(this, DiskCache.DISK_CACHE_SUBDIR);


        new CatchItemsTask().execute();

        mCarRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mCarRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void updateUi(){
        mCarAdapter = new CarAdapter(mCars, getApplicationContext(), mCache);
        SeparatorDecoration decoration = new SeparatorDecoration(this, getResources().getColor(R.color.colorPrimary), 0.5f);
        mCarRecyclerView.addItemDecoration(decoration);
        mCarRecyclerView.setAdapter(mCarAdapter);
    }

    private class CatchItemsTask extends AsyncTask<Void, Void, List<Car>> {
        @Override
        protected List<Car> doInBackground(Void... params) {
            return new ItemsCatcher().fetchItems();
        }
        @Override
        protected void onPostExecute(List<Car> cars) {
            mCars = cars;
            updateUi();
        }
    }
}
