package com.topdsr2.gotrip.home;

import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.util.FireBaseManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class HomePresenter implements HomeContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private final HomeContract.View mHomeView;

    public HomePresenter(
            @NonNull GoTripRepository goTripRepository,
            @NonNull HomeContract.View homeView) {
        mGoTripRepository = checkNotNull(goTripRepository, "GoTripRepository null");
        mHomeView = checkNotNull(homeView, "HomeView null");
        mHomeView.setPresenter(this);
    }

    @Override
    public void loadHomeData() {
        FireBaseManager.getInstance().getAllTypeTrip(new FireBaseManager.GetAllTripCallback() {
            @Override
            public void onCompleted(ArrayList<Trip> trips) {
                mHomeView.showHomeUi(trips);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void setHomeData() {

    }

    @Override
    public void openTripMap() {

    }

    @Override
    public void loadTrip(String tripId) {

    }

    @Override
    public void start() {

    }
}
