package com.example.vladzakharo.androidapplication.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


/**
 * Created by Vlad Zakharo on 25.01.2017.
 */

public class FirstDeleteService extends IntentService {

    private static final String TAG = "FirstDeleteService";
    private static final int timeToWait = 1000 * 60 * 60 * 24;

    public FirstDeleteService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent i = new Intent(this, DeleteTokenService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeToWait, pendingIntent);
    }
}
