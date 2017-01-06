package com.example.vladzakharo.androidproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad Zakharo on 31.12.2016.
 */

public class JsonLoader extends AsyncTaskLoader<List<Car>> {

    private static String mStringJsonObject;
    private static List<Car> mCars = new ArrayList<>();
    private static final String TAG = "JsonLoader";

    private static final String CATEGORIES = "categories";
    private static final String DATA = "data";

    public JsonLoader(Context context) {
        super(context);
    }

    @Override
    public List<Car> loadInBackground() {
        /*mStringJsonObject = JsonFetcher.getJsonObject(Constants.URL);
        try {
            JSONObject jsonObject = new JSONObject(mStringJsonObject);
            JSONObject categoriesJsonObject = jsonObject.getJSONObject(CATEGORIES);
            JSONArray dataJsonArray = categoriesJsonObject.getJSONArray(DATA);
            for (int i = 0; i < dataJsonArray.length(); i++) {
                JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                mCars.add(JsonParser.convert(Car.class, dataJsonObject));
            }
        } catch (JSONException je) {
            Log.e(TAG, "json problems", je);
        }
        return mCars;*/
        return new ItemsCatcher().fetchItems();
    }
}
