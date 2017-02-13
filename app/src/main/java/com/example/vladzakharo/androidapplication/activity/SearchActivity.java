package com.example.vladzakharo.androidapplication.activity;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.adapters.SearchAdapter;
import com.example.vladzakharo.androidapplication.decoration.Decorator;
import com.example.vladzakharo.androidapplication.items.Post;
import com.example.vladzakharo.androidapplication.services.ApiServices;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchAdapter;

    private static final int SEARCH_DELAY = 300;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mTextListener;

    private List<Post> mPosts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        mSearchView = (SearchView) findViewById(R.id.searchview_search);
        //mSearchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));
        mSearchView.setOnQueryTextListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void updateUi() {
        mSearchAdapter = new SearchAdapter(getApplicationContext(), mPosts);
        Decorator decoration = new Decorator(getApplicationContext(), getResources().getColor(R.color.colorPrimary), 0.5f);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mSearchAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        mHandler.removeCallbacks(mTextListener);
        mTextListener = new Runnable() {
            @Override
            public void run() {
                String myText = newText.replace("#", "%23");
                String text = myText.replace(" ", "%20");
                mPosts = new ApiServices(getApplicationContext()).searchNews(text);
                updateUi();
            }
        };
        mHandler.postDelayed(mTextListener, SEARCH_DELAY);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
