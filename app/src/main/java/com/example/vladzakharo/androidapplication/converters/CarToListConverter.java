package com.example.vladzakharo.androidapplication.converters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vladzakharo.androidapplication.items.Car;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad Zakharo on 07.01.2017.
 */

public class CarToListConverter implements Converter<List<Car>> {

    private static final String RESPONSE = "response";
    private static final String ITEMS = "items";
    private static final String TAG = "CarToListConverter";

    private PrefManager mPrefManager;

    public CarToListConverter(PrefManager prefManager) {
        mPrefManager = prefManager;
    }

    @Nullable
    @Override
    public List<Car> convert(JSONObject jsonObject) {
        List<Car> mCars = new ArrayList<>();
        try {
            JSONObject responseJsonObject = jsonObject.getJSONObject(RESPONSE);
            JSONArray itemsJsonArray = responseJsonObject.getJSONArray(ITEMS);

            for (int i = 0; i < itemsJsonArray.length(); i++) {
                JSONObject dataJsonObject = itemsJsonArray.getJSONObject(i);
                mCars.add(new JsonParser(mPrefManager).convert(Car.class, dataJsonObject));
            }
        } catch (JSONException je) {
            Log.e(TAG, "carToList problems", je);
        }
        return mCars;
    }
}
