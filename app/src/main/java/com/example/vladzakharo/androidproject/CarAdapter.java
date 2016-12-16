package com.example.vladzakharo.androidproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Vlad Zakharo on 15.12.2016.
 */

public class CarAdapter extends RecyclerView.Adapter<CarHolder> {

    public static final String CAR_SERIALIZABLE = "car_title";

    private List<Car> mCars;
    private Context mContext;

    public CarAdapter(List<Car> cars, Context context){
        mCars = cars;
        mContext = context;
    }

    @Override
    public CarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_item, parent, false);
        return new CarHolder(view);
    }

    @Override
    public void onBindViewHolder(CarHolder holder, final int position) {
        final Car car = mCars.get(position);
        holder.mTitleTextView.setText(car.getTitle());
        holder.mDescriptionTextView.setText(car.getDescription());

        holder.mImageView.setImageBitmap(ImageCatcher.getBitmapFromAssets(mContext, car.getNamePicture()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra(CAR_SERIALIZABLE, car);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCars.size();
    }


}
