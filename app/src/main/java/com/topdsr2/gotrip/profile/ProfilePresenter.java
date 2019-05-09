package com.topdsr2.gotrip.profile;

import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Request;
import com.topdsr2.gotrip.data.object.User;
import com.topdsr2.gotrip.home.HomeContract;
import com.topdsr2.gotrip.profile.item.ProfileItemFragment;
import com.topdsr2.gotrip.util.FireBaseManager;
import com.topdsr2.gotrip.util.UserManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProfilePresenter implements ProfileContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private final ProfileContract.View mProfileView;
    private User mUser;
    private Request mRequest;

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
    public void loadUserData() {
        mUser = UserManager.getInstance().getUser();
        mProfileView.showProfileUi(mUser);
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

    @Override
    public void openLogoutDialog() {

    }

    @Override
    public void openRequestDialog() {

    }

    @Override
    public void setRequestData(Request request) {
        mRequest = request;
        mProfileView.showRequestUi(request);

    }

    @Override
    public Request getRequestData() {
        return mRequest;
    }

    @Override
    public void checkUser() {
        mProfileView.checkUser();
    }
}
