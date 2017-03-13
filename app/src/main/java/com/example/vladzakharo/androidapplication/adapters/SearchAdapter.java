package com.example.vladzakharo.androidapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.activity.DetailActivity;
import com.example.vladzakharo.androidapplication.cache.DiskCache;
import com.example.vladzakharo.androidapplication.constants.Constants;
import com.example.vladzakharo.androidapplication.images.ImageManager;
import com.example.vladzakharo.androidapplication.items.Post;

import java.io.File;
import java.util.List;

/**
 * Created by Vlad Zakharo on 13.02.2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    public static final String PARCELABLE_POST = "parcelable_post";
    public static final String SEARCH_FLAG = "search_flag";

    private String mTextToColor = "a";
    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<Post> mPosts;
    private File cacheDir;

    public SearchAdapter(Context context, List<Post> posts, RecyclerView recyclerView, String text){
        mContext = context;
        mPosts = posts;
        mRecyclerView = recyclerView;
        mTextToColor = text;
        cacheDir = DiskCache.getDiskCacheDir(mContext, Constants.DISK_CACHE_SUBDIR);
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

        ImageManager.getInstance(cacheDir)
                .getImageLoader(mContext)
                .from(post.getNamePicture())
                .to(holder.mImageView)
                .load();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                Post post = mPosts.get(mRecyclerView.getChildLayoutPosition(v));
                intent.putExtra(SEARCH_FLAG, true);
                intent.putExtra(PARCELABLE_POST, post);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
