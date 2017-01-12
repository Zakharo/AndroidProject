package com.example.vladzakharo.androidproject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String ARG_CAR_ID = "car";

    //private Car mCar;
    private int mCarId;
    private TextView mTextView;
    private static final int LOADER_ID = 1;
    private static final String KEY_CAR_ID = "keyCarId";

    public static DetailFragment newInstance(int carId){
        Bundle arg = new Bundle();
        arg.putInt(ARG_CAR_ID, carId);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            mCarId = (int)getArguments().get(ARG_CAR_ID);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_CAR_ID, mCarId);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mTextView = (TextView) view.findViewById(R.id.detail_text_view);
        /*mTextView.setText(mCar.getDescription());
        Activity activity = getActivity();
        mTextView.setTransformationMethod(new LinkTransformationMethod(activity));
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());*/
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_ID) {
            return null;
        }
        return new CursorLoader(getContext(), CarsProvider.CAR_CONTENT_URI, null, DataBaseConstants.CAR_ID + " = ?", new String[]{String.valueOf(args.get(KEY_CAR_ID))}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Car car = Car.getCarFromCursor(data);

        mTextView.setText(car.getDescription());
        Activity activity = getActivity();
        mTextView.setTransformationMethod(new LinkTransformationMethod(activity));
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
