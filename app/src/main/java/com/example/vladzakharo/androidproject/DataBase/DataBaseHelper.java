package com.example.vladzakharo.androidproject.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vladzakharo.androidproject.DataBase.DataBaseConstants;

/**
 * Created by Vlad Zakharo on 08.01.2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    DataBaseHelper(Context context) {
        super(context, DataBaseConstants.DB_NAME, null, DataBaseConstants.DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DataBaseConstants.TABLE_CARS + "("
                + DataBaseConstants.CAR_ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE, "
                + DataBaseConstants.CAR_TITLE + " TEXT, "
                + DataBaseConstants.CAR_DESCRIPTION + " TEXT, "
                + DataBaseConstants.CAR_IMAGE_URL + " TEXT" + " );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
