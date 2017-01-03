package com.example.vladzakharo.androidproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * Created by Vlad Zakharo on 29.12.2016.
 */

public class DiskCache {

    private static DiskLruCache mDiskCache;
    private static final Object mDiskCacheLock = new Object();
    private static boolean mDiskCacheStarting = true;
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;
    public static final String TAG = "DiskCache";

    public static class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
        @Override
        protected Void doInBackground(File... params) {
            synchronized (mDiskCacheLock) {
                try {
                    File cacheDir = params[0];
                    mDiskCache = DiskLruCache.open(cacheDir, APP_VERSION, VALUE_COUNT, DISK_CACHE_SIZE);
                    mDiskCacheStarting = false;
                    mDiskCacheLock.notifyAll();
                } catch (IOException ioe) {
                    Log.e(TAG, "DiskCache", ioe);
                }
            }
            return null;
        }
    }

    public static void addBitmapToDiskCache(String key, Bitmap bitmap) {
        synchronized (mDiskCacheLock) {
            if (mDiskCache != null) {
                OutputStream out = null;
                String encryptedKey = CryptoUtils.encryptToMD5(key);
                try {
                    DiskLruCache.Snapshot snapshot = mDiskCache.get(encryptedKey);
                    if (snapshot == null) {
                        DiskLruCache.Editor editor = mDiskCache.edit(encryptedKey);
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
    }

    public static Bitmap getBitmapFromDiskCache(String key) {
        Bitmap bitmap = null;
        String encryptedKey = CryptoUtils.encryptToMD5(key);
        synchronized (mDiskCacheLock) {
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {}
            }
            if (mDiskCache != null) {
                try {

                    DiskLruCache.Snapshot snapshot = null;
                    snapshot = mDiskCache.get(encryptedKey);
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
            }
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

    public static class GetBitmapFromDiskCacheTask extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<ImageView> mImageView;

        public GetBitmapFromDiskCacheTask(ImageView imageView) {
            mImageView = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            final String imageKey = params[0];
            Bitmap bitmap = DiskCache.getBitmapFromDiskCache(imageKey);

            if (bitmap == null) {
                return null;
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                ImageView imageView = mImageView.get();
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
