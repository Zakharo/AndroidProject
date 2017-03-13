package com.example.vladzakharo.androidapplication.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.vladzakharo.androidapplication.constants.Constants;

/**
 * Created by Vlad Zakharo on 17.01.2017.
 */

public class PrefManager {
    private SharedPreferences defPref;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context mContext;

    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "android-welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String TOKEN = "token";
    private static final String UID = "uid";
    private static final String POST_ID = "post_id";
    private static final String PREF_KEY = "amount_of_cars";

    public PrefManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        defPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void putToken(String token) {
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return pref.getString(TOKEN, "");
    }

    public void putUid(String uid) {
        editor.putString(UID, uid);
        editor.apply();
    }

    public String getUid() {
        return pref.getString(UID, "");
    }

    public void deleteToken() {
        editor.putString(TOKEN, "");
        editor.apply();
    }

    public void deleteUid() {
        editor.putString(UID, "");
        editor.apply();
    }

    public void setPostId(int id) {
        editor.putInt(POST_ID, id);
        editor.apply();
    }

    public int getPostId() {
        return pref.getInt(POST_ID, 0);
    }

    public String getCountOfCars() {
        return defPref.getString(PREF_KEY, Constants.DEFAULT_VALUE);
    }
}
