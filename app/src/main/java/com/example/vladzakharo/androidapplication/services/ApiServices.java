package com.example.vladzakharo.androidapplication.services;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.vladzakharo.androidapplication.activity.MainActivity;
import com.example.vladzakharo.androidapplication.converters.JsonParser;
import com.example.vladzakharo.androidapplication.database.DataBaseConstants;
import com.example.vladzakharo.androidapplication.database.UserProvider;
import com.example.vladzakharo.androidapplication.http.HttpGetJson;
import com.example.vladzakharo.androidapplication.items.Post;
import com.example.vladzakharo.androidapplication.items.User;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;
import com.example.vladzakharo.androidapplication.utils.VKUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Vlad Zakharo on 24.01.2017.
 */

public class ApiServices {

    private static final String TAG = "ApiService";

    private PrefManager mPrefManager;
    private SharedPreferences mySharedPreferences;
    private String mCountOfCars;
    private String mUserInfoUrl;
    private String mSearchCarsUrl;
    private String mSearchUrl;
    private String mAddLikeUrl;
    private String mDeleteLikeUrl;
    private Context mContext;
    private String mSearchJsonObject;
    private static final String REDIRECT_URI = "http://oauth.vk.com/blank.html";
    private static final String PREF_KEY = "amount_of_cars";

    public ApiServices(Context context) {
        mContext = context;
        mPrefManager = new PrefManager(context);
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mCountOfCars = mySharedPreferences.getString(PREF_KEY, "10");
        mUserInfoUrl = "https://api.vk.com/method/users.get?user_ids="
                + mPrefManager.getUid()
                + "&fields=photo,photo_200_orig,bdate,home_town&v=5.62"
                + "&access_token="
                + mPrefManager.getToken();
        mSearchCarsUrl = "https://api.vk.com/method/newsfeed.search?q=%23car&extended=0&count="
                + mCountOfCars
                + "&v=5.62"
                + "&access_token="
                + mPrefManager.getToken();
    }

    public User getUser() {
        String stringJsonObject = HttpGetJson.GET(mUserInfoUrl);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringJsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "json problems", je);
        }

        User user = new JsonParser(mPrefManager).convert(User.class, jsonObject);

        ArrayList<ContentProviderOperation> delete = new ArrayList<>();
        delete.add(ContentProviderOperation.newDelete(UserProvider.USER_CONTENT_URI)
                .withSelection(DataBaseConstants.USER_ID + " = ?", new String[]{String.valueOf(1)})
                .build());

        try {
            mContext.getContentResolver().applyBatch(UserProvider.AUTHORITY, delete);
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
            mContext.getContentResolver().applyBatch(UserProvider.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException re) {
            Log.e(TAG, "UpdateDataService", re);
        }
        Log.d(TAG, "user loaded");
        return user;
    }

    public String getCars() {
        return HttpGetJson.GET(mSearchCarsUrl);
    }

    public List<Post> searchNews(String what) {
        mSearchUrl = "https://api.vk.com/method/newsfeed.search?q=%23car%20"
                + what
                +"&extended=0&count="
                + mCountOfCars
                + "&v=5.62"
                + "&access_token="
                + mPrefManager.getToken();

        List<Post> posts = null;
        try {
            mSearchJsonObject = new SearchNewsTask(this).execute(mSearchUrl).get();
            Log.d(TAG, "items found");
            JSONObject jsonObject;
            jsonObject = new JSONObject(mSearchJsonObject);
            posts = new JsonParser(mPrefManager).convertToList(jsonObject);
        } catch (ExecutionException | InterruptedException | JSONException ex) {
            Log.e(TAG, "search problems", ex);
        }

        return posts;
    }
    public void addLike(int ownerId, int postId) {
        mAddLikeUrl = "https://api.vk.com/method/likes.add?type=post&owner_id="
                + ownerId
                + "&item_id="
                + postId
                + "&v=5.62"
                + "&access_token="
                + mPrefManager.getToken();

        new Like().execute(mAddLikeUrl);
        Log.d(TAG, "like added");
    }

    public void deleteLike(int ownerId, int postId) {
        mDeleteLikeUrl = "https://api.vk.com/method/likes.delete?type=post&owner_id="
                + ownerId
                + "&item_id="
                + postId
                + "&v=5.62"
                + "&access_token="
                + mPrefManager.getToken();

        new Like().execute(mDeleteLikeUrl);
        Log.d(TAG, "like deleted");
    }

    public void parseResponse(String url) {
        try {
            if( url == null ) {
                return;
            }
            if( url.startsWith(REDIRECT_URI) ) {
                if(!url.contains("error")) {
                    String[] auth = VKUtil.parseRedirectUrl(url);

                    mPrefManager.putToken(auth[0]);
                    mPrefManager.putUid(auth[1]);

                    Intent service = new Intent(mContext, FirstDeleteService.class);
                    mContext.startService(service);

                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                }
            }
        } catch(Exception e) {
            Log.d(TAG, "parse url problem");
        }
        Log.d(TAG, "token and user id saved");
    }

    private static class SearchNewsTask extends AsyncTask<String, Void, String> {

        private ApiServices mApiServices;

        public SearchNewsTask(ApiServices apiServices) {
            mApiServices = apiServices;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            return HttpGetJson.GET(url);
        }

        @Override
        protected void onPostExecute(String s) {
            mApiServices.mSearchJsonObject = s;
        }
    }

    private static class Like extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];
            HttpGetJson.GET(url);
            return null;
        }
    }
}
