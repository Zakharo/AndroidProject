package com.example.vladzakharo.androidproject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad Zakharo on 15.12.2016.
 */

public class CarListFragment extends Fragment {
    private RecyclerView mCarRecyclerView;
    private CarAdapter mCarAdapter;
    JSONObject jObject;
    List<Car> mCars;

    public static CarListFragment newInstance(){
        CarListFragment carListFragment = new CarListFragment();
        return carListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCars = new ArrayList<>();

        InputStream inputStream = getResources().openRawResource(R.raw.file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            jObject = new JSONObject(byteArrayOutputStream.toString());

        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            mCars = parseItems(mCars, jObject);
        }catch (IOException io){
            io.printStackTrace();
        }catch (JSONException je){
            je.printStackTrace();
        }

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
        mCarAdapter = new CarAdapter(mCars, getActivity());
        mCarRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mCarRecyclerView.setAdapter(mCarAdapter);
    }

    private List<Car> parseItems(List<Car> items, JSONObject jsonBody)
        throws IOException, JSONException{
        JSONObject categoriesJsonObject = jsonBody.getJSONObject("categories");
        JSONArray dataJsonArray = categoriesJsonObject.getJSONArray("data");

        for (int i = 0; i < dataJsonArray.length(); i++){
            JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);

            Car car = new Car();

            car.setTitle(dataJsonObject.getString("title"));
            car.setDescription(dataJsonObject.getString("description"));
            car.setNamePicture(dataJsonObject.getString("image_name"));

            items.add(car);
        }
        return items;
    }
}

