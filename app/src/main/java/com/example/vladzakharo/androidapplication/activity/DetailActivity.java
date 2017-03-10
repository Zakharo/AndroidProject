package com.example.vladzakharo.androidapplication.activity;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vladzakharo.androidapplication.adapters.SearchAdapter;
import com.example.vladzakharo.androidapplication.cache.DiskCache;
import com.example.vladzakharo.androidapplication.constants.Constants;
import com.example.vladzakharo.androidapplication.database.DBUtils;
import com.example.vladzakharo.androidapplication.database.FavoritesProvider;
import com.example.vladzakharo.androidapplication.adapters.CarAdapter;
import com.example.vladzakharo.androidapplication.database.CarsProvider;
import com.example.vladzakharo.androidapplication.database.DataBaseConstants;
import com.example.vladzakharo.androidapplication.database.SearchSuggestionsProvider;
import com.example.vladzakharo.androidapplication.images.ImageManager;
import com.example.vladzakharo.androidapplication.items.Post;
import com.example.vladzakharo.androidapplication.links.LinkTransformationMethod;
import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.services.ApiServices;
import com.example.vladzakharo.androidapplication.services.UpdateDataService;

import java.io.File;
import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private String mPostNamePicture = null;
    private String mPostDescription = null;
    private TextView mTvDescription;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private Toolbar mToolbar;
    private Activity mActivity;

    private FloatingActionButton mFab;
    private boolean favorite_flag = false;
    private boolean search_flag = false;
    private int mButtonLikeState;
    private Bundle bundle;
    private Post mPost = null;
    private File cacheDir;

    private static final int CAR_LOADER_ID = 2;
    private static final int FAVORITE_LOADER_ID = 3;
    private static final String KEY_POST_NAME_PICTURE = "keyPostPic";
    private static final String KEY_POST_DESCRIPTION = "keyPostDesc";
    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        cacheDir = DiskCache.getDiskCacheDir(getApplicationContext(), Constants.DISK_CACHE_SUBDIR);

        if (getIntent() != null){
            mPostNamePicture = getIntent().getStringExtra(CarAdapter.CAR_NAME_PICTURE);
            mPostDescription = getIntent().getStringExtra(CarAdapter.CAR_DESCRIPTION);
            favorite_flag = getIntent().getBooleanExtra(CarAdapter.FAVOITE_FLAG, false);
            search_flag = getIntent().getBooleanExtra(SearchAdapter.SEARCH_FLAG, false);
            if (search_flag) {
                mPost = getIntent().getParcelableExtra(SearchAdapter.PARCELABLE_POST);
            }
            Intent i = new Intent();

        }

        bundle = new Bundle();
        bundle.putString(KEY_POST_NAME_PICTURE, mPostNamePicture);
        bundle.putString(KEY_POST_DESCRIPTION, mPostDescription);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonLikeState == 0) {
                    ApiServices.getInstance(getApplicationContext()).addLike(mPost.getOwnerId(), mPost.getPostId());
                    DBUtils.addToFavorite(mPost, getApplicationContext());
                    mFab.setImageResource(R.drawable.ic_like_add);
                    Snackbar.make(v, R.string.like, Snackbar.LENGTH_SHORT).show();
                    mButtonLikeState++;
                } else {
                    ApiServices.getInstance(getApplicationContext()).deleteLike(mPost.getOwnerId(), mPost.getPostId());
                    DBUtils.deleteFromFavorite(mPost, getApplicationContext());
                    mFab.setImageResource(R.drawable.ic_like_not_add);
                    Snackbar.make(v, R.string.dislike, Snackbar.LENGTH_SHORT).show();
                    mButtonLikeState--;
                }
                Intent intentService = new Intent(v.getContext(), UpdateDataService.class);
                startService(intentService);
            }
        });

        mToolbar = (Toolbar)findViewById(R.id.detail_toolbar);
        mImageView = (ImageView) findViewById(R.id.toolbar_image);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_description);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.toolbar_title_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTvDescription = (TextView) findViewById(R.id.detail_text_view);
        mActivity = this;

        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (favorite_flag) {
            getSupportLoaderManager().initLoader(FAVORITE_LOADER_ID, bundle, this);
        } else if (search_flag) {
            mTvDescription.setText(mPost.getDescription());
            mTvDescription.setTransformationMethod(new LinkTransformationMethod(mActivity));
            mTvDescription.setMovementMethod(LinkMovementMethod.getInstance());
            ImageManager.getInstance(cacheDir).getImageLoader(this)
                    .from(mPost.getNamePicture())
                    .to(mImageView)
                    .load();

            if (mPost.getIsPostLiked() == 1) {
                mFab.setImageResource(R.drawable.ic_like_add);
            }
            mButtonLikeState = mPost.getIsPostLiked();
            getSupportActionBar().setTitle(R.string.toolbar_title_search);
            mProgressBar.setVisibility(View.GONE);
        } else {
            getSupportLoaderManager().initLoader(CAR_LOADER_ID, bundle, this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == CAR_LOADER_ID) {
            if (mPostNamePicture == null) {
                return new CursorLoader(this, CarsProvider.CAR_CONTENT_URI, null, DataBaseConstants.CARS_POST_DESCRIPTION + " = ?", new String[]{String.valueOf(args.get(KEY_POST_DESCRIPTION))}, null);
            } else {
                return new CursorLoader(this, CarsProvider.CAR_CONTENT_URI, null, DataBaseConstants.CARS_POST_IMAGE_URL + " = ?", new String[]{String.valueOf(args.get(KEY_POST_NAME_PICTURE))}, null);
            }
        }else if (id == FAVORITE_LOADER_ID) {
            return new CursorLoader(this, FavoritesProvider.FAVORITE_CAR_CONTENT_URI, null, DataBaseConstants.FAVORITES_POST_IMAGE_URL + " = ?", new String[]{String.valueOf(args.get(KEY_POST_NAME_PICTURE))}, null);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProgressBar.setVisibility(View.GONE);
        if (data == null || !data.moveToFirst()) {
            return;
        }

        if (loader.getId() == CAR_LOADER_ID) {
            mPost = Post.getPostFromCarsDb(data);
        } else {
            mPost = Post.getPostFromFavoritesDb(data);
        }
        mTvDescription.setText(mPost.getDescription());
        mTvDescription.setTransformationMethod(new LinkTransformationMethod(mActivity));
        mTvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        ImageManager.getInstance(cacheDir).getImageLoader(this)
                .from(mPost.getNamePicture())
                .to(mImageView)
                .load();
        if (mPost.getIsPostLiked() == 1) {
            mFab.setImageResource(R.drawable.ic_like_add);
        }
        mButtonLikeState = mPost.getIsPostLiked();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
