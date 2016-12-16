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
    private static List<Car> mCars = new ArrayList<>();
    private static JSONObject jObject;

    private static JSONObject getJsonObject(Context context){
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

    public static List<Car> parseItems(Context context) throws IOException, JSONException{
        jObject = getJsonObject(context);

        JSONObject categoriesJsonObject = jObject.getJSONObject("categories");
        JSONArray dataJsonArray = categoriesJsonObject.getJSONArray("data");

        for (int i = 0; i < dataJsonArray.length(); i++){
            JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);

            Car car = new Car();

            car.setTitle(dataJsonObject.getString("title"));
            car.setDescription(dataJsonObject.getString("description"));
            car.setNamePicture(dataJsonObject.getString("image_name"));

            mCars.add(car);
        }
        return mCars;
    }
}
