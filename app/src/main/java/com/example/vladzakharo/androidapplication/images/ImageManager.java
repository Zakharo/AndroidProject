package com.example.vladzakharo.androidapplication.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.vladzakharo.androidapplication.cache.DiskCache;
import com.example.vladzakharo.androidapplication.cache.ImageCache;
import com.example.vladzakharo.androidapplication.cache.MemoryCache;
import com.example.vladzakharo.androidapplication.constants.Constants;

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

    private static final ExecutorService EXECUTOR_SERVICE = ImageExecutor.threadPoolExecutor;
    private static final String TAG = "ImageManager";
    private DiskCache mDiskCache;
    private MemoryCache mMemoryCache = MemoryCache.getInstance();

    private static ImageManager sImageManager;

    public static ImageManager getInstance(File cacheDir) {
        if (sImageManager == null) {
            sImageManager = new ImageManager(cacheDir);
        }
        return sImageManager;
    }

    private ImageManager(File cacheDir) {
        mMemoryCache.initializeCache();
        mDiskCache = DiskCache.getInstance(cacheDir);
    }

    private ImageManager() {

    }

    public ImageLoader getImageLoader(Context context) {
        return new ImageLoader(context);
    }

    public static class ImageLoader {
        private String mUrl;
        private WeakReference<ImageView> mImageView;
        private Context mContext;
        private Transformer mTransformation = null;

        public ImageLoader(Context context) {
            mContext = context;
        }

        public ImageLoader from (String url) {
            this.mUrl = url;
            return this;
        }

        public ImageLoader to (ImageView image) {
            this.mImageView = new WeakReference<>(image);
            return this;
        }

        public ImageLoader transform(Transformer transform) {
            this.mTransformation = transform;
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
            Bitmap downloadBitmap;
            downloadBitmap = sImageManager.mMemoryCache.getFromCache(mImageLoader.mUrl);
            if (downloadBitmap == null) {
                downloadBitmap = sImageManager.mDiskCache.getFromCache(mImageLoader.mUrl);
                if (downloadBitmap == null) {
                    if (isNetworkConnected()) {
                        try {
                            InputStream in = new URL(mImageLoader.mUrl).openStream();
                            downloadBitmap = BitmapFactory.decodeStream(in);
                        } catch (IOException ioe) {
                            Log.e(TAG, "Something wrong with url", ioe);
                        }
                        sImageManager.mMemoryCache.addToCache(mImageLoader.mUrl, downloadBitmap);
                        sImageManager.mDiskCache.addToCache(mImageLoader.mUrl, downloadBitmap);
                    }
                }
            }
            return downloadBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Transformer transformer = mImageLoader.mTransformation;
            ImageView imageView = mImageLoader.mImageView.get();
            if (imageView == null || result == null) {
                return;
            }
            if (transformer != null) {
                Drawable drawable = transformer.transform(result);
                imageView.setImageDrawable(drawable);
            } else {
                imageView.setImageBitmap(result);
            }
            Log.d(TAG, "image loaded");
        }

        private boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) mImageLoader.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null;
        }
    }
}
