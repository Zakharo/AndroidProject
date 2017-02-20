package com.example.vladzakharo.androidapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

/**
 * Created by Vlad Zakharo on 17.01.2017.
 */

public class SplashActivity extends AppCompatActivity {

    private PrefManager mPrefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefManager = new PrefManager(this);
        Intent intent;
        if (mPrefManager.isFirstTimeLaunch()) {
            intent = new Intent(this, WelcomeActivity.class);
        } else if (TextUtils.isEmpty(mPrefManager.getToken())) {
            intent = new Intent(this, LoginActivity.class);
        } else{
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
        finish();
    }
}
