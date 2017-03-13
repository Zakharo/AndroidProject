package com.example.vladzakharo.androidapplication.converters;

import com.example.vladzakharo.androidapplication.items.Post;
import com.example.vladzakharo.androidapplication.items.User;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Vlad Zakharo on 05.01.2017.
 */

public class JsonParser {
    private HashMap<String, Converter> mConverters = new HashMap<>();

    private static final String LIST_CARS_CONVERTER = "listConverter";

    public JsonParser(PrefManager prefManager) {
        mConverters.put(Post.class.getName(), new PostConverter(prefManager));
        mConverters.put(LIST_CARS_CONVERTER, new PostToListConverter(prefManager));
        mConverters.put(User.class.getName(), new UserConverter());
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
}
