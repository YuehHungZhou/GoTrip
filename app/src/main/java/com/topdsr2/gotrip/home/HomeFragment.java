package com.topdsr2.gotrip.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Trip;

import java.util.ArrayList;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class HomeFragment extends Fragment implements HomeContract.View {

    private HomeContract.Presenter mPresenter;
    private HomeAdapter mHomeAdapter;
    private ArrayList<Trip> mTrips;
    private FloatingActionButton mFloatingActionButton;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomeAdapter = new HomeAdapter(mPresenter);
        mPresenter.loadHomeData();

    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView homeRecyclerView = root.findViewById(R.id.recycleview_home);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeRecyclerView.setAdapter(mHomeAdapter);

        mFloatingActionButton = root.findViewById(R.id.floatingActionButton);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openHomeFilterDialog();
            }
        });
    }

    @Override
    public void showHomeUi(ArrayList<Trip> trips) {
        mTrips = trips;
        mHomeAdapter.updateData(mTrips);
    }

    @Override
    public void loadUserTripCollection(ArrayList<String> tripCollection) {
        mHomeAdapter.loadTripCollection(tripCollection);
    }


}
