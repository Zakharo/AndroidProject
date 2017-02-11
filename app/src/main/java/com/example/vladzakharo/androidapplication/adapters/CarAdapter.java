package com.example.vladzakharo.androidapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vladzakharo.androidapplication.activity.DetailActivity;
import com.example.vladzakharo.androidapplication.activity.FavoriteActivity;
import com.example.vladzakharo.androidapplication.items.Car;
import com.example.vladzakharo.androidapplication.images.ImageManager;
import com.example.vladzakharo.androidapplication.R;

/**
 * Created by Vlad Zakharo on 15.12.2016.
 */

public class CarAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {

    public static final String CAR_NAME_PICTURE = "car_picture";
    public static final String FLAG = "flag";
    private static final int VIEW_TYPE_NORMAL = 1;

    private Context mContext;
    private static ImageManager sImageManager = ImageManager.getInstance();

    public CarAdapter(Context context, Cursor cursor){
        super(context, cursor);
        mContext = context;
    }



    public class CarHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mDescriptionTextView;
        public TextView mLikeCounter;

        public CarHolder(View itemView) {
            super(itemView);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.card_car_message);
            mImageView = (ImageView) itemView.findViewById(R.id.card_car_picture);
            mLikeCounter = (TextView) itemView.findViewById(R.id.card_count_likes);
        }

        private void onBindViewHolder(final Cursor cursor) {
            final Car car = Car.getCarFromCursor(cursor);

            mDescriptionTextView.setText(car.getDescription());
            Drawable placeholder = mContext.getResources().getDrawable(R.drawable.placeholder);
            mImageView.setImageDrawable(placeholder);
            mLikeCounter.setText(String.valueOf(car.getLikes()));

            sImageManager.getImageLoader(mContext)
                    .from(car.getNamePicture())
                    .to(mImageView)
                    .load();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra(CAR_NAME_PICTURE, car.getNamePicture());
                    if (mContext.getClass().equals(FavoriteActivity.class)) {
                        intent.putExtra(FLAG, true);
                    }
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_item, parent, false);
        return new CarHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        if (holder.getItemViewType() == VIEW_TYPE_NORMAL) {
            if (holder instanceof CarHolder) {
                ((CarHolder)holder).onBindViewHolder(cursor);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }
}
