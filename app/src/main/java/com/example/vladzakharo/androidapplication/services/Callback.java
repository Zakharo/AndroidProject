package com.example.vladzakharo.androidapplication.services;

import com.example.vladzakharo.androidapplication.items.Post;

import java.util.ArrayList;

/**
 * Created by Vlad Zakharo on 16.02.2017.
 */

public interface Callback<T> {
    void onSuccess(ArrayList<T> T);

    void onError(Throwable tr);
}
