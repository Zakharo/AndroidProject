package com.example.vladzakharo.androidapplication.activity;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.adapters.SearchAdapter;
import com.example.vladzakharo.androidapplication.database.SearchSuggestionsProvider;
import com.example.vladzakharo.androidapplication.decoration.Decorator;
import com.example.vladzakharo.androidapplication.items.Post;
import com.example.vladzakharo.androidapplication.services.ApiServices;
import com.example.vladzakharo.androidapplication.services.Callback;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnSuggestionListener,
        Callback{

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchAdapter;
    private String textToColor = null;
    private Callback mCallback;
    private ProgressBar mProgressBar;

    private static final int SEARCH_DELAY = 300;
    private static final String TAG = "SearchActivity";

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mTextListener;
    private List<Post> mPosts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(mToolbar);

        mCallback = this;

        mProgressBar = (ProgressBar) findViewById(R.id.search_progress_bar);

        mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        mSearchView = (SearchView) findViewById(R.id.searchview_search);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSuggestionListener(this);
        mSearchView.onActionViewExpanded();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void updateUi() {
        mSearchAdapter = new SearchAdapter(this, mPosts, mRecyclerView, textToColor);
        Decorator decoration = new Decorator(getApplicationContext(), getResources().getColor(R.color.colorPrimary), 0.5f);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mSearchAdapter);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        mHandler.removeCallbacks(mTextListener);
        mTextListener = new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected()) {
                    String myText = newText.replace("#", "%23");
                    String text = myText.replace(" ", "%20");
                    textToColor = text;
                    ApiServices.getInstance(getApplicationContext()).searchNews(textToColor, mCallback);
                } else {
                    Toast.makeText(getApplicationContext(), "Check Internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mHandler.postDelayed(mTextListener, SEARCH_DELAY);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            SearchRecentSuggestions suggestions =
                    new SearchRecentSuggestions(this, SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
            suggestions.saveRecentQuery(textToColor, null);

            finish();
        }
        return true;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        String query = getSuggestedItem(position);
        mSearchView.setQuery(query, false);
        return true;
    }

    private String getSuggestedItem(int position) {
        Cursor cursor = (Cursor) mSearchView.getSuggestionsAdapter().getItem(position);
        return cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
    }


    @Override
    public void onSuccess(ArrayList T) {
        mPosts = T;
        updateUi();
    }

    @Override
    public void onError(Throwable tr) {
        Log.e(TAG, "error searching", tr);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
