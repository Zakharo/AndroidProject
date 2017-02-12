package com.example.vladzakharo.androidapplication.images;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Vlad Zakharo on 31.01.2017.
 */

public interface Transformer {
    Drawable transform(Bitmap bitmap);
}
