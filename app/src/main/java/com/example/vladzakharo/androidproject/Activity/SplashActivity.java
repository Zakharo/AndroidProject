package com.example.vladzakharo.androidproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.vladzakharo.androidproject.sharedpreferences.PrefManager;

/**
 * Created by Vlad Zakharo on 17.01.2017.
 */

public class SplashActivity extends AppCompatActivity {

    private PrefManager mPrefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefManager = new PrefManager(this);
        if (mPrefManager.isFirstTimeLaunch()) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
