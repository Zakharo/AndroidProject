package com.example.vladzakharo.androidproject;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Vlad Zakharo on 16.12.2016.
 */

public class ImageCatcher {

    public static Bitmap getBitmapFromAssets(AssetManager assetManager, String nameOfPicture){
        Bitmap bitmap = null;
        try{
            InputStream inputStream = assetManager.open(nameOfPicture);
            bitmap = BitmapFactory.decodeStream(inputStream);

        }catch (IOException ie){
            ie.printStackTrace();
        }
        return bitmap;
    }
}
