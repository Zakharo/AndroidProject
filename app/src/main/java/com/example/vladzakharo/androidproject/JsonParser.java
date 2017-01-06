package com.example.vladzakharo.androidproject;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Vlad Zakharo on 05.01.2017.
 */

public class JsonParser {
    private static HashMap<String, Converter> mConverters = new HashMap<>();

    public JsonParser() {
        mConverters.put(Car.class.getName(), new CarConverter());
    }

    public static <T> T convert (Class<T> t, JSONObject jsonObject) {
        Converter<T> converter = mConverters.get(t.getName());
        if (converter != null) {
            return converter.convert(jsonObject);
        }
        return null;
    }
}
