package com.topdsr2.gotrip.profile.item;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;
import com.topdsr2.gotrip.data.object.Trip;

import java.util.ArrayList;

public interface ProfileItemContract {

    interface View extends BaseView<Presenter> {

        void showTripUi(ArrayList<Trip> trips);

        void showToast();

    }

    interface Presenter extends BasePresenter {

        void loadNewTripData();

        void addNewTrip(Trip trip);

        void setProfileTripData(ArrayList<Trip> trips);

        void loadCompleteTripData();

        void loadCollectionTripData();

        void openTrip();

        void hideBtmNavi();

        void loadTripData(String tripId);

        void openAddTripDialog();

        void deleteTrip(Trip trip, int type);

    }
}
