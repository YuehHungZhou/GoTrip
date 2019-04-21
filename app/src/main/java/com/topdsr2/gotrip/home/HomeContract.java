package com.topdsr2.gotrip.home;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;
import com.topdsr2.gotrip.data.object.Trip;

import java.util.ArrayList;

public interface HomeContract {

    interface View extends BaseView<Presenter> {

        void showHomeUi(ArrayList<Trip> trips);

    }

    interface Presenter extends BasePresenter {

        void loadHomeData();

        void setHomeData();

        void openTripMap();

        void loadTrip(String tripId);
    }
}
