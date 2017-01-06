package com.example.vladzakharo.androidproject;

import android.support.annotation.Nullable;

import org.json.JSONObject;

/**
 * Created by Vlad Zakharo on 05.01.2017.
 */

public interface Converter <T> {

    @Nullable
    T convert (JSONObject jsonObject);
}
