package com.example.vladzakharo.androidproject.items;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vlad Zakharo on 23.01.2017.
 */

public class User implements Parcelable {

    private String mFirstName;
    private String mLastName;
    private String mPicture;

    private User(String firstName, String lastName, String picture) {
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mPicture = picture;
    }

    public User(){

    }

    public User (Parcel parcel){
        mFirstName = parcel.readString();
        mLastName = parcel.readString();
        mPicture = parcel.readString();
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mPicture);
    }

    public static final Parcelable.Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel source) {
            return new Car(source);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
}
