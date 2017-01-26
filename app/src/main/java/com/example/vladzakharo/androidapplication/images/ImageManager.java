package com.example.vladzakharo.androidapplication.images;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;

import com.example.vladzakharo.androidapplication.activity.MainActivity;
import com.example.vladzakharo.androidapplication.activity.ProfileActivity;
import com.example.vladzakharo.androidapplication.cache.DiskCache;
import com.example.vladzakharo.androidapplication.cache.ImageCache;
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
        private Context mContext;

        public ImageLoader(Context context) {
            cacheDir = DiskCache.getDiskCacheDir(context, Constants.DISK_CACHE_SUBDIR);
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
            if (mImageLoader.mContext instanceof ProfileActivity) {
                imageView.setImageBitmap(result);
            } else {
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mImageLoader.mContext.getResources(), result);
                roundedBitmapDrawable.setCornerRadius(50.0f);
                roundedBitmapDrawable.setAntiAlias(true);
                imageView.setImageDrawable(roundedBitmapDrawable);
            }
        }
    }
}
