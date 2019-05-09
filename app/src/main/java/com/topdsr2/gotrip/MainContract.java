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

        void openAddTripDialogUi();

        void openLogoutUi();

        void openRequestUi(Request request);

        void openExitUi();

        void openAddTripOwnerUi();

        void openHomeFilterUi();

        void openAddPointRequestUi();

        void openDeletePointRequestUi();

        void openAddOrDeletePointUi();

        ProfileItemFragment findNewTripView();

        ProfileItemFragment findCompleteTripView();

        ProfileItemFragment findCollectionTripView();

        void hideBtmNaviUi();

        void showBtmNaviUi();

        void selectedHomePage();

        void showToast(String message);

        void back();

    }

    interface Presenter extends BasePresenter {

        void checkLogIn(Activity activity);

        void openHome();

        void openTrip();

        void openProfile();

        void openAddOrDeletePoint();

        void detachListener();

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

        void readyDeletePoint();

        void saveCollection();

    }

}
