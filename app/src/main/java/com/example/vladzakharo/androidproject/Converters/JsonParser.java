package com.example.vladzakharo.androidproject.Converters;

import com.example.vladzakharo.androidproject.Converters.CarConverter;
import com.example.vladzakharo.androidproject.Converters.CarToListConverter;
import com.example.vladzakharo.androidproject.Converters.Converter;
import com.example.vladzakharo.androidproject.Items.Car;

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
