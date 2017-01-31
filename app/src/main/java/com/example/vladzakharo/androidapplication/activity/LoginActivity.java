package com.example.vladzakharo.androidapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.sharedpreferences.PrefManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton;
    private PrefManager mPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mButton = (Button) findViewById(R.id.btn_login);
        mButton.setOnClickListener(this);

        mPrefManager = new PrefManager(this);
    }

    @Override
    public void onClick(View v) {
        String token = mPrefManager.getToken();
        Intent intent;
        if (token.isEmpty()) {
            intent = new Intent(this, WebActivity.class);

        }else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
