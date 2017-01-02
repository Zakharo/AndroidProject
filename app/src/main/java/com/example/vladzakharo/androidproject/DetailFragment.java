package com.example.vladzakharo.androidproject;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Vlad Zakharo on 15.12.2016.
 */

public class DetailFragment extends Fragment {

    private static final String ARG_CAR = "car";

    private Car mCar;
    private TextView mTextView;

    public static DetailFragment newInstance(Car car){
        Bundle arg = new Bundle();
        arg.putParcelable(ARG_CAR, car);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            mCar = (Car)getArguments().get(ARG_CAR);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mTextView = (TextView) view.findViewById(R.id.detail_text_view);
        mTextView.setText(mCar.getDescription());
        mTextView.setTransformationMethod(new LinkTransformationMethod());
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }
}
