package com.topdsr2.gotrip.addordeletepoint;

import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.TripAndPoint;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddOrDeletePointPresenter implements AddOrDeletePointContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private final AddOrDeletePointContract.View mAddOrDeletePointView;

    public AddOrDeletePointPresenter(
            @NonNull GoTripRepository goTripRepository,
            @NonNull AddOrDeletePointContract.View pointView) {
        mGoTripRepository = checkNotNull(goTripRepository, "GoTripRepository null");
        mAddOrDeletePointView = checkNotNull(pointView, "tripView null");
        mAddOrDeletePointView.setPresenter(this);
    }


    @Override
    public void loadPointData() {

    }

    @Override
    public void setPointData(TripAndPoint bean, int today) {
        mAddOrDeletePointView.showAddOrDeleteUi(bean, today);

    }

    @Override
    public void sendNewPoint(Point point) {

    }

    @Override
    public void start() {

    }
}
