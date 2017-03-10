package com.example.vladzakharo.androidapplication.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.vladzakharo.androidapplication.services.UpdateDataService;

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
