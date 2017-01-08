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

    private String mStringJsonObject;
    private static final String TAG = "JsonLoader";

    public JsonLoader(Context context) {
        super(context);
    }

    @Override
    public List<Car> loadInBackground() {
        mStringJsonObject = JsonFetcher.getJsonObject(Constants.URL);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(mStringJsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "json problems", je);
        }
        List<Car> cars = new JsonParser().convertToList(jsonObject);
        return cars;
    }
}
