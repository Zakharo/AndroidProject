package com.example.vladzakharo.androidproject.Cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Vlad Zakharo on 28.12.2016.
 */

public class ImageCache {

    private LruCache<String, Bitmap> mMemoryCache;
    private static ImageCache mCache;

    public static ImageCache getInstance() {
        if (mCache == null) {
            mCache = new ImageCache();
        }
        return mCache;
    }

    public void initializeCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }
}
