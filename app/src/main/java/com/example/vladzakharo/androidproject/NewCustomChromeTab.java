package com.example.vladzakharo.androidproject;

import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.text.style.URLSpan;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Vlad Zakharo on 02.01.2017.
 */

public class NewCustomChromeTab extends URLSpan {

    private CustomTabsIntent.Builder intentBuilder;
    private WeakReference<Activity> mActivity;

    public NewCustomChromeTab(String url, WeakReference<Activity> activity) {
        super(url);
        mActivity = activity;
    }

    @Override
    public void onClick(View view) {
        String url = getURL();
        intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(view.getResources().getColor(R.color.colorPrimary));
        CustomTabsIntent intent = intentBuilder.build();
        Activity activity = mActivity.get();
        intent.launchUrl(activity, Uri.parse(url));
    }
}
