package com.topdsr2.gotrip;

import android.app.Activity;

import com.topdsr2.gotrip.data.object.Request;
import com.topdsr2.gotrip.data.object.SearchData;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.dialog.AddTripOwnerDialog;
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

        void openRequestUi(Request request);

        void selectHome();

        void openExitUi();

        void back();

        void openAddTripOwnerUi();

        void showToast(String message);

        void openFilterUi();

        void openAddPointRequestUi();

        void openDeletePointRequestUi();


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

        void leaveTrip();

        void loadRequestData();

        void agreeTripRequest(String documentId);

        void disagreeTripRequest(String documentId);

        void agreeFriendRequest(String email);

        void disagreeFriendRequest(String email);

        void setAddTripOwnerDialog(AddTripOwnerDialog addTripOwnerDialog);

        void addTripOwner(String email);

        void checkUserData();

        void search(SearchData searchData);

        void openAddOrDeletePoint();

        void readyDeletePoint();

        void saveCollection();

    }

    public interface GetAllRequestCallback {

        void onCompleted(Request request);

        void onError(String errorMessage);
    }


}
