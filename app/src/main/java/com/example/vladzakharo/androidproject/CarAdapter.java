package com.example.vladzakharo.androidproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Vlad Zakharo on 15.12.2016.
 */

public class CarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String CAR_PARCELABLE = "car_title";
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_HEADER = 2;

    private List<Car> mCars;
    public Context mContext;
    private ImageCache mCache;

    private CustomTabsIntent mCustomTabsIntent;
    private CustomTabsIntent.Builder intentBuilder;

    public CarAdapter(List<Car> cars, Context context, ImageCache cache){
        mCars = cars;
        mContext = context;
        mCache = cache;
    }

    public Car getCar(int pos) {
        return mCars.get(pos - 1);
    }

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

        private void onBindViewHolder(int position) {
            final Car car = getCar(position);

            mTitleTextView.setText(car.getTitle());
            mDescriptionTextView.setText(car.getDescription());
            Drawable placeholder = mContext.getResources().getDrawable(R.drawable.placeholder);
            mImageView.setImageDrawable(placeholder);

            Bitmap bitmap = mCache.getBitmapFromMemoryCache(car.getNamePicture());
            if (bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            } else {
                new DownloadImageTask(mImageView, mCache).execute(car.getNamePicture());
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (URLUtil.isValidUrl(car.getDescription())) {
                        intentBuilder = new CustomTabsIntent.Builder();
                        intentBuilder.setToolbarColor(mContext.getResources().getColor(R.color.colorPrimary));
                        intentBuilder.setShowTitle(true);
                        mCustomTabsIntent = intentBuilder.build();
                        mCustomTabsIntent.launchUrl(DetailActivity.activity, Uri.parse(car.getDescription()));

                    } else {
                        Intent intent = new Intent(v.getContext(), DetailActivity.class);
                        intent.putExtra(CAR_PARCELABLE, car);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    public class HeaderItem extends RecyclerView.ViewHolder {
        public HeaderItem(View itemView) {
            super(itemView);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER: {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.header_recyclerview, parent, false);
                return new HeaderItem(view);
            }
            case VIEW_TYPE_NORMAL:
                break;

        }
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_item, parent, false);
        return new CarHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_NORMAL) {
            if (holder instanceof CarHolder) {
                ((CarHolder)holder).onBindViewHolder(position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return mCars.size() + 1;
    }


}
