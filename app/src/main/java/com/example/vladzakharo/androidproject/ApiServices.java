package com.example.vladzakharo.androidproject;

import android.content.Context;
import android.util.Log;

import com.example.vladzakharo.androidproject.converters.JsonParser;
import com.example.vladzakharo.androidproject.http.HttpGetJson;
import com.example.vladzakharo.androidproject.items.User;
import com.example.vladzakharo.androidproject.sharedPreferences.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vlad Zakharo on 24.01.2017.
 */

public class ApiServices {

    private static final String TAG = "ApiService";

    private PrefManager mPrefManager;
    private String mUserInfoUrl;

    public ApiServices(Context context) {
        mPrefManager = new PrefManager(context);
        mUserInfoUrl = "https://api.vk.com/method/users.get?user_ids="
                + mPrefManager.getUid()
                + "&fields=photo,photo_200_orig,bdate,home_town&v=5.62";
    }

    public User getUser() {
        String stringJsonObject = HttpGetJson.GET(mUserInfoUrl);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringJsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "json problems", je);
        }
        return new JsonParser().convert(User.class, jsonObject);
    }
}
