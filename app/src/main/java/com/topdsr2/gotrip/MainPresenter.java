package com.topdsr2.gotrip;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.topdsr2.gotrip.addOrDeletePoint.AddOrDeletePointContract;
import com.topdsr2.gotrip.addOrDeletePoint.AddOrDeletePointPresenter;
import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.Request;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.data.object.TripAndPoint;
import com.topdsr2.gotrip.home.HomeContract;
import com.topdsr2.gotrip.home.HomePresenter;
import com.topdsr2.gotrip.profile.ProfileContract;
import com.topdsr2.gotrip.profile.ProfilePresenter;
import com.topdsr2.gotrip.profile.item.ProfileItemContract;
import com.topdsr2.gotrip.profile.item.ProfileItemFragment;
import com.topdsr2.gotrip.profile.item.ProfileItemPresenter;
import com.topdsr2.gotrip.trip.TripContract;
import com.topdsr2.gotrip.trip.TripPresenter;
import com.topdsr2.gotrip.util.FireBaseManager;
import com.topdsr2.gotrip.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainPresenter implements MainContract.Presenter, HomeContract.Presenter,
        TripContract.Presenter, ProfileContract.Presenter, ProfileItemContract.Presenter, AddOrDeletePointContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private MainContract.View mMainView;

    private HomePresenter mHomePresenter;
    private TripPresenter mTripPresenter;
    private ProfilePresenter mProfilePresenter;

    private ProfileItemPresenter mProfileNewTripPresenter;
    private ProfileItemPresenter mProfileCompleteTripPresenter;
    private ProfileItemPresenter mProfileCollectionTripPresenter;

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

    void setProfileNewTripPresenter(ProfileItemPresenter profileNewTripPresenter) {
        mProfileNewTripPresenter = checkNotNull(profileNewTripPresenter);
    }

    void setProfileCompleteTripPresenter(ProfileItemPresenter profileCompleteTripPresenter) {
        mProfileCompleteTripPresenter = checkNotNull(profileCompleteTripPresenter);
    }

    void setProfileCollectionTripPresenter(ProfileItemPresenter profileCollectionTripPresenter) {
        mProfileCollectionTripPresenter = checkNotNull(profileCollectionTripPresenter);
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

    @Override
    public void checkOnTrip() {
        if (mTripPresenter == null) {
            FireBaseManager.getInstance().getHasOnTrip(UserManager.getInstance().getUser().getEmail(),
                    new FireBaseManager.GetUserOnTripCallback() {
                        @Override
                        public void onCompleted(String tripId) {
                            mMainView.openTripUi();
                            mMainView.hideBottomNavigationUi();
                            mTripPresenter.loadTripData(tripId);
                        }

                        @Override
                        public void onFailure() {
                            // 轉到profile
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });

        } else {
            mTripPresenter.checkTripStatus(new TripContract.GetOnTripStatusCallback() {
                @Override
                public void onCompleted(String tripId) {
                    mMainView.openTripUi();
                    mMainView.hideBottomNavigationUi();
                    mTripPresenter.loadTripData(tripId);
                }

                @Override
                public void onFailure() {
                    // 轉到profile
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        }

    }

    @Override
    public void addNewTrip(Trip trip) {

        FireBaseManager.getInstance().addNewTrip(trip, new FireBaseManager.AddNewTripCallback() {
            @Override
            public void onCompleted(String tripId) {
                mProfileNewTripPresenter.loadNewTripData();

            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void logout() {
        mMainView.selectHome();
    }

    @Override
    public void leaveTrip() {

        mTripPresenter.leaveThisTrip(new TripContract.LeaveOrNotCallback() {
            @Override
            public void onCompleted() {
                mMainView.back();
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void loadRequestData() {
        FireBaseManager.getInstance().getRequest(UserManager.getInstance().getUser().getEmail(), new FireBaseManager.GetRequestCallback() {
            @Override
            public void onCompleted(Request request) {
                mProfilePresenter.setRequestData(request);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void agreeTripRequest(String documentId) {
        FireBaseManager.getInstance().agreeTripRequest(UserManager.getInstance().getUser().getEmail(), documentId);
    }

    @Override
    public void disagreeTripRequest(String documentId) {
        FireBaseManager.getInstance().removeTripRequest(UserManager.getInstance().getUser().getEmail(), documentId);
    }

    @Override
    public void agreeFriendRequest(String email) {
        FireBaseManager.getInstance().agreeFriendRequest(UserManager.getInstance().getUser().getEmail(), email);

    }

    @Override
    public void disagreeFriendRequest(String email) {
        FireBaseManager.getInstance().removeFriendRequest(UserManager.getInstance().getUser().getEmail(), email);

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
    public void loadTripData(String tripId) {
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
    public void loadTrip(String tripId) {
        mMainView.openTripUi();
        mMainView.hideBottomNavigationUi();
        mTripPresenter.loadTripData(tripId);
    }

    @Override
    public void openAddTripDialog() {
        mMainView.openAddTripDialogView();
    }

    @Override
    public void deleteTrip(Trip trip, int type) {
        mProfileNewTripPresenter.deleteTrip(trip, type);
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
    public void checkTripStatus(TripContract.GetOnTripStatusCallback callback) {

    }

    @Override
    public void getAddPointData(TripContract.GetAddPointDataCallback callback) {

    }

    @Override
    public void showVoteView(int position) {
        mTripPresenter.showVoteView(position);
    }

    @Override
    public void vote(String pointId, String type) {
        mTripPresenter.vote(pointId, type);
    }

    @Override
    public void openExit() {
        mMainView.openExitUi();
    }

    @Override
    public void leaveThisTrip(TripContract.LeaveOrNotCallback callback) {

    }

    @Override
    public void loadPointData() {
        mTripPresenter.getAddPointData(new TripContract.GetAddPointDataCallback() {
            @Override
            public void onCompleted(TripAndPoint bean, int today) {

                mAddOrDeletePointPresenter.setPointData(bean, today);
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void setPointData(TripAndPoint bean, int today) {
    }

    @Override
    public void sendNewPoint(Point point) {
        mTripPresenter.addPoint(point);
    }

    @Override
    public void loadUserData() {
        mProfilePresenter.loadUserData();
    }

    @Override
    public ProfileItemFragment findNewTrip() {
        return mMainView.findNewTripView();
    }

    @Override
    public ProfileItemFragment findCompleteTrip() {
        return mMainView.findCompleteTripView();
    }

    @Override
    public ProfileItemFragment findCollectionTrip() {
        return mMainView.findCollectionTripView();
    }

    @Override
    public void openLogoutView() {
        mMainView.openLogoutUi();
    }

    @Override
    public void openRequestView() {
        mMainView.openRequestUi(mProfilePresenter.getRequestData());
    }

    @Override
    public void setRequestData(Request request) {

    }

    @Override
    public Request getRequestData() {
        return null;
    }

    @Override
    public void loadNewTripData() {
        mProfileNewTripPresenter.loadNewTripData();
    }

    @Override
    public void setNewTripData(ArrayList<Trip> trips) {
        mProfileNewTripPresenter.setNewTripData(trips);
    }

    @Override
    public void loadCompleteTripData() {
        mProfileCompleteTripPresenter.loadCompleteTripData();
    }

    @Override
    public void setCompleteTripData(ArrayList<Trip> trips) {
        mProfileCompleteTripPresenter.setCompleteTripData(trips);
    }

    @Override
    public void loadCollectionTripData() {
        mProfileCollectionTripPresenter.loadCollectionTripData();
    }

    @Override
    public void setCollectionTripData(ArrayList<Trip> trips) {
        mProfileCollectionTripPresenter.setCollectionTripData(trips);
    }


}
