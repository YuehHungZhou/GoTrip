package com.topdsr2.gotrip.home;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;
import com.topdsr2.gotrip.data.object.SearchData;
import com.topdsr2.gotrip.data.object.Trip;

import java.util.ArrayList;

public interface HomeContract {

    interface View extends BaseView<Presenter> {

        void showHomeUi(ArrayList<Trip> trips);

        void showUserTripCollection(ArrayList<String> tripCollection);


    }

    interface Presenter extends BasePresenter {

        void loadHomeData();

        void setHomeData(ArrayList<Trip> trips);

        void openTrip();

        void hideBtmNavi();

        void loadTripData(String tripId);

        void openHomeFilterDialog();

        void searchData(SearchData searchData);

        void loadUserTripCollection();

        void changeCollection(ArrayList<String> tripCollection);

    }
}
