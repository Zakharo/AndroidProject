package com.example.vladzakharo.androidapplication.database;

import android.content.SearchRecentSuggestionsProvider;


/**
 * Created by Vlad Zakharo on 13.02.2017.
 */

public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.vladzakharo.provider.searchSuggestions";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
