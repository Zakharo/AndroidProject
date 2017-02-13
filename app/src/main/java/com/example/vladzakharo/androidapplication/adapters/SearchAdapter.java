package com.example.vladzakharo.androidapplication.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.images.ImageManager;
import com.example.vladzakharo.androidapplication.items.Post;

import java.util.List;

/**
 * Created by Vlad Zakharo on 13.02.2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    public static final String CAR_NAME_PICTURE = "car_picture";
    public static final String FLAG = "flag";
    private static final int VIEW_TYPE_NORMAL = 1;

    private Context mContext;
    private List<Post> mPosts;
    private static ImageManager sImageManager = ImageManager.getInstance();

    public SearchAdapter(Context context, List<Post> posts){
        mContext = context;
        mPosts = posts;
    }

    public class SearchHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mDescriptionTextView;
        public TextView mLikeCounter;

        public SearchHolder(View itemView) {
            super(itemView);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.card_car_message);
            mImageView = (ImageView) itemView.findViewById(R.id.card_car_picture);
            mLikeCounter = (TextView) itemView.findViewById(R.id.card_count_likes);
        }
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item, parent, false);
        return new SearchHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.mDescriptionTextView.setText(post.getDescription());

        Drawable placeholder = mContext.getResources().getDrawable(R.drawable.placeholder);
        holder.mImageView.setImageDrawable(placeholder);

        holder.mLikeCounter.setText(String.valueOf(post.getLikes()));

        sImageManager.getImageLoader(mContext)
                .from(post.getNamePicture())
                .to(holder.mImageView)
                .load();
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
