package com.example.vladzakharo.androidapplication.database;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.example.vladzakharo.androidapplication.items.Post;
import com.example.vladzakharo.androidapplication.items.User;

import java.util.ArrayList;

/**
 * Created by Vlad Zakharo on 20.02.2017.
 */

public class DBManager {

    private static final String TAG = "DBManager";

    public static void addToFavorite(Post post, Context context) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        operations.add(ContentProviderOperation.newInsert(FavoritesProvider.FAVORITE_CAR_CONTENT_URI)
                .withValue(DataBaseConstants.FAVORITES_POST_ID, post.getId())
                .withValue(DataBaseConstants.FAVORITES_POST_LIKES, post.getLikes()+1)
                .withValue(DataBaseConstants.FAVORITES_POST_IS_LIKED, 1)
                .withValue(DataBaseConstants.FAVORITES_POST_POST_ID, post.getPostId())
                .withValue(DataBaseConstants.FAVORITES_POST_OWNER_ID, post.getOwnerId())
                .withValue(DataBaseConstants.FAVORITES_POST_DESCRIPTION, post.getDescription())
                .withValue(DataBaseConstants.FAVORITES_POST_IMAGE_URL, post.getNamePicture())
                .build());

        ArrayList<ContentProviderOperation> update = new ArrayList<>();
        update.add(ContentProviderOperation.newUpdate(CarsProvider.CAR_CONTENT_URI)
                .withSelection(DataBaseConstants.CARS_POST_IMAGE_URL + " = ?", new String[]{String.valueOf(post.getNamePicture())})
                .withValue(DataBaseConstants.CARS_POST_IS_LIKED, 1)
                .build());

        try {
            context.getContentResolver().applyBatch(FavoritesProvider.AUTHORITY, operations);
            context.getContentResolver().applyBatch(CarsProvider.AUTHORITY, update);
        } catch (RemoteException | OperationApplicationException re) {
            Log.e(TAG, "applyBatch()", re);
        }
    }

    public static void deleteFromFavorite(Post post, Context context) {
        ArrayList<ContentProviderOperation> deleteFromFavorites = new ArrayList<>();
        deleteFromFavorites.add(ContentProviderOperation.newDelete(FavoritesProvider.FAVORITE_CAR_CONTENT_URI)
                .withSelection(DataBaseConstants.FAVORITES_POST_IMAGE_URL + " = ?", new String[]{String.valueOf(post.getNamePicture())})
                .build());

        ArrayList<ContentProviderOperation> update = new ArrayList<>();
        update.add(ContentProviderOperation.newUpdate(CarsProvider.CAR_CONTENT_URI)
                .withSelection(DataBaseConstants.CARS_POST_IMAGE_URL + " = ?", new String[]{String.valueOf(post.getNamePicture())})
                .withValue(DataBaseConstants.CARS_POST_IS_LIKED, 0)
                .build());
        try {
            context.getContentResolver().applyBatch(FavoritesProvider.AUTHORITY, deleteFromFavorites);
            context.getContentResolver().applyBatch(CarsProvider.AUTHORITY, update);
        } catch (RemoteException | OperationApplicationException re) {
            Log.e(TAG, "applyBatch()", re);
        }
    }

    public static void clearTableCars(Context context) {
        ArrayList<ContentProviderOperation> deleteOperations = new ArrayList<>();
        deleteOperations.add(ContentProviderOperation.newDelete(CarsProvider.CAR_CONTENT_URI).build());
        try {
            context.getContentResolver().applyBatch(CarsProvider.AUTHORITY, deleteOperations);
        } catch (RemoteException | OperationApplicationException re) {
            Log.e(TAG, "UpdateDataService", re);
        }
    }

    public static void loadTableCars(ArrayList list, Context context) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Post post = (Post)list.get(i);
            operations.add(ContentProviderOperation.newInsert(CarsProvider.CAR_CONTENT_URI)
                    .withValue(DataBaseConstants.CARS_POST_ID, post.getId())
                    .withValue(DataBaseConstants.CARS_POST_LIKES, post.getLikes())
                    .withValue(DataBaseConstants.CARS_POST_IS_LIKED, post.getIsPostLiked())
                    .withValue(DataBaseConstants.CARS_POST_POST_ID, post.getPostId())
                    .withValue(DataBaseConstants.CARS_POST_OWNER_ID, post.getOwnerId())
                    .withValue(DataBaseConstants.CARS_POST_DESCRIPTION, post.getDescription())
                    .withValue(DataBaseConstants.CARS_POST_IMAGE_URL, post.getNamePicture())
                    .build());
        }

        try {
            context.getContentResolver().applyBatch(CarsProvider.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException re) {
            Log.e(TAG, "UpdateDataService", re);
        }
    }

    public static void saveUser(User user, Context context) {
        ArrayList<ContentProviderOperation> delete = new ArrayList<>();
        delete.add(ContentProviderOperation.newDelete(UserProvider.USER_CONTENT_URI)
                .withSelection(DataBaseConstants.USER_ID + " = ?", new String[]{String.valueOf(1)})
                .build());

        try {
            context.getContentResolver().applyBatch(UserProvider.AUTHORITY, delete);
        } catch (RemoteException | OperationApplicationException re) {
            Log.e(TAG, "UpdateDataService", re);
        }

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        operations.add(ContentProviderOperation.newInsert(UserProvider.USER_CONTENT_URI)
                .withValue(DataBaseConstants.USER_ID, user.getId())
                .withValue(DataBaseConstants.USER_FIRST_NAME, user.getFirstName())
                .withValue(DataBaseConstants.USER_LAST_NAME, user.getLastName())
                .withValue(DataBaseConstants.USER_PICTURE, user.getPicture())
                .withValue(DataBaseConstants.USER_FULL_PHOTO, user.getFullPhoto())
                .withValue(DataBaseConstants.USER_DATE_OF_BIRTH, user.getDateOfBirth())
                .withValue(DataBaseConstants.USER_HOMETOWN, user.getHomeTown())
                .build());
        try {
            context.getContentResolver().applyBatch(UserProvider.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException re) {
            Log.e(TAG, "UpdateDataService", re);
        }
    }
}
