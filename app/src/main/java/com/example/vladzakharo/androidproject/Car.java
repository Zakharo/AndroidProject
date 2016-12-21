package com.example.vladzakharo.androidproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vlad Zakharo on 15.12.2016.
 */

public class Car implements Parcelable{
    private String mTitle;
    private String mDescription;
    private String mNamePicture;

    public Car(){

    }

    public Car (String title, String description, String namePicture){
        mTitle = title;
        mDescription = description;
        mNamePicture = namePicture;
    }

    public Car (Parcel parcel){
        String[] data = new String[3];
        parcel.readStringArray(data);
        mTitle = data[0];
        mDescription = data[1];
        mNamePicture = data[2];
    }

    public String getNamePicture() {
        return mNamePicture;
    }

    public void setNamePicture(String name) {
        mNamePicture = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {mTitle, mDescription, mNamePicture});
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
