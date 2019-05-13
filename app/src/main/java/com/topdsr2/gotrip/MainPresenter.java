package com.topdsr2.gotrip;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.topdsr2.gotrip.addordeletepoint.AddOrDeletePointContract;
import com.topdsr2.gotrip.addordeletepoint.AddOrDeletePointPresenter;
import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.Request;
import com.topdsr2.gotrip.data.object.SearchData;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.data.object.TripAndPoint;
import com.topdsr2.gotrip.dialog.AddTripOwnerDialog;
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
        TripContract.Presenter, ProfileContract.Presenter, ProfileItemContract.Presenter,
        AddOrDeletePointContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private MainContract.View mMainView;

    private HomePresenter mHomePresenter;
    private TripPresenter mTripPresenter;
    private ProfilePresenter mProfilePresenter;

    private ProfileItemPresenter mProfileNewTripPresenter;
    private ProfileItemPresenter mProfileCompleteTripPresenter;
    private ProfileItemPresenter mProfileCollectionTripPresenter;

    private AddOrDeletePointPresenter mAddOrDeletePointPresenter;
    private AddTripOwnerDialog mAddTripOwnerDialog;

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
    public void openHomeFilterDialog() {
        mMainView.openHomeFilterUi();
    }

    @Override
    public void openAddTripDialog() {
        mMainView.openAddTripDialogUi();
    }

    @Override
    public void openAddOrDeletePoint() {
        mMainView.openAddOrDeletePointUi();
    }

    @Override
    public void openExitDialog() {
        mMainView.openExitUi();
    }

    @Override
    public void openLogoutDialog() {
        mMainView.openLogoutUi();
    }

    @Override
    public void openRequestDialog() {
        mMainView.openRequestUi();
    }

    @Override
    public void openAddTripOwnerDialog() {
        mMainView.openAddTripOwnerUi();
    }

    @Override
    public void openAddPointRequestDialog() {
        mMainView.openAddPointRequestUi();
    }

    @Override
    public void openDeletePointRequestDialog() {
        mMainView.openDeletePointRequestUi();
    }

    @Override
    public void openSuccessDialog() {
        mMainView.openSuccessUi();
    }

    @Override
    public void showPointDeleteView(int position) {
        mTripPresenter.showPointDeleteView(position);
    }

    @Override
    public void showVoteView(int position) {
        mTripPresenter.showVoteView(position);
    }

    @Override
    public void setAddTripOwnerDialog(AddTripOwnerDialog addTripOwnerDialog) {
        mAddTripOwnerDialog = addTripOwnerDialog;
    }

    @Override
    public void hideBtmNavi() {
        mMainView.hideBtmNaviUi();
    }

    @Override
    public void checkLogIn(Activity activity) {

        if (UserManager.getInstance().getLoginState()) {
            UserManager.getInstance().readInternalStg(activity, new UserManager.LoadCallback() {
                @Override
                public void onSuccess() {
                    mMainView.openHomeUi();
                }

                @Override
                public void onFail(String errorMessage) {

                }

                @Override
                public void onInvalidToken(String errorMessage) {

                }
            });
        } else {
            mMainView.openLoginUi();
        }
    }

    @Override
    public void checkIsOwner() {
        mTripPresenter.checkIsOwner();
    }

    @Override
    public void pressBack() {
        mMainView.pressBack();
    }

    @Override
    public void logout() {
        UserManager.getInstance().userLogout();
        mMainView.selectedHomePage();
    }

    @Override
    public void checkUserData() {
        mProfilePresenter.checkUserData();
    }

    @Override
    public void loadUserData() {
        mProfilePresenter.loadUserData();
    }


    @Override
    public void checkOnTrip() {

        FireBaseManager.getInstance().getHasOnTrip(UserManager.getInstance()
                .getUser().getEmail(), new FireBaseManager.GetUserOnTripCallback() {
                    @Override
                    public void onCompleted(String tripId) {
                        mMainView.openTripUi();
                        mMainView.hideBtmNaviUi();
                        mTripPresenter.loadTripData(tripId);
                    }

                    @Override
                    public void onFailure() {
                        mMainView.showToast(GoTrip.getContext().getResources()
                                .getString(R.string.no_ontrip));
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
    }

    @Override
    public void addNewTrip(Trip trip) {
        mProfileNewTripPresenter.addNewTrip(trip);

    }

    @Override
    public void deleteTrip(Trip trip, int type) {
        mProfileNewTripPresenter.deleteTrip(trip, type);
    }

    @Override
    public void leaveThisTrip(TripContract.LeaveOrNotCallback callback) {

        mTripPresenter.leaveThisTrip(callback);
    }

    @Override
    public void addTripOwner(String email) {
        mTripPresenter.addTripRequest(email, new TripContract.AddTripOwnerOrNotCallback() {
            @Override
            public void onCompleted() {
                mAddTripOwnerDialog.dismiss();
                mMainView.showToast(GoTrip.getContext()
                        .getResources().getString(R.string.add_success));
            }

            @Override
            public void onFailure() {
                mMainView.showToast(GoTrip.getContext()
                        .getResources().getString(R.string.add_owner_fail));
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void addPoint(Point point) {
        mTripPresenter.addPoint(point);
    }

    @Override
    public void sendNewPoint(Point point) {
        mTripPresenter.addPoint(point);
    }

    @Override
    public void deletePoint() {
        mTripPresenter.deletePoint();
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
    public void vote(String pointId, String type) {
        mTripPresenter.vote(pointId, type);
    }

    @Override
    public void setTripListener(String documentId) {
        mTripPresenter.setTripListener(documentId);
    }

    @Override
    public void detachTripListener() {
        mTripPresenter.detachTripListener();
    }

    @Override
    public Request getRequestData() {
        return mProfilePresenter.getRequestData();
    }

    @Override
    public void loadRequestData() {
       mProfilePresenter.loadRequestData();
    }

    @Override
    public void addTripRequest(String email, TripContract.AddTripOwnerOrNotCallback callback) {

    }

    @Override
    public void addFriendRequest(String email) {
        mTripPresenter.addFriendRequest(email);
    }


    @Override
    public void agreeTripRequest(String documentId) {
        FireBaseManager.getInstance().agreeTripRequest(UserManager.getInstance()
                .getUser().getEmail(), documentId);
    }

    @Override
    public void disagreeTripRequest(String documentId) {
        FireBaseManager.getInstance().removeTripRequest(UserManager.getInstance()
                .getUser().getEmail(), documentId);
    }

    @Override
    public void agreeFriendRequest(String email) {
        FireBaseManager.getInstance().agreeFriendRequest(UserManager.getInstance()
                .getUser().getEmail(), email);
    }

    @Override
    public void disagreeFriendRequest(String email) {
        FireBaseManager.getInstance().removeFriendRequest(UserManager.getInstance()
                .getUser().getEmail(), email);
    }

    @Override
    public void search(SearchData searchData) {
        mHomePresenter.searchData(searchData);
    }

    @Override
    public void searchData(SearchData searchData) {
    }

    @Override
    public void loadHomeData() {
        mHomePresenter.loadHomeData();
    }

    @Override
    public void setHomeData(ArrayList<Trip> trips) {
        mHomePresenter.setHomeData(trips);
    }

    @Override
    public void loadTripData(String tripId) {
        mTripPresenter.loadTripData(tripId);
    }

    @Override
    public void setTripData(TripAndPoint bean) {
        mTripPresenter.setTripData(bean);
    }

    @Override
    public void loadNewTripData() {
        mProfileNewTripPresenter.loadNewTripData();
    }

    @Override
    public void loadCompleteTripData() {
        mProfileCompleteTripPresenter.loadCompleteTripData();
    }

    @Override
    public void loadCollectionTripData() {
        mProfileCollectionTripPresenter.loadCollectionTripData();
    }

    @Override
    public void setProfileTripData(ArrayList<Trip> trips) {
        mProfileNewTripPresenter.setProfileTripData(trips);
    }

    @Override
    public void getAddPointData(TripContract.GetAddPointDataCallback callback) {

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
    public void loadUserTripCollection() {
        mHomePresenter.loadUserTripCollection();
    }

    @Override
    public void saveCollection() {

        if (UserManager.getInstance().getUser() != null) {
            FireBaseManager.getInstance().setUserCollection(UserManager.getInstance()
                    .getUser().getTripCollection(), UserManager.getInstance().getUser().getEmail());
        }
    }

    @Override
    public void changeCollection(ArrayList<String> tripCollection) {
        mHomePresenter.changeCollection(tripCollection);
    }

}
