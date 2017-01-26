package com.example.vladzakharo.androidapplication.items;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vlad Zakharo on 23.01.2017.
 */

public class User {

    private String mFirstName;
    private String mLastName;
    private String mPicture;
    private String mFullPhoto;
    private String mDateOfBirth;
    private String mHomeTown;

    public User(){

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
}
