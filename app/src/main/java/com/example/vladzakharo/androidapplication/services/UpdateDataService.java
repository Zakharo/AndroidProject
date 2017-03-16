package com.example.vladzakharo.androidapplication.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.util.Log;

import com.example.vladzakharo.androidapplication.converters.JsonParser;
import com.example.vladzakharo.androidapplication.database.CarsProvider;
import com.example.vladzakharo.androidapplication.database.DBUtils;
import com.example.vladzakharo.androidapplication.database.DataBaseConstants;
import com.example.vladzakharo.androidapplication.items.Post;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad Zakharo on 08.01.2017.
 */

public class UpdateDataService extends IntentService implements Callback<Post> {
    private static final String TAG = "UpdateDataService";
    private static final int timeToWait = 1000 * 60 * 10;
    private Callback mCallback;

    public UpdateDataService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkConnected()) {
            return;
        }

        mCallback = this;
        ApiServices.getInstance(getApplicationContext()).getCars(mCallback);

        Intent i = new Intent(this, UpdateDataService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeToWait, pendingIntent);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onSuccess(ArrayList<Post> list) {

        DBUtils.clearTableCars(getApplicationContext());
        DBUtils.loadTableCars(list, getApplicationContext());
    }

    @Override
    public void onError(Throwable tr) {
        Log.e(TAG, "UpdateDataService", tr);
    }
}
