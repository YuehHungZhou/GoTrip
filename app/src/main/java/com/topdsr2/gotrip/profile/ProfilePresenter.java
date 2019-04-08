package com.topdsr2.gotrip.profile;

import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.home.HomeContract;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProfilePresenter implements ProfileContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private final ProfileContract.View mProfileView;

    public ProfilePresenter(
            @NonNull GoTripRepository goTripRepository,
            @NonNull ProfileContract.View profileView) {
        mGoTripRepository = checkNotNull(goTripRepository, "GoTripRepository null");
        mProfileView = checkNotNull(profileView, "profileView null");
        mProfileView.setPresenter(this);
    }


    @Override
    public void loadProfileData() {

    }

    @Override
    public void setProfileData() {

    }

    @Override
    public void openTripMap() {

    }

    @Override
    public void start() {

    }
}