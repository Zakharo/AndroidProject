package com.example.vladzakharo.androidapplication.converters;

import com.example.vladzakharo.androidapplication.items.Car;
import com.example.vladzakharo.androidapplication.items.User;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Vlad Zakharo on 05.01.2017.
 */

public class JsonParser {
    private HashMap<String, Converter> mConverters = new HashMap<>();

    private static final String LIST_CONVERTER = "listConverter";

    public JsonParser() {
        mConverters.put(Car.class.getName(), new CarConverter());
        mConverters.put(LIST_CONVERTER, new CarToListConverter());
        mConverters.put(User.class.getName(), new UserConverter());
    }

    public <T> T convert (Class<T> t, JSONObject jsonObject) {
        Converter<T> converter = mConverters.get(t.getName());
        if (converter != null) {
            return converter.convert(jsonObject);
        }
        return null;
    }

    public <T> T convertToList (JSONObject jsonObject) {
        Converter<T> converter = mConverters.get(LIST_CONVERTER);
        if (converter != null) {
            return converter.convert(jsonObject);
        }
        return null;
    }
}
