package com.topdsr2.gotrip.profile.item;

import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.util.FireBaseManager;
import com.topdsr2.gotrip.util.UserManager;

import java.util.ArrayList;

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
        FireBaseManager.getInstance().getNewTripData(UserManager.getInstance().getUser().getEmail(), new FireBaseManager.GetUserTripCallback() {
            @Override
            public void onCompleted(ArrayList<Trip> trips) {
                setNewTripData(trips);
            }

            @Override
            public void onFailure(ArrayList<Trip> trips) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }

    @Override
    public void setNewTripData(ArrayList<Trip> trips) {
        mProfileItemView.showTripUi(trips);
    }

    @Override
    public void loadCompleteTripData() {
        FireBaseManager.getInstance().getCompleteTripData(UserManager.getInstance().getUser().getEmail(), new FireBaseManager.GetUserTripCallback() {
            @Override
            public void onCompleted(ArrayList<Trip> trips) {
                setCompleteTripData(trips);

            }

            @Override
            public void onFailure(ArrayList<Trip> trips) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void setCompleteTripData(ArrayList<Trip> trips) {
        mProfileItemView.showTripUi(trips);
    }

    @Override
    public void loadCollectionTripData() {
        FireBaseManager.getInstance().getCollectionTripData(UserManager.getInstance().getUser().getEmail(), new FireBaseManager.GetUserTripCallback() {
            @Override
            public void onCompleted(ArrayList<Trip> trips) {
                setCollectionTripData(trips);

            }

            @Override
            public void onFailure(ArrayList<Trip> trips) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void setCollectionTripData(ArrayList<Trip> trips) {
        mProfileItemView.showTripUi(trips);
    }

    @Override
    public void loadTrip(String tripId) {

    }

    @Override
    public void openAddTripDialog() {

    }

    @Override
    public void start() {

    }
}
