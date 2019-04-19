package com.topdsr2.gotrip.profile;

import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.home.HomeContract;
import com.topdsr2.gotrip.profile.item.ProfileItemFragment;

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
    public void start() {

    }

    @Override
    public ProfileItemFragment findNewTrip() {
        return null;
    }

    @Override
    public ProfileItemFragment findCompleteTrip() {
        return null;
    }

    @Override
    public ProfileItemFragment findCollectionTrip() {
        return null;
    }
}
