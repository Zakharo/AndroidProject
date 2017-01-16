package com.example.vladzakharo.androidproject.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.example.vladzakharo.androidproject.Constants.Constants;
import com.example.vladzakharo.androidproject.Converters.JsonParser;
import com.example.vladzakharo.androidproject.DataBase.CarsProvider;
import com.example.vladzakharo.androidproject.DataBase.DataBaseConstants;
import com.example.vladzakharo.androidproject.Http.HttpGetJson;
import com.example.vladzakharo.androidproject.Items.Car;

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

    public UpdateDataService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String mStringJsonObject = HttpGetJson.GET(Constants.URL);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(mStringJsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "json problems", je);
        }

        List<Car> cars = new JsonParser().convertToList(jsonObject);
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            operations.add(ContentProviderOperation.newInsert(CarsProvider.CAR_CONTENT_URI)
            .withValue(DataBaseConstants.CAR_ID, car.getId())
            .withValue(DataBaseConstants.CAR_TITLE, car.getTitle())
            .withValue(DataBaseConstants.CAR_DESCRIPTION, car.getDescription())
            .withValue(DataBaseConstants.CAR_IMAGE_URL, car.getNamePicture())
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
