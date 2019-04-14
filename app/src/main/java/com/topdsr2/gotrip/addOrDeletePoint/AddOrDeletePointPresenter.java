package com.topdsr2.gotrip.addOrDeletePoint;

import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;

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
        mAddOrDeletePointView.showAddOrDeleteUi();

    }

    @Override
    public void setPointData() {

    }

    @Override
    public void start() {

    }
}
