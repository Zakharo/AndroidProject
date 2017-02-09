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

/**
 * Created by Vlad Zakharo on 08.02.2017.
 */

public class UserProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.vladzakharo.provider.users";
    static final String USER_PATH = "user";
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + USER_PATH);

    static final int URI_USER = 1;
    static final int URI_USER_ID = 2;

    static final String USER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + USER_PATH;
    static final String USER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + USER_PATH;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        mUriMatcher.addURI(AUTHORITY, USER_PATH, URI_USER);
        mUriMatcher.addURI(AUTHORITY, USER_PATH + "/#", URI_USER_ID);
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
            case URI_USER:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DataBaseConstants.USER_ID + " ASC";
                }
                break;
            case URI_USER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DataBaseConstants.USER_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DataBaseConstants.USER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        mDataBase = mDBHelper.getReadableDatabase();
        Cursor cursor = mDataBase.query(DataBaseConstants.USER_TABLE_USER, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), USER_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case URI_USER:
                return USER_CONTENT_TYPE;
            case URI_USER_ID:
                return USER_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (mUriMatcher.match(uri) != URI_USER)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        mDataBase = mDBHelper.getWritableDatabase();
        long rowID = mDataBase.insert(DataBaseConstants.USER_TABLE_USER, null, values);
        Uri resultUri = ContentUris.withAppendedId(USER_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_USER:
                break;
            case URI_USER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DataBaseConstants.USER_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DataBaseConstants.USER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        mDataBase = mDBHelper.getWritableDatabase();
        int cnt = mDataBase.delete(DataBaseConstants.USER_TABLE_USER, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_USER:
                break;
            case URI_USER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DataBaseConstants.USER_ID + " = " + id;
                } else {
                    selection = selection + " AND " + DataBaseConstants.USER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        mDataBase = mDBHelper.getWritableDatabase();
        int cnt = mDataBase.update(DataBaseConstants.USER_TABLE_USER, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}
