package com.example.vladzakharo.androidproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.*;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mCarRecyclerView;
    private CarAdapter mCarAdapter;
    private List<Car> mCars = new ArrayList<>();
    private static final int LOADER_ID = 0;

    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intentService = new Intent(this, UpdateDataService.class);
        startService(intentService);


        mCarRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mCarRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        updateUi();

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void updateUi(){
        mCarAdapter = new CarAdapter(this, null);
        SeparatorDecoration decoration = new SeparatorDecoration(this, getResources().getColor(R.color.colorPrimary), 0.5f);
        mCarRecyclerView.addItemDecoration(decoration);
        mCarRecyclerView.setAdapter(mCarAdapter);
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
        //int i = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.CAR_ID));
        //Toast.makeText(getApplicationContext(), i, Toast.LENGTH_LONG).show();
        //mCarAdapter.changeCursor(cursor);
        Log.d("TAG", DatabaseUtils.dumpCursorToString(cursor));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
