package com.example.vladzakharo.androidproject;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Vlad Zakharo on 16.12.2016.
 */

public class ImageCatcher {
    private static Bitmap bitmap;

    public static Bitmap getBitmapFromAssets(Context context, String nameOfPicture){
        AssetManager assetManager = context.getAssets();
        try{
            InputStream inputStream = assetManager.open(nameOfPicture);
            bitmap = BitmapFactory.decodeStream(inputStream);

        }catch (IOException ie){
            ie.printStackTrace();
        }
        return bitmap;
    }
}
