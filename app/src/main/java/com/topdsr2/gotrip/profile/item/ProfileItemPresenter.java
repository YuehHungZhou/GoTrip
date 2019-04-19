package com.topdsr2.gotrip.profile.item;

import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProfileItemPresenter implements ProfileItemContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private final ProfileItemContract.View mProfileItemView;

    public ProfileItemPresenter(
            @NonNull GoTripRepository goTripRepository,
            @NonNull ProfileItemContract.View profileItemView) {
        mGoTripRepository = checkNotNull(goTripRepository, "GoTripRepository null");
        mProfileItemView = checkNotNull(profileItemView, "profileView null");

        mProfileItemView.setPresenter(this);
    }


    @Override
    public void loadNewTripData() {

    }

    @Override
    public void setNewTripData() {

    }

    @Override
    public void loadCompleteTripData() {

    }

    @Override
    public void setCompleteTripData() {

    }

    @Override
    public void loadCollectionTripData() {

    }

    @Override
    public void setCollectionTripData() {

    }

    @Override
    public void openTrip() {

    }

    @Override
    public void start() {

    }
}
