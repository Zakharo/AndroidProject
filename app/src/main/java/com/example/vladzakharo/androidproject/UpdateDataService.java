package com.example.vladzakharo.androidproject;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad Zakharo on 08.01.2017.
 */

public class UpdateDataService extends IntentService {
    private static final String TAG = "UpdateDataService";

    public UpdateDataService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String mStringJsonObject = JsonFetcher.getJsonObject(Constants.URL);
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

    }
}
