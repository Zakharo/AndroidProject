package com.example.vladzakharo.androidapplication.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Vlad Zakharo on 28.12.2016.
 */

public class MemoryCache implements ImageCache {

    private LruCache<String, Bitmap> mMemoryCache;
    private static MemoryCache mCache;

    public static MemoryCache getInstance() {
        if (mCache == null) {
            mCache = new MemoryCache();
        }
        return mCache;
    }

    private MemoryCache() {

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

    public void addToCache(String key, Bitmap bitmap) {
        if (getFromCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getFromCache(String key) {
        return mMemoryCache.get(key);
    }
}
