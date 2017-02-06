package com.example.vladzakharo.androidapplication.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.vladzakharo.androidapplication.R;
import com.example.vladzakharo.androidapplication.adapters.CarAdapter;
import com.example.vladzakharo.androidapplication.database.CarsProvider;
import com.example.vladzakharo.androidapplication.decoration.Decorator;
import com.example.vladzakharo.androidapplication.services.UpdateDataService;

public class FragmentLikes extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID = 5;
    private static final String SORT = "sort_by_likes";

    private RecyclerView mCarRecyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CarAdapter mCarAdapter;

    private Cursor mCursor;

    public FragmentLikes() {

    }

    private void setCursor( Cursor cursor ) {
        mCursor = cursor;
    }

    public static FragmentLikes newInstance(Cursor cursor) {
        FragmentLikes fragmentLikes = new FragmentLikes();
        fragmentLikes.setCursor(cursor);
        return fragmentLikes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_two, container, false);

        mCarRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_two_recycler_view);
        mCarRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProgressBar = (ProgressBar) v.findViewById(R.id.fragment_two_progress_bar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.fragment_two_swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intentService = new Intent(getActivity(), UpdateDataService.class);
                getActivity().startService(intentService);
            }
        });
        updateUi();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void updateUi() {
        mCarAdapter = new CarAdapter(getActivity(), mCursor);
        Decorator decoration = new Decorator(getActivity(), getResources().getColor(R.color.colorPrimary), 0.5f);
        mCarRecyclerView.addItemDecoration(decoration);
        mCarRecyclerView.setAdapter(mCarAdapter);
        mCarRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_ID) {
            return null;
        }
        return new CursorLoader(getActivity(), CarsProvider.CAR_CONTENT_URI, null, null, null, SORT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        updateUi();
        mProgressBar.setVisibility(View.GONE);
        mCarRecyclerView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
