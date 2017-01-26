package com.example.vladzakharo.androidproject.items;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vladzakharo.androidproject.database.DataBaseConstants;

/**
 * Created by Vlad Zakharo on 15.12.2016.
 */

public class Car implements Parcelable{
    private String mTitle;
    private String mDescription;
    private String mNamePicture;
    private int mId;

    private Car(int id, String title, String description, String namePicture) {
        this.mId = id;
        this.mTitle = title;
        this.mDescription = description;
        this.mNamePicture = namePicture;
    }

    public Car(){

    }

    public Car (Parcel parcel){
        mTitle = parcel.readString();
        mDescription = parcel.readString();
        mNamePicture = parcel.readString();
        mId = parcel.readInt();
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

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mNamePicture);
        dest.writeInt(mId);
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

    public static Car getCarFromCursor(Cursor cursor) {
        return new Car(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.CAR_ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.CAR_TITLE)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.CAR_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.CAR_IMAGE_URL)));
    }
}
