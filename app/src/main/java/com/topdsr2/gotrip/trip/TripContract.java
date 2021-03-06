package com.topdsr2.gotrip.trip;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.data.object.TripAndPoint;

import java.util.ArrayList;

public interface TripContract {

    interface View extends BaseView<Presenter> {

        void showTripUi(TripAndPoint bean);

        void changeIconInfoUi(int posotion);

        void moveCameraToMarker(Double latitude, Double longitude);

        int getToday();

        int getPointNumber();

        int getTouchedIconPosition();

        Point addPointData(Point point);

        void showPointDeleteView(int position);

        void closeFunction(boolean isOwner);

        void openFunction(boolean isOwner);

        void showVoteViewUi(int position);

        void showToast();

        TripAndPoint getOriginTrip();

        void resetContent();

    }

    interface Presenter extends BasePresenter {

        void loadTripData(String tripId);

        void setTripData(TripAndPoint bean);

        void addPoint(Point point);

        void changeIconInfo(int position);

        void moveMapToIcon(Double latitude, Double longitude);

        void setTripListener(String documentId);

        void showPointDeleteView(int position);

        void deletePoint();

        void addFriendRequest(String email);

        void addTripRequest(String email, AddTripOwnerOrNotCallback callback);

        void checkIsOwner();

        void getAddPointData(GetAddPointDataCallback callback);

        void showVoteView(int position);

        void vote(String pointId, String type);

        void openExitDialog();

        void leaveThisTrip(LeaveOrNotCallback callback);

        void openAddTripOwnerDialog();

        void openAddPointRequestDialog();

        void openDeletePointRequestDialog();

        void detachTripListener();

        void checkDifferentTrip(TripAndPoint bean);

        ArrayList<Point> sortPoint(ArrayList<Point> points);

    }

    interface GetAddPointDataCallback {

        void onCompleted(TripAndPoint bean,  int day);

        void onFailure();

        void onError(String errorMessage);

    }

    interface LeaveOrNotCallback {

        void onCompleted();

        void onFailure();

        void onError(String errorMessage);
    }

    interface AddTripOwnerOrNotCallback {

        void onCompleted();

        void onFailure();

        void onError(String errorMessage);
    }


}
