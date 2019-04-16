package com.topdsr2.gotrip;

public interface MainContract {
    interface View extends BaseView<Presenter> {

        void openHomeUi();

        void openTripUi();

        void openProfileUi();

        void openLoginUi();

        void hideBottomNavigationUi();

        void openAddOrDeletePointUi();

    }

    interface Presenter extends BasePresenter {

        void checkLogInState();

        void openHome();

        void openTrip();

        void openProfile();

        void detachListener();

    }
}
