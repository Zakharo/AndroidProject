package com.example.vladzakharo.androidapplication.activity;

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
import com.example.vladzakharo.androidapplication.utils.VKUtil;
import com.example.vladzakharo.androidapplication.services.FirstDeleteService;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

public class WebActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private PrefManager mPrefManager;

    private static final String TAG = "WebActivity";
    private static final String URL = "http://oauth.vk.com/authorize?" +
                "client_id=5829154" +
                "&scope=wall"+
                "&redirect_uri=http://oauth.vk.com/blank.html" +
                "&response_type=token";
    private static final String OAUTH_ONE = "http://oauth.vk.com/authorize";
    private static final String OAUTH_TWO = "http://oauth.vk.com/oauth/authorize";
    private static final String REDIRECT_URI = "http://oauth.vk.com/blank.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mWebView = (WebView) findViewById(R.id.web);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPrefManager = new PrefManager(this);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.clearCache(true);
        mWebView.setWebViewClient(new VkWebViewClient());

        mWebView.loadUrl(URL);
        mWebView.setVisibility(View.VISIBLE);

    }

    class VkWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
            parseUrl(url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if( url.startsWith(OAUTH_ONE) || url.startsWith(OAUTH_TWO) ) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    private void parseUrl(String url) {
        try {
            if( url == null ) {
                return;
            }
            if( url.startsWith(REDIRECT_URI) ) {
                if(!url.contains("error")) {
                    String[] auth = VKUtil.parseRedirectUrl(url);
                    mWebView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);

                    mPrefManager.putToken(auth[0]);
                    mPrefManager.putUid(auth[1]);

                    Intent service = new Intent(this, FirstDeleteService.class);
                    startService(service);

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    finish();
                }
            }
        } catch(Exception e) {
            Log.d(TAG, "parse url problem");
        }
    }
}
