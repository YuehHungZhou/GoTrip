package com.topdsr2.gotrip.profile.item;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;

public interface ProfileItemContract {

    interface View extends BaseView<Presenter> {

        void showTripUi();

    }

    interface Presenter extends BasePresenter {

        void loadNewTripData();

        void setNewTripData();

        void loadCompleteTripData();

        void setCompleteTripData();

        void loadCollectionTripData();

        void setCollectionTripData();

        void openTrip();

    }
}
