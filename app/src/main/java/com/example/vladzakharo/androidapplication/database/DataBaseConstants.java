package com.example.vladzakharo.androidapplication.database;

/**
 * Created by Vlad Zakharo on 08.01.2017.
 */

public class DataBaseConstants {
    public static final String DB_NAME = "DBcars";
    public static final int DB_VERSION = 1;

    public static final String CARS_TABLE_CARS = "cars";
    public static final String CARS_POST_ID = "_id";
    public static final String CARS_POST_POST_ID = "post_id";
    public static final String CARS_POST_OWNER_ID = "post_owner_id";
    public static final String CARS_POST_DESCRIPTION = "description";
    public static final String CARS_POST_IMAGE_URL = "image_url";
    public static final String CARS_POST_LIKES = "likes";
    public static final String CARS_POST_IS_LIKED = "is_liked";

    public static final String FAVORITES_TABLE_CARS = "favorites";
    public static final String FAVORITES_POST_ID = "_id";
    public static final String FAVORITES_POST_POST_ID = "post_id";
    public static final String FAVORITES_POST_OWNER_ID = "post_owner_id";
    public static final String FAVORITES_POST_DESCRIPTION = "description";
    public static final String FAVORITES_POST_IMAGE_URL = "image_url";
    public static final String FAVORITES_POST_LIKES = "likes";
    public static final String FAVORITES_POST_IS_LIKED = "is_liked";

    public static final String USER_TABLE_USER = "user";
    public static final String USER_ID = "_id";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_PICTURE = "picture";
    public static final String USER_FULL_PHOTO = "full_photo";
    public static final String USER_DATE_OF_BIRTH = "date_of_birth";
    public static final String USER_HOMETOWN = "hometown";

}
