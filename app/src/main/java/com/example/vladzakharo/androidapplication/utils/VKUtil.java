package com.example.vladzakharo.androidapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.vladzakharo.androidapplication.activity.MainActivity;
import com.example.vladzakharo.androidapplication.services.FirstDeleteService;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vlad Zakharo on 19.01.2017.
 */

public class VKUtil  {

    private static final String REDIRECT_URI = "http://oauth.vk.com/blank.html";
    private static final String TAG = "VKUtil";
    private PrefManager mPrefManager;
    private Context mContext;

    public VKUtil(Context context) {
        mContext = context;
        mPrefManager = new PrefManager(context);
    }

    public static String[] parseRedirectUrl(String url) throws Exception {
        String access_token = extractPattern(url, "access_token=(.*?)&");
        String user_id = extractPattern(url, "user_id=(\\d*)");
        if( user_id == null || user_id.length() == 0 || access_token == null || access_token.length() == 0 ) {
            throw new Exception("Failed to parse redirect url " + url);
        }
        return new String[]{access_token, user_id};
    }

    public static String extractPattern(String string, String pattern){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(string);
        if (!m.find())
            return null;
        return m.toMatchResult().group(1);
    }

    public void parseResponse(String url) {
        try {
            if( url == null ) {
                return;
            }
            if(url.startsWith(REDIRECT_URI) ) {
                if(!url.contains("error")) {
                    String[] auth = VKUtil.parseRedirectUrl(url);

                    mPrefManager.putToken(auth[0]);
                    mPrefManager.putUid(auth[1]);

                    Intent service = new Intent(mContext, FirstDeleteService.class);
                    mContext.startService(service);

                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);
                }
            }
        } catch(Exception e) {
            Log.d(TAG, "parse url problem");
        }
        Log.d(TAG, "token and user id saved");
    }
}
