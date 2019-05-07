package com.topdsr2.gotrip;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.topdsr2.gotrip.addOrDeletePoint.AddOrDeletePointContract;
import com.topdsr2.gotrip.addOrDeletePoint.AddOrDeletePointPresenter;
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
    public void checkLogInState(Activity activity) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = (accessToken != null);

        if (!isLoggedIn) {
            mMainView.openLoginUi();
        } else {
            UserManager.getInstance().readInternal(activity, new UserManager.LoadCallback() {
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
//        if (mTripPresenter == null) {
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
                        mMainView.showToast("沒有正在旅行的行程，請到個人頁面安排新旅程");
                        // 轉到profile
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

//        } else {
//            mTripPresenter.checkTripStatus(new TripContract.GetOnTripStatusCallback() {
//                @Override
//                public void onCompleted(String tripId) {
//                    mMainView.openTripUi();
//                    mMainView.hideBottomNavigationUi();
//                    mTripPresenter.loadTripData(tripId);
//                }
//
//                @Override
//                public void onFailure() {
//                    // 轉到profile
//                }
//
//                @Override
//                public void onError(String errorMessage) {
//
//                }
//            });
//        }

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

    @Override
    public void setAddTripOwnerDialog(AddTripOwnerDialog addTripOwnerDialog) {
        mAddTripOwnerDialog = addTripOwnerDialog;
    }

    @Override
    public void addTripOwner(String email) {
        mTripPresenter.addTripRequest(email, new TripContract.AddTripOwnerOrNotCallback() {
            @Override
            public void onCompleted() {
                mAddTripOwnerDialog.dismiss();
                mMainView.showToast("加入成功");
            }

            @Override
            public void onFailure() {
                mMainView.showToast("沒有此會員");
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void checkUserData() {
        mProfilePresenter.checkUser();
    }

    @Override
    public void search(SearchData searchData) {
        mHomePresenter.searchData(searchData);
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
    public void openFilterView() {
        mMainView.openFilterUi();
    }

    @Override
    public void searchData(SearchData searchData) {

    }

    @Override
    public void getUserTripCollection() {

    }

    @Override
    public void changeCollection(ArrayList<String> tripCollection) {
        mHomePresenter.changeCollection(tripCollection);
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
    public void readyDeletePoint() {
        mTripPresenter.deletePoint();
    }

    @Override
    public void saveCollection() {

        if (UserManager.getInstance().getUser() != null) {
            FireBaseManager.getInstance().setUserCollection(UserManager.getInstance().getUser().getTripCollection(), UserManager.getInstance().getUser().getEmail());
        }
    }


    @Override
    public void showDeleteView(int position) {
        mTripPresenter.showDeleteView(position);
    }

    @Override
    public void deletePoint() {
    }

    @Override
    public void reSetTripListener() {

    }

    @Override
    public void addFriendRequest(String email) {
        mTripPresenter.addFriendRequest(email);
    }

    @Override
    public void addTripRequest(String email, TripContract.AddTripOwnerOrNotCallback callback) {

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
    public void openAddTripOwner() {
        mMainView.openAddTripOwnerUi();
    }

    @Override
    public void openAddPointRequestView() {
        mMainView.openAddPointRequestUi();
    }


    @Override
    public void openDeletePointRequestView() {
        mMainView.openDeletePointRequestUi();
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
    public void checkUser() {

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
