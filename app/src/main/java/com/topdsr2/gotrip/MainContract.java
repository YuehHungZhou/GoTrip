package com.topdsr2.gotrip;

import android.app.Activity;

import com.topdsr2.gotrip.data.object.Request;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.profile.item.ProfileItemFragment;

public interface MainContract {
    interface View extends BaseView<Presenter> {

        void openHomeUi();

        void openTripUi();

        void openProfileUi();

        void openLoginUi();

        void hideBottomNavigationUi();

        void openAddOrDeletePointUi();

        void checkListener();

        void notSignin();

        ProfileItemFragment findNewTripView();

        ProfileItemFragment findCompleteTripView();

        ProfileItemFragment findCollectionTripView();

        void openAddTripDialogView();

        void openLogoutUi();

        void openRequestUi();

        void selectHome();


    }

    interface Presenter extends BasePresenter {

        void checkLogInState(Activity activity);

        void openHome();

        void openTrip();

        void openProfile();

        void detachListener();

        void checkGoBack();

        void setOrignalListener();

        void notSignin();

        void checkOnTrip();

        void addNewTrip(Trip trip);

        void logout();

        void loadRequestData(GetAllRequestCallback callback);

        void agreeTripRequest(String documentId);

        void disagreeTripRequest(String documentId);

        void agreeFriendRequest(String email);

        void disagreeFriendRequest(String email);
    }

    public interface GetAllRequestCallback {

        void onCompleted(Request request);

        void onError(String errorMessage);
    }


}
