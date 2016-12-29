package com.example.vladzakharo.androidproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Vlad Zakharo on 26.12.2016.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private String urlImage;
    ImageView bmImage;
    private ImageCache mCache;

    public DownloadImageTask(ImageView bmImage, ImageCache cache) {
        this.bmImage = bmImage;
        this.mCache = cache;
    }

    @Override
    protected Bitmap doInBackground(String... url) {
        urlImage = url[0];
        Bitmap mIcon = null;
        try {
            InputStream in = new URL(urlImage).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return mIcon;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            mCache.addBitmapToMemoryCache(urlImage, bitmap);
            bmImage.setImageBitmap(bitmap);
        }
    }
}