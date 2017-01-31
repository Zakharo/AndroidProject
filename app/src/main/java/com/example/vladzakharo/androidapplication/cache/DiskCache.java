package com.example.vladzakharo.androidapplication.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * Created by Vlad Zakharo on 29.12.2016.
 */

public class DiskCache {

    private DiskLruCache mDiskLruCache;
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;
    private static final String TAG = "DiskCache";

    private static DiskCache mDiskCache;

    public static DiskCache getInstance(File cacheDir) {
        if (mDiskCache == null) {
            mDiskCache = new DiskCache(cacheDir);
        }
        return mDiskCache;
    }

    private DiskCache(File cacheDir) {
        try {
            mDiskLruCache = DiskLruCache.open(cacheDir, APP_VERSION, VALUE_COUNT, DISK_CACHE_SIZE);
        } catch (IOException ioe) {
            Log.e(TAG, "DiskCache", ioe);
        }
    }

    public void addBitmapToDiskCache(String key, Bitmap bitmap) {

        if (mDiskLruCache != null) {
            OutputStream out = null;
            String encryptedKey = CryptoUtils.encryptToMD5(key);
            try {
                DiskLruCache.Snapshot snapshot = mDiskLruCache.get(encryptedKey);
                if (snapshot == null) {
                    DiskLruCache.Editor editor = mDiskLruCache.edit(encryptedKey);
                    out = editor.newOutputStream(0);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    editor.commit();
                    out.close();
                } else {
                    snapshot.getInputStream(0).close();
                }
            } catch (IOException ioe) {
                Log.e(TAG, "DiskCache", ioe);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ioe) {
                        Log.e(TAG, "DiskCache", ioe);
                    }
                }
            }
        }

    }

    public Bitmap getBitmapFromDiskCache(String key) {

        Bitmap bitmap = null;
        String encryptedKey = CryptoUtils.encryptToMD5(key);
        if (mDiskLruCache != null) {
            try {

                DiskLruCache.Snapshot snapshot;
                snapshot = mDiskLruCache.get(encryptedKey);
                if (snapshot == null) {
                    return null;
                }
                InputStream in = snapshot.getInputStream(0);
                if (in != null) {
                    BufferedInputStream buffIn =
                            new BufferedInputStream(in);
                    bitmap = BitmapFactory.decodeStream(buffIn);
                }
            } catch (IOException ioe) {
                Log.e(TAG, "DiskCache", ioe);
            }
        } else {
            return null;
        }
        return bitmap;
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }
}
