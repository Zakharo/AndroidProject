package com.example.vladzakharo.androidproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Vlad Zakharo on 08.01.2017.
 */

public class FirstReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, UpdateDataService.class);
        context.startService(intentService);
    }
}
