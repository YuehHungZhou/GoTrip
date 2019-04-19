package com.topdsr2.gotrip.trip;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.data.object.TripAndPoint;
import com.topdsr2.gotrip.util.Constants;
import com.topdsr2.gotrip.util.FireBaseManager;
import com.topdsr2.gotrip.util.UserManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class TripPresenter implements TripContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private final TripContract.View mTripView;
    private TripAndPoint mBean;
    
    public TripPresenter(
            @NonNull GoTripRepository goTripRepository,
            @NonNull TripContract.View tripView) {
        mGoTripRepository = checkNotNull(goTripRepository, "GoTripRepository null");
        mTripView = checkNotNull(tripView, "tripView null");
        mTripView.setPresenter(this);
    }


    @Override
    public void loadTripData(int tripId) {
        if (mBean == null) {
            FireBaseManager.getInstance().getSelectedTrip(tripId, new FireBaseManager.FindTripCallback() {
                @Override
                public void onCompleted(TripAndPoint bean) {
                    mBean = bean;
                    mTripView.showTripUi(mBean);
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        } else if (mBean.getTrip().getId() != tripId) {
            FireBaseManager.getInstance().getSelectedTrip(tripId, new FireBaseManager.FindTripCallback() {
                @Override
                public void onCompleted(TripAndPoint bean) {
                    mBean = bean;
                    mTripView.showTripUi(mBean);
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        } else {
            mTripView.showTripUi(mBean);
        }

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
    public void addPoint(Point point) {

        FireBaseManager.getInstance().updatePointToFireBase(mBean.getDocumentId(),
                mTripView.addPointData(point), mTripView.getPointNumber(), mTripView.getToday());

    }

    @Override
    public void deletePoint(int position) {
        FireBaseManager.getInstance().deletePoint(mBean.getDocumentId(), position + 1,
                position + 1, mTripView.getPointNumber(), mTripView.getToday());
    }

    @Override
    public void removeListener() {
        FireBaseManager.getInstance().closeListener();
    }

    @Override
    public void reSetTripListener() {
        mTripView.reSetTripListener();
    }

    @Override
    public void addFriendRequest(String email) {
        FireBaseManager.getInstance().addFriendRequest(email, UserManager.getInstance().getUser().getEmail(),
                new FireBaseManager.AddFriendCallback() {
                    @Override
                    public void onCompleted() {
                        //show 加入成功
                    }

                    @Override
                    public void OnFailure() {
                        //show 無此使用者
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
    }

    @Override
    public void addTripRequest(String email) {
        FireBaseManager.getInstance().addTripRequest(email, UserManager.getInstance().getUser().getEmail(),
                new FireBaseManager.AddFriendCallback() {
                    @Override
                    public void onCompleted() {
                        //show 加入成功

                    }

                    @Override
                    public void OnFailure() {
                        //show 無此使用者
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
    }

    @Override
    public void checkIsOwner() {

        if (isOwner()) {
            mTripView.openFunction(isOwner());
        } else {
            mTripView.closeFunction(isOwner());
        }
    }

    @Override
    public void checkTripStatus(TripContract.GetOnTripStatusCallback callback) {
        if (isOwner() && mBean.getTrip().isOnTrip()) {
            callback.onCompleted(mBean.getTrip().getId());
        } else {
            callback.onFailure();
        }

        
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
        FireBaseManager.getInstance().setListener(documentId, mBean.getTrip().getAddPointTimes(), new FireBaseManager.EvenHappendCallback() {
            @Override
            public void onCompleted(int tripId) {
                loadTripData(tripId);
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
    public void showDeleteView(int position) {
        mTripView.showPointDeleteView(position);
    }


    @Override
    public void start() {

    }
    
    private boolean isOwner() {
        return mBean.getTrip().getOwners().contains(UserManager.getInstance().getUser().getEmail());
    }

}
