package com.example.vladzakharo.androidapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
        } else if (mPrefManager.getToken() == null) {
            intent = new Intent(this, LoginActivity.class);
        } else{
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
