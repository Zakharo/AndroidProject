package com.example.vladzakharo.androidproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;

/**
 * Created by Vlad Zakharo on 31.12.2016.
 */

public class Loader {

    private static final String TAG = "Loader";
    private static ImageCache mImageCache;
    private static final ExecutorService EXECUTOR_SERVICE = ImageExecutor.threadPoolExecutor;

    private Loader() {
    }

    public static ImageLoader getImageLoader() {
        return new ImageLoader();
    }

    public static class ImageLoader {
        private String mUrl;
        private WeakReference<ImageView> mImageView;

        public ImageLoader() {
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
            mImageCache = ImageCache.getInstance();
            Bitmap downloadBitmap = null;
            try {
                InputStream in = new URL(mImageLoader.mUrl).openStream();
                downloadBitmap = BitmapFactory.decodeStream(in);
            } catch (IOException ioe) {
                Log.e(TAG, "Something wrong with url", ioe);
            }
            mImageCache.addBitmapToMemoryCache(mImageLoader.mUrl, downloadBitmap);
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
