package com.example.vladzakharo.androidapplication.activity;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
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
import com.example.vladzakharo.androidapplication.database.FavoritesProvider;
import com.example.vladzakharo.androidapplication.items.Car;
import com.example.vladzakharo.androidapplication.adapters.CarAdapter;
import com.example.vladzakharo.androidapplication.database.CarsProvider;
import com.example.vladzakharo.androidapplication.database.DataBaseConstants;
import com.example.vladzakharo.androidapplication.images.ImageManager;
import com.example.vladzakharo.androidapplication.items.Post;
import com.example.vladzakharo.androidapplication.links.LinkTransformationMethod;
import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.services.ApiServices;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private String mCarNamePicture = null;
    private TextView mTvDescription;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private Toolbar mToolbar;
    private Activity mActivity;
    private Car mCar;
    private FloatingActionButton mFab;
    private boolean favorite_flag = false;
    private int mButtonLikeState;
    private Bundle bundle;
    private Post mPost = null;

    private static final int CAR_LOADER_ID = 2;
    private static final int FAVORITE_LOADER_ID = 3;
    private static final String KEY_CAR_NAME_PICTURE = "keyCarPic";
    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getIntent() != null){
            mCarNamePicture = getIntent().getStringExtra(CarAdapter.CAR_NAME_PICTURE);
            favorite_flag = getIntent().getBooleanExtra(CarAdapter.FAVOITE_FLAG, false);
            mPost = getIntent().getParcelableExtra(SearchAdapter.PARCELABLE_POST);
        }

        bundle = new Bundle();
        bundle.putString(KEY_CAR_NAME_PICTURE, mCarNamePicture);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPost != null) {
                    if (mButtonLikeState == 0) {
                        new ApiServices(getApplicationContext()).addLike(mPost.getOwnerId(), mPost.getPostId());
                        //addPostToFavorite(mPost);
                        mFab.setImageResource(R.drawable.ic_like_add);
                        Snackbar.make(v, R.string.like, Snackbar.LENGTH_SHORT).show();
                        mButtonLikeState++;
                    } else {
                        new ApiServices(getApplicationContext()).deleteLike(mPost.getOwnerId(), mPost.getPostId());
                        //deletePostFromFavorite();
                        mFab.setImageResource(R.drawable.ic_like_not_add);
                        Snackbar.make(v, R.string.dislike, Snackbar.LENGTH_SHORT).show();
                        mButtonLikeState--;
                    }
                } else {
                    if (mButtonLikeState == 0) {
                        new ApiServices(getApplicationContext()).addLike(mCar.getOwnerId(), mCar.getPostId());
                        addCarToFavorite(mCar);
                        mFab.setImageResource(R.drawable.ic_like_add);
                        Snackbar.make(v, R.string.like, Snackbar.LENGTH_SHORT).show();
                        mButtonLikeState++;
                    } else {
                        new ApiServices(getApplicationContext()).deleteLike(mCar.getOwnerId(), mCar.getPostId());
                        deleteCarFromFavorite();
                        mFab.setImageResource(R.drawable.ic_like_not_add);
                        Snackbar.make(v, R.string.dislike, Snackbar.LENGTH_SHORT).show();
                        mButtonLikeState--;
                    }
                }
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
        } else {
            if (mPost != null) {
                mTvDescription.setText(mPost.getDescription());
                mTvDescription.setTransformationMethod(new LinkTransformationMethod(mActivity));
                mTvDescription.setMovementMethod(LinkMovementMethod.getInstance());
                ImageManager.getInstance().getImageLoader(this)
                        .from(mPost.getNamePicture())
                        .to(mImageView)
                        .load();

                if (mPost.getIsCarLiked() == 1) {
                    mFab.setImageResource(R.drawable.ic_like_add);
                }
                mButtonLikeState = mPost.getIsCarLiked();
                getSupportActionBar().setTitle("Awesome News");
            }
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
            return new CursorLoader(this, CarsProvider.CAR_CONTENT_URI, null, DataBaseConstants.CARS_CAR_IMAGE_URL + " = ?", new String[]{String.valueOf(args.get(KEY_CAR_NAME_PICTURE))}, null);
        }else if (id == FAVORITE_LOADER_ID) {
            return new CursorLoader(this, FavoritesProvider.FAVORITE_CAR_CONTENT_URI, null, DataBaseConstants.FAVORITES_CAR_IMAGE_URL + " = ?", new String[]{String.valueOf(args.get(KEY_CAR_NAME_PICTURE))}, null);
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
        mCar = Car.getCarFromCursor(data);
        mTvDescription.setText(mCar.getDescription());
        mTvDescription.setTransformationMethod(new LinkTransformationMethod(mActivity));
        mTvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        ImageManager.getInstance().getImageLoader(this)
                .from(mCar.getNamePicture())
                .to(mImageView)
                .load();
        if (mCar.getIsCarLiked() == 1) {
            mFab.setImageResource(R.drawable.ic_like_add);
        }

        mButtonLikeState = mCar.getIsCarLiked();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void addCarToFavorite(Car car) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        operations.add(ContentProviderOperation.newInsert(FavoritesProvider.FAVORITE_CAR_CONTENT_URI)
                .withValue(DataBaseConstants.FAVORITES_CAR_ID, car.getId())
                .withValue(DataBaseConstants.FAVORITES_CAR_LIKES, car.getLikes())
                .withValue(DataBaseConstants.FAVORITES_CAR_IS_LIKED, 1)
                .withValue(DataBaseConstants.FAVORITES_CAR_POST_ID, car.getPostId())
                .withValue(DataBaseConstants.FAVORITES_CAR_POST_OWNER_ID, car.getOwnerId())
                .withValue(DataBaseConstants.FAVORITES_CAR_DESCRIPTION, car.getDescription())
                .withValue(DataBaseConstants.FAVORITES_CAR_IMAGE_URL, car.getNamePicture())
                .build());


        ArrayList<ContentProviderOperation> update = new ArrayList<>();
        update.add(ContentProviderOperation.newUpdate(CarsProvider.CAR_CONTENT_URI)
                .withSelection(DataBaseConstants.CARS_CAR_IMAGE_URL + " = ?", new String[]{String.valueOf(mCarNamePicture)})
                .withValue(DataBaseConstants.CARS_CAR_IS_LIKED, 1)
                .build());
        try {
            getContentResolver().applyBatch(FavoritesProvider.AUTHORITY, operations);
            getContentResolver().applyBatch(CarsProvider.AUTHORITY, update);
        } catch (RemoteException | OperationApplicationException re) {
            Log.e(TAG, "applyBatch()", re);
        }
    }

    private void deleteCarFromFavorite() {
        ArrayList<ContentProviderOperation> deleteFromFavorites = new ArrayList<>();
        deleteFromFavorites.add(ContentProviderOperation.newDelete(FavoritesProvider.FAVORITE_CAR_CONTENT_URI)
                .withSelection(DataBaseConstants.FAVORITES_CAR_IMAGE_URL + " = ?", new String[]{String.valueOf(mCarNamePicture)})
                .build());

        ArrayList<ContentProviderOperation> deleteFromCars = new ArrayList<>();
        deleteFromCars.add(ContentProviderOperation.newDelete(CarsProvider.CAR_CONTENT_URI)
                .withSelection(DataBaseConstants.CARS_CAR_IMAGE_URL + " = ?", new String[]{String.valueOf(mCarNamePicture)})
                .build());
        try {
            getContentResolver().applyBatch(FavoritesProvider.AUTHORITY, deleteFromFavorites);
            getContentResolver().applyBatch(CarsProvider.AUTHORITY, deleteFromCars);
        } catch (RemoteException | OperationApplicationException re) {
            Log.e(TAG, "applyBatch()", re);
        }
    }
}
