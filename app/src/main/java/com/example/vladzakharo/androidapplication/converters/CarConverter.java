package com.example.vladzakharo.androidapplication.converters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vladzakharo.androidapplication.items.Car;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vlad Zakharo on 05.01.2017.
 */

public class CarConverter implements Converter<Car> {
    private static final String TAG = "CarConverter";

    private static final String TEXT = "text";
    private static final String ATTACHMENTS = "attachments";
    private static final String PHOTO = "photo";
    private static final String IMAGE_NAME = "photo_604";
    private static final String LIKES = "likes";
    private static final String LIKES_COUNT = "count";
    private static final String POST_ID = "id";
    private static final String OWNER_ID = "owner_id";
    private static final String USER_LIKES = "user_likes";

    private PrefManager mPrefManager;

    public CarConverter(PrefManager prefManager) {
        mPrefManager = prefManager;
    }

    @Nullable
    @Override
    public Car convert(JSONObject jsonObject) {
        Car car = new Car();
        try {
            car.setId(mPrefManager.getPostId());
            mPrefManager.setPostId(mPrefManager.getPostId() + 1);

            car.setPostId(jsonObject.getInt(POST_ID));
            car.setOwnerId(jsonObject.getInt(OWNER_ID));
            car.setDescription(jsonObject.getString(TEXT));

            JSONArray attachmentsArray = jsonObject.getJSONArray(ATTACHMENTS);

            JSONObject objectInsideAttachment = attachmentsArray.getJSONObject(0);
            JSONObject photoObject = objectInsideAttachment.getJSONObject(PHOTO);
            car.setNamePicture(photoObject.getString(IMAGE_NAME));
            JSONObject likesObject = jsonObject.getJSONObject(LIKES);
            car.setLikes(likesObject.getInt(LIKES_COUNT));
            car.setCarLiked(likesObject.getInt(USER_LIKES));
        } catch (JSONException je) {
            Log.e(TAG, "json parse problems", je);
        }
        return car;
    }
}
