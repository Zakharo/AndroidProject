package com.example.vladzakharo.androidapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.constants.Constants;
import com.example.vladzakharo.androidapplication.services.ApiServices;
import com.example.vladzakharo.androidapplication.utils.VKUtil;
import com.example.vladzakharo.androidapplication.services.FirstDeleteService;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

public class WebActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private Context mContext;
    private PrefManager mPrefManager;

    private static final String TAG = "WebActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mWebView = (WebView) findViewById(R.id.web);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mContext = this;

        mPrefManager = new PrefManager(this);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.clearCache(true);
        mWebView.setWebViewClient(new VkWebViewClient());

        mWebView.loadUrl(Constants.AUTHORIZE_URL);
        mWebView.setVisibility(View.VISIBLE);
    }

    class VkWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);

            Log.d(TAG, "PageLoadStarted");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(url.startsWith(Constants.REDIRECT_URI) ) {
                mProgressBar.setVisibility(View.GONE);
                VKUtil.parseResponse(url, mPrefManager, mContext);
                Log.d(TAG, "PageLoadFinished");
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
