package com.example.vladzakharo.androidproject.adapters;

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

import com.example.vladzakharo.androidproject.activity.DetailActivity;
import com.example.vladzakharo.androidproject.items.Car;
import com.example.vladzakharo.androidproject.images.ImageManager;
import com.example.vladzakharo.androidproject.R;

/**
 * Created by Vlad Zakharo on 15.12.2016.
 */

public class CarAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {

    public static final String CAR_ID = "car_id";
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_HEADER = 2;

    private Context mContext;
    //private RecyclerView mRecyclerView;

    private static ImageManager sImageManager = ImageManager.getInstance();

    public CarAdapter(Context context, Cursor cursor){
        super(context, cursor);
        mContext = context;
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

        private void onBindViewHolder(final Cursor cursor) {
            final Car car = Car.getCarFromCursor(cursor);

            mTitleTextView.setText(car.getTitle());
            mDescriptionTextView.setText(car.getDescription());
            Drawable placeholder = mContext.getResources().getDrawable(R.drawable.placeholder);
            mImageView.setImageDrawable(placeholder);

            sImageManager.getImageLoader(mContext)
                    .from(car.getNamePicture())
                    .to(mImageView)
                    .load();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra(CAR_ID, getPosition());
                    v.getContext().startActivity(intent);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        if (holder.getItemViewType() == VIEW_TYPE_NORMAL) {
            if (holder instanceof CarHolder) {
                ((CarHolder)holder).onBindViewHolder(cursor);
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
}
