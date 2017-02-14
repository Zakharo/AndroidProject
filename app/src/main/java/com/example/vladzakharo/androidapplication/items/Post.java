package com.example.vladzakharo.androidapplication.items;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vlad Zakharo on 13.02.2017.
 */

public class Post implements Parcelable {
    private String mDescription;
    private String mNamePicture;
    private int mLikes;
    private int mPostId;
    private int mOwnerId;
    private int mId;
    private int mIsLiked;

    private Post(int id, int likes, int isLiked, int postId, int ownerId, String description, String namePicture) {
        this.mId = id;
        this.mLikes = likes;
        this.mPostId = postId;
        this.mOwnerId = ownerId;
        this.mDescription = description;
        this.mNamePicture = namePicture;
        this.mIsLiked = isLiked;
    }

    public Post(){

    }

    public Post (Parcel parcel) {
        mPostId = parcel.readInt();
        mDescription = parcel.readString();
        mNamePicture = parcel.readString();
        mLikes = parcel.readInt();
        mId = parcel.readInt();
        mOwnerId = parcel.readInt();
        mIsLiked= parcel.readInt();
    }

    public int getIsCarLiked() {
        return mIsLiked;
    }

    public void setCarLiked(int value) {
        mIsLiked = value;
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
        dest.writeInt(mIsLiked);
    }

    public static final Parcelable.Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
