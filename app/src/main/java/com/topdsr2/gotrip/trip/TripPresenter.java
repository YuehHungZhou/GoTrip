package com.topdsr2.gotrip.trip;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.SupportMapFragment;
import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.data.object.TripAndPoint;
import com.topdsr2.gotrip.util.FireBaseManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class TripPresenter implements TripContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private SupportMapFragment mSupportMapFragment;
    private final TripContract.View mTripView;
    private TripAndPoint mBean;

    private Trip mTrip;

    public TripPresenter(
            @NonNull GoTripRepository goTripRepository,
            @NonNull TripContract.View tripView) {
        mGoTripRepository = checkNotNull(goTripRepository, "GoTripRepository null");
        mTripView = checkNotNull(tripView, "tripView null");
        mTripView.setPresenter(this);
    }


    @Override
    public void loadTripData() {

        FireBaseManager.getInstance().getSelectedTrip(1, new FireBaseManager.FindTripCallback() {
            @Override
            public void onCompleted(TripAndPoint bean) {
                mTripView.showTripUi(bean);
                mBean = bean;
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void setTripData() {

    }

    @Override
    public void openTripMap() {

    }

    @Override
    public void hideBottomNavigation() {

    }

    @Override
    public void addPoint(String documentId, Point point, int dayPoints) {
        FireBaseManager.getInstance().updatePointToFireBase(documentId, point, dayPoints);

    }

    @Override
    public void changeIconInfo(int position) {
        mTripView.changeIconInfoUi(position);
    }

    @Override
    public void moveMapToIcon(Double latitude, Double longitude) {
        mTripView.moveCameraToMarker(latitude, longitude);
    }

    @Override
    public void setTripListener(String documentId) {
        FireBaseManager.getInstance().setListener(documentId, new FireBaseManager.EvenHappendCallback() {
            @Override
            public void onCompleted() {
                mTripView.reLoadData();
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void openAddOrDeletePoint() {


    }

    @Override
    public void start() {

    }

}
