package com.example.vladzakharo.androidapplication.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.vladzakharo.androidapplication.activity.MainActivity;
import com.example.vladzakharo.androidapplication.converters.JsonParser;
import com.example.vladzakharo.androidapplication.http.HttpGetJson;
import com.example.vladzakharo.androidapplication.items.Car;
import com.example.vladzakharo.androidapplication.items.User;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;
import com.example.vladzakharo.androidapplication.utils.VKUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String mAddLikeUrl;
    private Context mContext;
    private static final String REDIRECT_URI = "http://oauth.vk.com/blank.html";
    private static final String PREF_KEY = "amount_of_cars";

    public ApiServices(Context context) {
        mContext = context;
        mPrefManager = new PrefManager(context);
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mCountOfCars = mySharedPreferences.getString(PREF_KEY, "10");
        mUserInfoUrl = "https://api.vk.com/method/users.get?user_ids="
                + mPrefManager.getUid()
                + "&fields=photo,photo_200_orig,bdate,home_town&v=5.62";
        mSearchCarsUrl = "https://api.vk.com/method/newsfeed.search?q=%23car&extended=0&count="
                + mCountOfCars
                + "&v=5.62";

    }

    public User getUser() {
        String stringJsonObject = HttpGetJson.GET(mUserInfoUrl);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringJsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "json problems", je);
        }
        return new JsonParser(mPrefManager).convert(User.class, jsonObject);
    }

    public String getCars() {
        String stringJsonObject = HttpGetJson.GET(mSearchCarsUrl);
        return stringJsonObject;
    }

    public String addLike(Car car) {
        mAddLikeUrl = "https://api.vk.com/method/likes.add?type=post&owner_id="
                + car.getOwnerId()
                + "&item_id="
                + car.getPostId()
                + "&v=5.62";

        String response = HttpGetJson.GET(mAddLikeUrl);
        return response;
    }

    public void parseResponse(String url, WebView webView, ProgressBar progressBar) {
        try {
            if( url == null ) {
                return;
            }
            if( url.startsWith(REDIRECT_URI) ) {
                if(!url.contains("error")) {
                    String[] auth = VKUtil.parseRedirectUrl(url);
                    webView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

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
    }
}
