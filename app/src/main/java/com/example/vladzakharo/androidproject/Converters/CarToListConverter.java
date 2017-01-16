package com.example.vladzakharo.androidproject.Converters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vladzakharo.androidproject.Items.Car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad Zakharo on 07.01.2017.
 */

public class CarToListConverter implements Converter<List<Car>> {

    private static final String CATEGORIES = "categories";
    private static final String DATA = "data";
    private static final String TAG = "CarToListConverter";

    @Nullable
    @Override
    public List<Car> convert(JSONObject jsonObject) {
        List<Car> mCars = new ArrayList<>();
        try {
            JSONObject categoriesJsonObject = jsonObject.getJSONObject(CATEGORIES);
            JSONArray dataJsonArray = categoriesJsonObject.getJSONArray(DATA);

            for (int i = 0; i < dataJsonArray.length(); i++) {
                JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                mCars.add(new JsonParser().convert(Car.class, dataJsonObject));
            }
        } catch (JSONException je) {
            Log.e(TAG, "carToList problems", je);
        }
        return mCars;
    }
}
