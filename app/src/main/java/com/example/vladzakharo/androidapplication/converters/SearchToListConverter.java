package com.example.vladzakharo.androidapplication.converters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vladzakharo.androidapplication.items.Car;
import com.example.vladzakharo.androidapplication.items.Post;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad Zakharo on 13.02.2017.
 */

public class SearchToListConverter implements Converter<List<Post>> {

    private static final String RESPONSE = "response";
    private static final String ITEMS = "items";
    private static final String TAG = "SearchToListConverter";
    private PrefManager mPrefManager;

    public SearchToListConverter (PrefManager prefManager) {
        mPrefManager = prefManager;
    }

    @Nullable
    @Override
    public List<Post> convert(JSONObject jsonObject) {
        List<Post> mPosts = new ArrayList<>();
        try {
            JSONObject responseJsonObject = jsonObject.getJSONObject(RESPONSE);
            JSONArray itemsJsonArray = responseJsonObject.getJSONArray(ITEMS);

            for (int i = 0; i < itemsJsonArray.length(); i++) {
                JSONObject dataJsonObject = itemsJsonArray.getJSONObject(i);
                mPosts.add(new JsonParser(mPrefManager).convert(Post.class, dataJsonObject));
            }
        } catch (JSONException je) {
            Log.e(TAG, "carToList problems", je);
        }
        return mPosts;
    }
}
