package com.example.vladzakharo.androidapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vlad Zakharo on 08.01.2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    DataBaseHelper(Context context) {
        super(context, DataBaseConstants.DB_NAME, null, DataBaseConstants.DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DataBaseConstants.CARS_TABLE_CARS + "("
                + DataBaseConstants.CARS_CAR_ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE, "
                + DataBaseConstants.CARS_CAR_LIKES + " INTEGER, "
                + DataBaseConstants.CARS_CAR_POST_ID + " INTEGER, "
                + DataBaseConstants.CARS_CAR_POST_OWNER_ID + " INTEGER, "
                + DataBaseConstants.CARS_CAR_DESCRIPTION + " TEXT, "
                + DataBaseConstants.CARS_CAR_IMAGE_URL + " TEXT" + " );");


        db.execSQL("CREATE TABLE " + DataBaseConstants.FAVORITES_TABLE_CARS + "("
                + DataBaseConstants.FAVORITES_CAR_ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE, "
                + DataBaseConstants.FAVORITES_CAR_LIKES + " INTEGER, "
                + DataBaseConstants.FAVORITES_CAR_POST_ID + " INTEGER, "
                + DataBaseConstants.FAVORITES_CAR_POST_OWNER_ID + " INTEGER, "
                + DataBaseConstants.FAVORITES_CAR_DESCRIPTION + " TEXT, "
                + DataBaseConstants.FAVORITES_CAR_IMAGE_URL + " TEXT" + " );");

        db.execSQL("CREATE TABLE " + DataBaseConstants.USER_TABLE_USER + "("
                + DataBaseConstants.USER_ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE, "
                + DataBaseConstants.USER_FIRST_NAME +  "TEXT, "
                + DataBaseConstants.USER_LAST_NAME + " TEXT, "
                + DataBaseConstants.USER_PICTURE + " TEXT, "
                + DataBaseConstants.USER_FULL_PHOTO + " TEXT, "
                + DataBaseConstants.USER_DATE_OF_BIRTH + " TEXT, "
                + DataBaseConstants.USER_HOMETOWN + " TEXT" + " );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
