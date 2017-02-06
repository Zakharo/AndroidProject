package com.example.vladzakharo.androidapplication.items;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vladzakharo.androidapplication.database.DataBaseConstants;

/**
 * Created by Vlad Zakharo on 15.12.2016.
 */

public class Car implements Parcelable{
    private String mDescription;
    private String mNamePicture;
    private int mLikes;
    private int mPostId;
    private int mOwnerId;
    private int mId;

    private Car(int id, int likes, int postId, int ownerId, String description, String namePicture) {
        this.mId = id;
        this.mLikes = likes;
        this.mPostId = postId;
        this.mOwnerId = ownerId;
        this.mDescription = description;
        this.mNamePicture = namePicture;
    }

    public Car(){

    }

    public Car (Parcel parcel){
        mPostId = parcel.readInt();
        mDescription = parcel.readString();
        mNamePicture = parcel.readString();
        mLikes = parcel.readInt();
        mId = parcel.readInt();
        mOwnerId = parcel.readInt();
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

    public int getPostId() {
        return mPostId;
    }

    public void setPostId(int postId) {
        mPostId = postId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getLikes() {
        return mLikes;
    }

    public void setLikes(int likes) {
        mLikes = likes;
    }

    public void setOwnerId(int ownerId) {
        mOwnerId = ownerId;
    }

    public int getOwnerId() {
        return mOwnerId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mPostId);
        dest.writeString(mDescription);
        dest.writeString(mNamePicture);
        dest.writeInt(mLikes);
        dest.writeInt(mId);
        dest.writeInt(mOwnerId);
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
        return new Car(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.CARS_CAR_ID)),
                cursor.getInt(cursor.getColumnIndex(DataBaseConstants.CARS_CAR_LIKES)),
                cursor.getInt(cursor.getColumnIndex(DataBaseConstants.CARS_CAR_POST_ID)),
                cursor.getInt(cursor.getColumnIndex(DataBaseConstants.CARS_CAR_POST_OWNER_ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.CARS_CAR_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.CARS_CAR_IMAGE_URL)));
    }
}
