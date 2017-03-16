package com.example.vladzakharo.androidapplication.services;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.vladzakharo.androidapplication.activity.MainActivity;
import com.example.vladzakharo.androidapplication.constants.Constants;
import com.example.vladzakharo.androidapplication.converters.JsonParser;
import com.example.vladzakharo.androidapplication.database.DBUtils;
import com.example.vladzakharo.androidapplication.database.DataBaseConstants;
import com.example.vladzakharo.androidapplication.database.UserProvider;
import com.example.vladzakharo.androidapplication.http.HttpGetJson;
import com.example.vladzakharo.androidapplication.items.Post;
import com.example.vladzakharo.androidapplication.items.User;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;
import com.example.vladzakharo.androidapplication.utils.VKUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Vlad Zakharo on 24.01.2017.
 */

public class ApiServices {

    private static final String TAG = "ApiService";

    private static PrefManager mPrefManager;
    private String mUserInfoUrl;
    private String mSearchUrl;
    private String mAddLikeUrl;
    private String mDeleteLikeUrl;
    private Context mContext;

    private static ApiServices mApiServices;

    public static ApiServices getInstance(Context context) {
        if (mApiServices == null) {
            mApiServices = new ApiServices(context);
        }
        return mApiServices;
    }

    private ApiServices(Context context) {
        mContext = context;
        mPrefManager = new PrefManager(context);
        mUserInfoUrl = "https://api.vk.com/method/users.get?user_ids="
                + mPrefManager.getUid()
                + "&fields="
                + Constants.USER_INFO_FIELDS
                + "&v="
                + Constants.API_VERSION
                + "&access_token="
                + mPrefManager.getToken();
    }

    private ApiServices() {

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
        Log.d(TAG, "user loaded");
        return user;
    }

    public void getCars(Callback callback) {
        String countOfCars = mPrefManager.getCountOfCars();
        String mSearchCarsUrl = "https://api.vk.com/method/newsfeed.search?q=%23car&extended=0&count="
                + countOfCars
                + "&v="
                + Constants.API_VERSION
                + "&access_token="
                + mPrefManager.getToken();
        new ExecuteTask(callback).execute(mSearchCarsUrl);
        Log.d(TAG, "cars loaded");

    }

    public void searchNews(String what, Callback callback) {
        String countOfCars = mPrefManager.getCountOfCars();
        mSearchUrl = "https://api.vk.com/method/newsfeed.search?q=%23car%20"
                + what
                +"&extended=0&count="
                + countOfCars
                + "&v=5.62"
                + "&access_token="
                + mPrefManager.getToken();

        new ExecuteTask(callback).execute(mSearchUrl);
        Log.d(TAG, "news founded");
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

    private static class ExecuteTask extends AsyncTask<String, Void, ArrayList<Post>> {

        private WeakReference<Callback> mCallback;

        public ExecuteTask(Callback callback) {
            this.mCallback = new WeakReference<>(callback);
        }

        @Override
        protected ArrayList<Post> doInBackground(String... params) {
            String url = params[0];
            String response = HttpGetJson.GET(url);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException je) {
                mCallback.get().onError(je);
                Log.d(TAG, "json problems", je);
            }
            return new JsonParser(mPrefManager).convertToList(jsonObject);
        }

        @Override
        protected void onPostExecute(ArrayList<Post> list) {
            mCallback.get().onSuccess(list);
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
