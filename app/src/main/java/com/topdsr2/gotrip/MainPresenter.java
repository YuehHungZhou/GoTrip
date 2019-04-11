package com.topdsr2.gotrip;


import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.home.HomeContract;
import com.topdsr2.gotrip.home.HomePresenter;
import com.topdsr2.gotrip.profile.ProfileContract;
import com.topdsr2.gotrip.profile.ProfilePresenter;
import com.topdsr2.gotrip.trip.TripContract;
import com.topdsr2.gotrip.trip.TripPresenter;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainPresenter implements MainContract.Presenter, HomeContract.Presenter,
        TripContract.Presenter, ProfileContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private MainContract.View mMainView;

    private HomePresenter mHomePresenter;
    private TripPresenter mTripPresenter;
    private ProfilePresenter mProfilePresenter;


    public MainPresenter(
            @NonNull GoTripRepository goTripRepository,
            @NonNull MainContract.View mainView) {
        mGoTripRepository = checkNotNull(goTripRepository, "goTripRepository null");
        mMainView = checkNotNull(mainView, "mainView null");
        mMainView.setPresenter(this);
    }

    void setHomePresenter(HomePresenter homePresenter) {
        mHomePresenter = checkNotNull(homePresenter);
    }

    void setTripPresenter(TripPresenter tripPresenter) {
        mTripPresenter = checkNotNull(tripPresenter);
    }

    void setProfilePresenter(ProfilePresenter profilePresenter) {
        mProfilePresenter = checkNotNull(profilePresenter);
    }


    @Override
    public void start() {

    }

    @Override
    public void openHome() {
        mMainView.openHomeUi();
    }

    @Override
    public void openTrip() {
        mMainView.openTripUi();
    }

    @Override
    public void openProfile() {
        mMainView.openProfileUi();
    }

    /**
     * Home
     */

    @Override
    public void loadHomeData() {

    }

    @Override
    public void setHomeData() {

    }

    @Override
    public void loadProfileData() {

    }

    @Override
    public void setProfileData() {

    }

    @Override
    public void loadTripData() {
        mTripPresenter.loadTripData();
    }

    @Override
    public void setTripData() {
        mTripPresenter.setTripData();
    }

    @Override
    public void openTripMap() {

    }

    @Override
    public void hideBottomNavigation() {
        mMainView.hideBottomNavigationUi();
    }

    @Override
    public void addPoint(String documentId) {
        mTripPresenter.addPoint(documentId);
    }


    /**
     *
     */
}
