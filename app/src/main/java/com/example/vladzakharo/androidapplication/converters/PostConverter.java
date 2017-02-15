package com.example.vladzakharo.androidapplication.converters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vladzakharo.androidapplication.items.Post;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vlad Zakharo on 05.01.2017.
 */

public class PostConverter implements Converter<Post> {
    private static final String TAG = "PostConverter";

    private static final String TEXT = "text";
    private static final String ATTACHMENTS = "attachments";
    private static final String PHOTO = "photo";
    private static final String LINK = "link";
    private static final String IMAGE_NAME = "photo_604";
    private static final String VIDEO = "video";
    private static final String LIKES = "likes";
    private static final String LIKES_COUNT = "count";
    private static final String POST_ID = "id";
    private static final String OWNER_ID = "owner_id";
    private static final String USER_LIKES = "user_likes";

    private PrefManager mPrefManager;

    public PostConverter(PrefManager prefManager) {
        mPrefManager = prefManager;
    }

    @Nullable
    @Override
    public Post convert(JSONObject jsonObject) {
        Post post = new Post();
        try {
            post.setId(mPrefManager.getPostId());
            mPrefManager.setPostId(mPrefManager.getPostId() + 1);

            post.setPostId(jsonObject.getInt(POST_ID));
            post.setOwnerId(jsonObject.getInt(OWNER_ID));
            post.setDescription(jsonObject.getString(TEXT));

            if (jsonObject.has(ATTACHMENTS)) {
                JSONArray attachmentsArray = jsonObject.getJSONArray(ATTACHMENTS);

                JSONObject objectInsideAttachment = attachmentsArray.getJSONObject(0);

                if (objectInsideAttachment.has(PHOTO)) {
                    JSONObject photoObject = objectInsideAttachment.getJSONObject(PHOTO);
                    post.setNamePicture(photoObject.getString(IMAGE_NAME));
                }

                if (post.getNamePicture() == null) {
                    if (objectInsideAttachment.has(LINK)) {
                        JSONObject linkObject = objectInsideAttachment.getJSONObject(LINK);
                        JSONObject photoObject = linkObject.getJSONObject(PHOTO);
                        post.setNamePicture(photoObject.getString(IMAGE_NAME));
                    }
                }
                if (post.getNamePicture() == null) {
                    if (objectInsideAttachment.has(VIDEO)) {
                        JSONObject videoObject = objectInsideAttachment.getJSONObject(VIDEO);
                        if (videoObject.has(IMAGE_NAME)) {
                            post.setNamePicture(videoObject.getString(IMAGE_NAME));
                        }
                    }
                }
                if (post.getNamePicture() == null) {
                    if (attachmentsArray.length() > 1) {
                        JSONObject secondObjectInsideAttachment = attachmentsArray.getJSONObject(1);
                        if (secondObjectInsideAttachment.has(LINK)) {
                            JSONObject linkObject = secondObjectInsideAttachment.getJSONObject(LINK);
                            JSONObject photoObject = linkObject.getJSONObject(PHOTO);
                            post.setNamePicture(photoObject.getString(IMAGE_NAME));
                        }
                    }

                }
            }


            JSONObject likesObject = jsonObject.getJSONObject(LIKES);
            post.setLikes(likesObject.getInt(LIKES_COUNT));
            post.setPostLiked(likesObject.getInt(USER_LIKES));
        } catch (JSONException je) {
            Log.e(TAG, "json parse problems", je);
        }
        return post;
    }
}
