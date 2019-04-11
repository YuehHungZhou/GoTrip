package com.topdsr2.gotrip.trip;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.SupportMapFragment;
import com.topdsr2.gotrip.data.GoTripRepository;
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

        FireBaseManager.getInstance().getSelectedTrip(1,new FireBaseManager.findTripCallback() {

            @Override
            public void onCompleted(TripAndPoint bean) {

                Log.v("kerry", bean.getPoints().get(2).getDay()+ "");

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
    public void addPoint(String documentId) {
        FireBaseManager.getInstance().addPointToFireBase(documentId);

    }

    @Override
    public void changeIconInfo(int position) {
        mTripView.changeIconInfoUi(position);
    }

    @Override
    public void start() {

    }

}
