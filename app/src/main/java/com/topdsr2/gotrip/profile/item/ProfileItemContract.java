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

        void setNewTripData(ArrayList<Trip> trips);

        void loadCompleteTripData();

        void setCompleteTripData(ArrayList<Trip> trips);

        void loadCollectionTripData();

        void setCollectionTripData(ArrayList<Trip> trips);

        void loadTrip(String tripId);

        void openAddTripDialog();

        void deleteTrip(Trip trip, int type);

    }
}
