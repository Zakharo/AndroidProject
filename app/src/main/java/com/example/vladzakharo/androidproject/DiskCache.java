package com.example.vladzakharo.androidproject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * Created by Vlad Zakharo on 29.12.2016.
 */

public class DiskCache {
    /*

    private DiskLruCache mDiskCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;
    public static final String DISK_CACHE_SUBDIR = "thumbnails";


    class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
        @Override
        protected Void doInBackground(File... params) {
            synchronized (mDiskCacheLock) {
                File cacheDir = params[0];
                mDiskCache = DiskLruCache.open(cacheDir, DISK_CACHE_SIZE);
                mDiskCacheStarting = false;
                mDiskCacheLock.notifyAll();
            }
            return null;
        }
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }*/
}
