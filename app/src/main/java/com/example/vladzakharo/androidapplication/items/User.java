package com.example.vladzakharo.androidapplication.items;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vladzakharo.androidapplication.database.DataBaseConstants;

/**
 * Created by Vlad Zakharo on 23.01.2017.
 */

public class User {

    private int mId;
    private String mFirstName;
    private String mLastName;
    private String mPicture;
    private String mFullPhoto;
    private String mDateOfBirth;
    private String mHomeTown;

    public User(){

    }

    private User(int id, String firstName, String lastName, String picture, String fullPhoto, String dateOfBirth, String homeTown) {
        this.mId = id;
        this.mLastName = lastName;
        this.mFirstName = firstName;
        this.mPicture = picture;
        this.mFullPhoto = fullPhoto;
        this.mDateOfBirth = dateOfBirth;
        this.mHomeTown = homeTown;
    }

    public int getId() {
        return 1;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getDateOfBirth() {
        return mDateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        mDateOfBirth = dateOfBirth;
    }

    public String getHomeTown() {
        return mHomeTown;
    }

    public void setHomeTown(String homeTown) {
        mHomeTown = homeTown;
    }

    public String getFullPhoto() {
        return mFullPhoto;
    }

    public void setFullPhoto(String fullPhoto) {
        mFullPhoto = fullPhoto;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String name) {
        mPicture = name;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    public static User getUserFromCursor(Cursor cursor) {
        return new User(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.USER_ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER_PICTURE)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER_FULL_PHOTO)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER_DATE_OF_BIRTH)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER_HOMETOWN)));
    }
}
