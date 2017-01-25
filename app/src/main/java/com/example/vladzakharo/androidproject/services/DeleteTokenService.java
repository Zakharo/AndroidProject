package com.example.vladzakharo.androidproject.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.vladzakharo.androidproject.sharedPreferences.PrefManager;

/**
 * Created by Vlad Zakharo on 25.01.2017.
 */

public class DeleteTokenService extends IntentService {

    private static final String TAG = "DeleteTokenService";
    private PrefManager mPrefManager;
    private static final int timeToWait = 1000 * 60 * 60 * 24;

    public DeleteTokenService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mPrefManager = new PrefManager(this);
        mPrefManager.deleteUid();
        mPrefManager.deleteToken();

        Intent i = new Intent(this, DeleteTokenService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeToWait, pendingIntent);
    }
}
