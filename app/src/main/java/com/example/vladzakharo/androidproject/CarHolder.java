package com.example.vladzakharo.androidproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Vlad Zakharo on 15.12.2016.
 */

public class CarHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;
    public TextView mTitleTextView;
    public TextView mDescriptionTextView;

    public CarHolder(View itemView) {
        super(itemView);

        mTitleTextView = (TextView) itemView.findViewById(R.id.car_title);
        mDescriptionTextView = (TextView) itemView.findViewById(R.id.car_description);
        mImageView = (ImageView) itemView.findViewById(R.id.car_picture);
    }

}
