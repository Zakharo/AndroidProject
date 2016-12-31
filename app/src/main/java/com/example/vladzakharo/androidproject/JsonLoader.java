package com.example.vladzakharo.androidproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Vlad Zakharo on 31.12.2016.
 */

public class JsonLoader extends AsyncTaskLoader<List<Car>> {

    public JsonLoader(Context context) {
        super(context);
    }

    @Override
    public List<Car> loadInBackground() {
        return new ItemsCatcher().fetchItems();
    }
}
