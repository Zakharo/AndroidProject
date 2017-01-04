package com.example.vladzakharo.androidproject;

import android.net.Uri;
import android.os.Parcel;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.text.style.URLSpan;
import android.view.View;

/**
 * Created by Vlad Zakharo on 02.01.2017.
 */

public class NewCustomChromeTab extends URLSpan {

    private CustomTabsIntent.Builder intentBuilder;
    public NewCustomChromeTab(String url) {
        super(url);
    }

    @Override
    public void onClick(View view) {
        String url = getURL();
        intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(view.getResources().getColor(R.color.colorPrimary));
        CustomTabsIntent intent = intentBuilder.build();
        intent.launchUrl(DetailActivity.activity, Uri.parse(url));
    }

}
