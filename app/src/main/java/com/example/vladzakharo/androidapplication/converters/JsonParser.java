package com.example.vladzakharo.androidapplication.converters;

import android.content.Context;

import com.example.vladzakharo.androidapplication.items.Car;
import com.example.vladzakharo.androidapplication.items.Post;
import com.example.vladzakharo.androidapplication.items.User;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Vlad Zakharo on 05.01.2017.
 */

public class JsonParser {
    private HashMap<String, Converter> mConverters = new HashMap<>();

    private static final String LIST_CARS_CONVERTER = "listConverter";
    private static final String LIST_SEARCH_CONVERTER = "list_search_converter";

    public JsonParser(PrefManager prefManager) {
        mConverters.put(Car.class.getName(), new CarConverter(prefManager));
        mConverters.put(LIST_CARS_CONVERTER, new CarToListConverter(prefManager));
        mConverters.put(User.class.getName(), new UserConverter());
        mConverters.put(Post.class.getName(), new SearchConverter(prefManager));
        mConverters.put(LIST_SEARCH_CONVERTER, new SearchToListConverter(prefManager));
    }

    public <T> T convert(Class<T> t, JSONObject jsonObject) {
        Converter<T> converter = mConverters.get(t.getName());
        if (converter != null) {
            return converter.convert(jsonObject);
        }
        return null;
    }

    public <T> T convertToList(JSONObject jsonObject) {
        Converter<T> converter = mConverters.get(LIST_CARS_CONVERTER);
        if (converter != null) {
            return converter.convert(jsonObject);
        }
        return null;
    }

    public <T> T convertSearchToList(JSONObject jsonObject) {
        Converter<T> converter = mConverters.get(LIST_SEARCH_CONVERTER);
        if (converter != null) {
            return converter.convert(jsonObject);
        }
        return null;
    }
}
