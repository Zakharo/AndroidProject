package com.example.vladzakharo.androidproject;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {
    private FragmentManager fm;
    private Car mCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getIntent() != null){
            mCar = (Car)getIntent().getSerializableExtra(CarAdapter.CAR_SERIALIZABLE);
        }

        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.detail_fragment_container, DetailFragment.newInstance(mCar))
                .commit();
    }
}
