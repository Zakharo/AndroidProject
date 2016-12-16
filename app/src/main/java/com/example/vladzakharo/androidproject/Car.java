package com.example.vladzakharo.androidproject;

import java.io.Serializable;

/**
 * Created by Vlad Zakharo on 15.12.2016.
 */

public class Car implements Serializable{
    private String mTitle;
    private String mDescription;
    private String mNamePicture;

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


}
