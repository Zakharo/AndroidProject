package com.example.vladzakharo.androidproject;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Vlad Zakharo on 08.01.2017.
 */

public class CarsProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.vladzakharo.provider.cars";
    static final String CAR_PATH = "cars";
    public static final Uri CAR_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CAR_PATH);

    static final int URI_CARS = 1;
    static final int URI_CARS_ID = 2;

    static final String CAR_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CAR_PATH;
    static final String CAR_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + CAR_PATH;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        mUriMatcher.addURI(AUTHORITY, CAR_PATH, URI_CARS);
        mUriMatcher.addURI(AUTHORITY, CAR_PATH + "/#", URI_CARS_ID);
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
            case URI_CARS:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DataBaseConstants.CAR_ID + " ASC";
                    break;
                }
            case URI_CARS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DataBaseConstants.CAR_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DataBaseConstants.CAR_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        mDataBase = mDBHelper.getReadableDatabase();
        Cursor cursor = mDataBase.query(DataBaseConstants.TABLE_CARS, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), CAR_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case URI_CARS:
                return CAR_CONTENT_TYPE;
            case URI_CARS_ID:
                return CAR_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (mUriMatcher.match(uri) != URI_CARS)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        mDataBase = mDBHelper.getWritableDatabase();
        long rowID = mDataBase.insert(DataBaseConstants.TABLE_CARS, null, values);
        Uri resultUri = ContentUris.withAppendedId(CAR_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_CARS:
                break;
            case URI_CARS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DataBaseConstants.CAR_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DataBaseConstants.CAR_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        mDataBase = mDBHelper.getWritableDatabase();
        int cnt = mDataBase.delete(DataBaseConstants.TABLE_CARS, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_CARS:
                break;
            case URI_CARS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DataBaseConstants.CAR_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DataBaseConstants.CAR_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        mDataBase = mDBHelper.getWritableDatabase();
        int cnt = mDataBase.update(DataBaseConstants.TABLE_CARS, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}
