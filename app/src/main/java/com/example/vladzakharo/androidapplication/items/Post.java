package com.example.vladzakharo.androidapplication.items;

/**
 * Created by Vlad Zakharo on 13.02.2017.
 */

public class Post {
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
}
