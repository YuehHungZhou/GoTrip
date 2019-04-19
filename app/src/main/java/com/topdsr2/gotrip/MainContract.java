package com.topdsr2.gotrip;

import android.app.Activity;

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

    }


}
