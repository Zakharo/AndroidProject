package com.example.vladzakharo.androidproject;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad Zakharo on 16.12.2016.
 */

public class ItemsCatcher {
    private static JSONObject jObject;

    private static final String CATEGORIES = "categories";
    private static final String DATA = "data";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE_NAME = "image_name";

    private static JSONObject getJsonObject(Context context) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            jObject = new JSONObject(byteArrayOutputStream.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
        return jObject;
    }

    public static List<Car> parseItems(Context context) {
        List<Car> mCars = new ArrayList<>();
        try{
            jObject = getJsonObject(context);

            JSONObject categoriesJsonObject = jObject.getJSONObject(CATEGORIES);
            JSONArray dataJsonArray = categoriesJsonObject.getJSONArray(DATA);

            for (int i = 0; i < dataJsonArray.length(); i++) {
                JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);

                Car car = new Car();

                car.setTitle(dataJsonObject.getString(TITLE));
                car.setDescription(dataJsonObject.getString(DESCRIPTION));
                car.setNamePicture(dataJsonObject.getString(IMAGE_NAME));

                mCars.add(car);
            }
        }catch (JSONException je){
            je.printStackTrace();
        }
        return mCars;
    }
}
