package com.topdsr2.gotrip;

public interface MainContract {
    interface View extends BaseView<Presenter> {

        void openHomeUi();

        void openTripUi();

        void openProfileUi();

    }

    interface Presenter extends BasePresenter {

        void openHome();

        void openTrip();

        void openProfile();

    }
}
