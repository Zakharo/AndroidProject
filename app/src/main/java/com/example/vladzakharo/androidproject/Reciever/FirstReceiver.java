package com.example.vladzakharo.androidproject.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.vladzakharo.androidproject.services.UpdateDataService;

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
