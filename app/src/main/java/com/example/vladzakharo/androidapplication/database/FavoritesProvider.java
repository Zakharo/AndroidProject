package com.example.vladzakharo.androidapplication.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by Vlad Zakharo on 06.02.2017.
 */

public class FavoritesProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.vladzakharo.provider.favorites";
    static final String FAVORITE_PATH = "favorite";
    public static final Uri FAVORITE_CAR_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + FAVORITE_PATH);

    static final int URI_FAVORITE = 1;
    static final int URI_FAVORITE_ID = 2;

    static final String FAVORITE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + FAVORITE_PATH;
    static final String FAVORITE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + FAVORITE_PATH;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        mUriMatcher.addURI(AUTHORITY, FAVORITE_PATH, URI_FAVORITE);
        mUriMatcher.addURI(AUTHORITY, FAVORITE_PATH + "/#", URI_FAVORITE_ID);
    }

    private DataBaseHelper mDBHelper;
    private SQLiteDatabase mDataBase;

    @Override
    public boolean onCreate() {
        mDBHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (mUriMatcher.match(uri)) {
            case URI_FAVORITE:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DataBaseConstants.FAVORITES_CAR_ID + " ASC";
                }
                break;
            case URI_FAVORITE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DataBaseConstants.FAVORITES_CAR_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DataBaseConstants.FAVORITES_CAR_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        mDataBase = mDBHelper.getReadableDatabase();
        Cursor cursor = mDataBase.query(DataBaseConstants.FAVORITES_TABLE_CARS, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), FAVORITE_CAR_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case URI_FAVORITE:
                return FAVORITE_CONTENT_TYPE;
            case URI_FAVORITE_ID:
                return FAVORITE_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (mUriMatcher.match(uri) != URI_FAVORITE)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        mDataBase = mDBHelper.getWritableDatabase();
        long rowID = mDataBase.insert(DataBaseConstants.FAVORITES_TABLE_CARS, null, values);
        Uri resultUri = ContentUris.withAppendedId(FAVORITE_CAR_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_FAVORITE:
                break;
            case URI_FAVORITE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DataBaseConstants.FAVORITES_CAR_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DataBaseConstants.FAVORITES_CAR_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        mDataBase = mDBHelper.getWritableDatabase();
        int cnt = mDataBase.delete(DataBaseConstants.FAVORITES_TABLE_CARS, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_FAVORITE:
                break;
            case URI_FAVORITE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DataBaseConstants.FAVORITES_CAR_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DataBaseConstants.FAVORITES_CAR_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        mDataBase = mDBHelper.getWritableDatabase();
        int cnt = mDataBase.update(DataBaseConstants.FAVORITES_TABLE_CARS, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}
