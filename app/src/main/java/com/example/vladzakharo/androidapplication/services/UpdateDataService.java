package com.example.vladzakharo.androidapplication.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.example.vladzakharo.androidapplication.constants.Constants;
import com.example.vladzakharo.androidapplication.converters.JsonParser;
import com.example.vladzakharo.androidapplication.database.CarsProvider;
import com.example.vladzakharo.androidapplication.database.DataBaseConstants;
import com.example.vladzakharo.androidapplication.http.HttpGetJson;
import com.example.vladzakharo.androidapplication.items.Car;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad Zakharo on 08.01.2017.
 */

public class UpdateDataService extends IntentService {
    private static final String TAG = "UpdateDataService";
    private static final int timeToWait = 1000 * 60 * 10;
    private PrefManager mPrefManager;

    public UpdateDataService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mPrefManager = new PrefManager(getApplicationContext());
        String mStringJsonObject = new ApiServices(getApplicationContext()).getCars();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(mStringJsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "json problems", je);
        }
        List<Car> cars = new JsonParser(mPrefManager).convertToList(jsonObject);

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        ArrayList<ContentProviderOperation> deleteOperations = new ArrayList<>();

        deleteOperations.add(ContentProviderOperation.newDelete(CarsProvider.CAR_CONTENT_URI).build());
        try {
            getContentResolver().applyBatch(CarsProvider.AUTHORITY, deleteOperations);
        } catch (RemoteException | OperationApplicationException re) {
            Log.e(TAG, "UpdateDataService", re);
        }

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            operations.add(ContentProviderOperation.newInsert(CarsProvider.CAR_CONTENT_URI)
            .withValue(DataBaseConstants.CARS_CAR_ID, car.getId())
            .withValue(DataBaseConstants.CARS_CAR_LIKES, car.getLikes())
            .withValue(DataBaseConstants.CARS_CAR_POST_ID, car.getPostId())
            .withValue(DataBaseConstants.CARS_CAR_POST_OWNER_ID, car.getOwnerId())
            .withValue(DataBaseConstants.CARS_CAR_DESCRIPTION, car.getDescription())
            .withValue(DataBaseConstants.CARS_CAR_IMAGE_URL, car.getNamePicture())
            .build());
        }

        try {
            getContentResolver().applyBatch(CarsProvider.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException re) {
            Log.e(TAG, "UpdateDataService", re);
        }

        Intent i = new Intent(this, UpdateDataService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeToWait, pendingIntent);

    }
}
