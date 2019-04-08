package com.topdsr2.gotrip.home;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;

public interface HomeContract {

    interface View extends BaseView<Presenter> {

        void showHomeUi();

    }

    interface Presenter extends BasePresenter {

        void loadHomeData();

        void setHomeData();

        void openTripMap();
    }
}
