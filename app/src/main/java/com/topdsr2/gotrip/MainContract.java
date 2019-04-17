package com.topdsr2.gotrip;

import android.app.Activity;

public interface MainContract {
    interface View extends BaseView<Presenter> {

        void openHomeUi();

        void openTripUi();

        void openProfileUi();

        void openLoginUi();

        void hideBottomNavigationUi();

        void openAddOrDeletePointUi();

        void checkListener();

    }

    interface Presenter extends BasePresenter {

        void checkLogInState(Activity activity);

        void openHome();

        void openTrip();

        void openProfile();

        void detachListener();

        void checkGoBack();

        void setOrignalListener();

    }
}
