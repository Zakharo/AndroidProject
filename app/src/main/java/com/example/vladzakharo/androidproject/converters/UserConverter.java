package com.example.vladzakharo.androidproject.converters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vladzakharo.androidproject.items.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vlad Zakharo on 24.01.2017.
 */

public class UserConverter implements Converter<User> {

    private static final String TAG = "UserConverter";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHOTO = "photo";
    private static final String FULL_PHOTO = "photo_200_orig";
    private static final String BDATE = "bdate";
    private static final String HOME_TOWN = "home_town";
    private static final String RESPONSE = "response";

    @Nullable
    @Override
    public User convert(JSONObject jsonObject) {
        User user = new User();
        JSONArray jsonArray;
        JSONObject object ;
        try {
            jsonArray = jsonObject.getJSONArray(RESPONSE);
            object = jsonArray.getJSONObject(0);
            user.setFirstName(object.getString(FIRST_NAME));
            user.setLastName(object.getString(LAST_NAME));
            user.setPicture(object.getString(PHOTO));
            user.setFullPhoto(object.getString(FULL_PHOTO));
            user.setDateOfBirth(object.getString(BDATE));
            user.setHomeTown(object.getString(HOME_TOWN));
        } catch (JSONException je) {
            Log.e(TAG, "json parse problems", je);
        }
        return user;
    }
}
