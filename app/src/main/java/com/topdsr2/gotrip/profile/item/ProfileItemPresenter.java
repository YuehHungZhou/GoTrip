package com.topdsr2.gotrip.profile.item;

import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.util.FireBaseManager;
import com.topdsr2.gotrip.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.topdsr2.gotrip.profile.item.ProfileItemAdapter.TYPE_COLLECTIONTRIP;
import static com.topdsr2.gotrip.profile.item.ProfileItemAdapter.TYPE_COMPLETETRIP;
import static com.topdsr2.gotrip.profile.item.ProfileItemAdapter.TYPE_NEWTRIP;

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
        FireBaseManager.getInstance().getNewTripData(UserManager.getInstance().getUser().getEmail(),
                new FireBaseManager.GetUserTripCallback() {
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
        FireBaseManager.getInstance().getCompleteTripData(UserManager.getInstance().getUser().getEmail(),
                new FireBaseManager.GetUserTripCallback() {
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
        FireBaseManager.getInstance().getCollectionTripData(UserManager.getInstance().getUser().getTripCollection(),
                new FireBaseManager.GetUserTripCallback() {
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
    public void deleteTrip(Trip trip, int type) {

        switch (type) {
            case TYPE_NEWTRIP:
                if ((checkIsCreater(trip.getCreater()))) {
                    FireBaseManager.getInstance().deleteTrip(trip.getId(),
                            new FireBaseManager.DeleteTripCallback() {
                                @Override
                                public void onCompleted() {
                                    loadNewTripData();
                                }

                                @Override
                                public void onError(String errorMessage) {

                                }
                            });
                } else {
                    mProfileItemView.showToast();
                }
                break;
            case TYPE_COMPLETETRIP:
                if ((checkIsCreater(trip.getCreater()))) {
                    FireBaseManager.getInstance().deleteTrip(trip.getId(),
                            new FireBaseManager.DeleteTripCallback() {
                                @Override
                                public void onCompleted() {
                                    loadCompleteTripData();
                                }

                                @Override
                                public void onError(String errorMessage) {

                                }
                            });
                } else {
                    mProfileItemView.showToast();
                }
                break;
            case TYPE_COLLECTIONTRIP:
                FireBaseManager.getInstance().removeCollectionTrip(trip.getId(),
                        UserManager.getInstance().getUser().getEmail(),
                        new FireBaseManager.RemoveUserCollectionCallback() {
                            @Override
                            public void onCompleted() {
                                loadCollectionTripData();
                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });
                break;
            default:
                break;
        }
    }

    @Override
    public void start() {

    }

    private boolean checkIsCreater(String creater) {
        return UserManager.getInstance().getUser().getEmail().trim().equals(creater.trim());
    }


}
