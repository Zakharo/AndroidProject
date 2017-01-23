package com.example.vladzakharo.androidproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.vladzakharo.androidproject.R;
import com.example.vladzakharo.androidproject.Utils.VKUtil;
import com.example.vladzakharo.androidproject.constants.Constants;
import com.example.vladzakharo.androidproject.sharedPreferences.PrefManager;

import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private PrefManager mPrefManager;
    private static final String TAG = "LoginActivity";

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

        String url = "http://oauth.vk.com/authorize?" +
                "client_id="+Constants.VK_API_ID +
                "&scope="+Constants.SCOPE+
                "&redirect_uri="+ URLEncoder.encode(Constants.REDIRECT_URI)+
                "&response_type=token";
        mWebView.loadUrl(url);
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
            if( url.startsWith("http://oauth.vk.com/authorize") || url.startsWith("http://oauth.vk.com/oauth/authorize") ) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    private void parseUrl(String url) {
        try {
            if( url == null ) {
                return;
            }
            if( url.startsWith(Constants.REDIRECT_URI) ) {
                if(!url.contains("error")) {
                    String[] auth = VKUtil.parseRedirectUrl(url);
                    mWebView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);

                    mPrefManager.putToken(auth[0]);
                    mPrefManager.putUid(auth[1]);

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
