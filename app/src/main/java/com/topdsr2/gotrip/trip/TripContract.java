package com.topdsr2.gotrip.trip;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.TripAndPoint;

public interface TripContract {

    interface View extends BaseView<Presenter> {

        void showTripUi(TripAndPoint bean);

        void changeIconInfoUi(int posotion);

        void moveCameraToMarker(Double latitude, Double longitude);

        void reLoadData();

        int getToday();

        int getPointNumber();

        Point addPointData(Point point);

        void showPointDeleteView(int position);

        void reSetTripListener();


    }

    interface Presenter extends BasePresenter {

        void loadTripData();

        void setTripData();

        void openTripMap();

        void hideBottomNavigation();

        void addPoint(Point point);

        void changeIconInfo(int position);

        void moveMapToIcon(Double latitude, Double longitude);

        void setTripListener(String documentId);

        void openAddOrDeletePoint();

        void showDeleteView(int position);

        void deletePoint(int position);

        void removeListener();

        void reSetTripListener();


    }
}
