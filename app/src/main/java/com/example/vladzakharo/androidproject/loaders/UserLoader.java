package com.example.vladzakharo.androidproject.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.example.vladzakharo.androidproject.ApiServices;
import com.example.vladzakharo.androidproject.items.User;

/**
 * Created by Vlad Zakharo on 24.01.2017.
 */

public class UserLoader extends AsyncTaskLoader {
    public UserLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public User loadInBackground() {
        return new ApiServices(getContext()).getUser();
    }
}
