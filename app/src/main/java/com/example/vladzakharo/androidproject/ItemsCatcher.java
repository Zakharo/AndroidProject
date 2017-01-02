package com.example.vladzakharo.androidproject;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad Zakharo on 16.12.2016.
 */

public class ItemsCatcher {
    private static final String TAG = "Items Catcher";

    private static final String CATEGORIES = "categories";
    private static final String DATA = "data";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE_NAME = "image_name";

    public static byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<Car> fetchItems() {
        List<Car> cars = new ArrayList<>();
        try {
            String jsonString = getUrlString(MainActivity.URL);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(cars, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return cars;
    }

    private void parseItems(List<Car> mCars, JSONObject jsonBody) {
        try{

            JSONObject categoriesJsonObject = jsonBody.getJSONObject(CATEGORIES);
            JSONArray dataJsonArray = categoriesJsonObject.getJSONArray(DATA);

            for (int i = 0; i < dataJsonArray.length(); i++) {
                JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);

                Car car = new Car();

                car.setTitle(dataJsonObject.getString(TITLE));
                car.setDescription(dataJsonObject.getString(DESCRIPTION));
                car.setNamePicture(dataJsonObject.getString(IMAGE_NAME));

                mCars.add(car);
            }
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
    }
}
