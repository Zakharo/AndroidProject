package com.example.vladzakharo.androidapplication.cache;

import android.graphics.Bitmap;

/**
 * Created by Vlad Zakharo on 19.02.2017.
 */

public interface ImageCache {
    void addToCache(String key, Bitmap bitmap);

    Bitmap getFromCache(String key);
}
