package com.topdsr2.gotrip.trip;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;
import com.topdsr2.gotrip.data.object.TripAndPoint;

public interface TripContract {

    interface View extends BaseView<Presenter> {

        void showTripUi(TripAndPoint bean);

        void changeIconInfoUi(int posotion);

        void moveCameraToMarker(Double latitude, Double longitude);

    }

    interface Presenter extends BasePresenter {

        void loadTripData();

        void setTripData();

        void openTripMap();

        void hideBottomNavigation();

        void addPoint(String documentId);

        void changeIconInfo(int position);

        void moveMapToIcon(Double latitude, Double longitude);

    }
}
