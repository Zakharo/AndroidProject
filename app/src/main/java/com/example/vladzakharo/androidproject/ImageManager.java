package com.example.vladzakharo.androidproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.concurrent.ExecutorService;

/**
 * Created by Vlad Zakharo on 31.12.2016.
 */

public class ImageManager {

    private static final String TAG = "ImageManager";
    private static ImageCache mImageCache = ImageCache.getInstance();
    private static DiskCache mDiskCache;
    private static final ExecutorService EXECUTOR_SERVICE = ImageExecutor.threadPoolExecutor;

    private static ImageManager sImageManager;

    public static ImageManager getInstance() {
        if (sImageManager == null) {
            sImageManager = new ImageManager();
            mImageCache.initializeCache();
        }
        return sImageManager;
    }

    public ImageLoader getImageLoader(Context context) {
        return new ImageLoader(context);
    }

    public static class ImageLoader {
        private String mUrl;
        private WeakReference<ImageView> mImageView;
        private File cacheDir;

        public ImageLoader(Context context) {
            cacheDir = DiskCache.getDiskCacheDir(context, Constants.DISK_CACHE_SUBDIR);
        }

        public ImageLoader from (String url) {
            this.mUrl = url;
            return this;
        }

        public ImageLoader to (ImageView image) {
            this.mImageView = new WeakReference<>(image);
            return this;
        }

        public void load() {
            if (mUrl != null && mImageView != null) {
                new DownloadImageTask(this).executeOnExecutor(EXECUTOR_SERVICE);
            }
        }
    }

    private static class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        private ImageLoader mImageLoader;

        public DownloadImageTask (ImageLoader loader) {
            mImageLoader = loader;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            mDiskCache = DiskCache.getInstance(mImageLoader.cacheDir);
            Bitmap downloadBitmap;
            downloadBitmap = mImageCache.getBitmapFromMemoryCache(mImageLoader.mUrl);
            if (downloadBitmap == null) {
                downloadBitmap = mDiskCache.getBitmapFromDiskCache(mImageLoader.mUrl);
                if (downloadBitmap == null) {
                    try {
                        InputStream in = new URL(mImageLoader.mUrl).openStream();
                        downloadBitmap = BitmapFactory.decodeStream(in);
                    } catch (IOException ioe) {
                        Log.e(TAG, "Something wrong with url", ioe);
                    }
                    mImageCache.addBitmapToMemoryCache(mImageLoader.mUrl, downloadBitmap);
                    mDiskCache.addBitmapToDiskCache(mImageLoader.mUrl, downloadBitmap);
                }
            }
            return downloadBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imageView = mImageLoader.mImageView.get();
            if (imageView == null || result == null) {
                return;
            }
            imageView.setImageBitmap(result);
        }
    }
}
