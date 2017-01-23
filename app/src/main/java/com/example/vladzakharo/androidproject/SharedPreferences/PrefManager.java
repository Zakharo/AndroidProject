package com.example.vladzakharo.androidproject.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vlad Zakharo on 17.01.2017.
 */

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context mContext;

    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "android-welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String TOKEN = "token";
    private static final String UID = "uid";

    public PrefManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void putToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(TOKEN, "");
    }

    public void putUid(String uid) {
        editor.putString(UID, uid);
        editor.commit();
    }

    public String getUid() {
        return pref.getString(UID, "");
    }

    public void deleteToken() {
        editor.putString(TOKEN, "");
        editor.commit();
    }

    public void deleteUid() {
        editor.putString(UID, "");
        editor.commit();
    }
}
