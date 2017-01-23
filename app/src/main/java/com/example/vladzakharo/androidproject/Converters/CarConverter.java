package com.example.vladzakharo.androidproject.converters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vladzakharo.androidproject.items.Car;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vlad Zakharo on 05.01.2017.
 */

public class CarConverter implements Converter<Car> {
    private static final String TAG = "CarConverter";

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE_NAME = "image_name";

    @Nullable
    @Override
    public Car convert(JSONObject jsonObject) {
        Car car = new Car();
        try {
            car.setId(jsonObject.getInt(ID));
            car.setTitle(jsonObject.getString(TITLE));
            car.setDescription(jsonObject.getString(DESCRIPTION));
            car.setNamePicture(jsonObject.getString(IMAGE_NAME));
        } catch (JSONException je) {
            Log.e(TAG, "json parse problems", je);
        }
        return car;
    }
}
