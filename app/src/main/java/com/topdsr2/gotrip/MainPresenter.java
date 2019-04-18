package com.topdsr2.gotrip;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.topdsr2.gotrip.addOrDeletePoint.AddOrDeletePointContract;
import com.topdsr2.gotrip.addOrDeletePoint.AddOrDeletePointPresenter;
import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.data.object.User;
import com.topdsr2.gotrip.home.HomeContract;
import com.topdsr2.gotrip.home.HomePresenter;
import com.topdsr2.gotrip.profile.ProfileContract;
import com.topdsr2.gotrip.profile.ProfilePresenter;
import com.topdsr2.gotrip.trip.TripContract;
import com.topdsr2.gotrip.trip.TripPresenter;
import com.topdsr2.gotrip.util.FireBaseManager;
import com.topdsr2.gotrip.util.UserManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainPresenter implements MainContract.Presenter, HomeContract.Presenter,
        TripContract.Presenter, ProfileContract.Presenter, AddOrDeletePointContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private MainContract.View mMainView;

    private HomePresenter mHomePresenter;
    private TripPresenter mTripPresenter;
    private ProfilePresenter mProfilePresenter;
    private AddOrDeletePointPresenter mAddOrDeletePointPresenter;


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

    void setAddOrDeletePointPresenter(AddOrDeletePointPresenter addOrDeletePointPresenter) {
        mAddOrDeletePointPresenter = checkNotNull(addOrDeletePointPresenter);
    }


    @Override
    public void start() {

    }

    @Override
    public void checkLogInState(Activity activity) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = (accessToken != null);

        if (!isLoggedIn) {
            mMainView.openLoginUi();
        } else {
            UserManager.getInstance().readInternal(activity);
        }

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

    @Override
    public void detachListener() {
        FireBaseManager.getInstance().closeListener();
    }

    @Override
    public void checkGoBack() {
        mMainView.checkListener();
    }

    @Override
    public void setOrignalListener() {
        mTripPresenter.reSetTripListener();
    }

    @Override
    public void notSignin() {
        mMainView.notSignin();
    }


    /**
     * Home
     */

    @Override
    public void loadHomeData() {
        mHomePresenter.loadHomeData();
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
    public void loadTripData(int tripId) {
        mTripPresenter.loadTripData(tripId);
    }

    @Override
    public void setTripData() {
        mTripPresenter.setTripData();
    }

    @Override
    public void openTripMap() {

    }

    @Override
    public void loadTrip(int tripId) {
        mMainView.openTripUi();
        mMainView.hideBottomNavigationUi();
        mTripPresenter.loadTripData(tripId);
    }

    @Override
    public void hideBottomNavigation() {
        mMainView.hideBottomNavigationUi();
    }

    @Override
    public void addPoint(Point point) {
        mTripPresenter.addPoint(point);
    }

    @Override
    public void changeIconInfo(int position) {
        mTripPresenter.changeIconInfo(position);
    }

    @Override
    public void moveMapToIcon(Double latitude, Double longitude) {
        mTripPresenter.moveMapToIcon(latitude, longitude);
    }

    @Override
    public void setTripListener(String documentId) {
        mTripPresenter.setTripListener(documentId);
    }

    @Override
    public void openAddOrDeletePoint() {
        mMainView.openAddOrDeletePointUi();
    }

    @Override
    public void showDeleteView(int position) {
        mTripPresenter.showDeleteView(position);
    }

    @Override
    public void deletePoint(int position) {
        mTripPresenter.deletePoint(position);
    }

    @Override
    public void removeListener() {
        mTripPresenter.removeListener();
    }

    @Override
    public void reSetTripListener() {

    }

    @Override
    public void addFriendRequest(String email) {
        mTripPresenter.addFriendRequest(email);
    }

    @Override
    public void addTripRequest(String email) {
        mTripPresenter.addTripRequest(email);
    }

    @Override
    public void checkIsOwner() {
        mTripPresenter.checkIsOwner();
    }

    @Override
    public void loadPointData() {
        mAddOrDeletePointPresenter.loadPointData();
    }

    @Override
    public void setPointData() {
        mAddOrDeletePointPresenter.setPointData();
    }

    @Override
    public void sendNewPoint(Point point) {
        mTripPresenter.addPoint(point);
    }


    /**
     *
     */
}
