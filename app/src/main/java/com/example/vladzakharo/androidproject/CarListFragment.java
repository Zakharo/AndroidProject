package com.example.vladzakharo.androidproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by Vlad Zakharo on 15.12.2016.
 */

public class CarListFragment extends Fragment {
    private RecyclerView mCarRecyclerView;
    private CarAdapter mCarAdapter;
    private List<Car> mCars;

    public static CarListFragment newInstance(){
        CarListFragment carListFragment = new CarListFragment();
        return carListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCars = ItemsCatcher.parseItems(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_list, container, false);
        mCarRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mCarRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUi();
        return view;
    }

    private void updateUi(){
        mCarAdapter = new CarAdapter(mCars, getActivity().getAssets());
        mCarRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mCarRecyclerView.setAdapter(mCarAdapter);
    }
}

