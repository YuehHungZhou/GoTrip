package com.topdsr2.gotrip.trip;

import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.TripAndPoint;
import com.topdsr2.gotrip.util.Constants;
import com.topdsr2.gotrip.util.FireBaseManager;
import com.topdsr2.gotrip.util.UserManager;

import java.util.ArrayList;

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
    public void loadTripData(String tripId) {
        FireBaseManager.getInstance().getSelectedTrip(tripId, new FireBaseManager.FindTripCallback() {
            @Override
            public void onCompleted(TripAndPoint bean) {
                mBean = bean;
                setTripData(mBean);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void setTripData(TripAndPoint bean) {
        mTripView.showTripUi(bean);
    }

    @Override
    public void addPoint(Point point) {
        FireBaseManager.getInstance().updatePointToFireBase(mBean.getDocumentId(),
                mTripView.addPointData(point), mTripView.getPointNumber(), mTripView.getToday());

    }

    @Override
    public void deletePoint() {
        int position = mTripView.getTouchedIconPosition();
        FireBaseManager.getInstance().deletePoint(mBean.getDocumentId(), position + 1,
                position + 1, mTripView.getPointNumber(), mTripView.getToday());
    }

    @Override
    public void addFriendRequest(String email) {
        FireBaseManager.getInstance().addFriendRequest(email,
                UserManager.getInstance().getUser().getEmail(), new FireBaseManager.AddFriendCallback() {
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
    public void addTripRequest(String addEmail, TripContract.AddTripOwnerOrNotCallback callback) {
        FireBaseManager.getInstance().addTripRequest(addEmail, mBean.getTrip().getId(),
                new FireBaseManager.AddFriendCallback() {
                    @Override
                    public void onCompleted() {
                        callback.onCompleted();

                    }

                    @Override
                    public void OnFailure() {
                        callback.onFailure();
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
    }

    @Override
    public void checkIsOwner() {

        if (isOwner()) {
            mTripView.openFunction(Constants.TRUE);
            setTripListener(mBean.getDocumentId());
        } else {
            mTripView.closeFunction(Constants.FALSE);
        }
    }

    @Override
    public void checkDifferentTrip(TripAndPoint bean) {
        if (mTripView.getOriginTrip() != null) {
            if (!mTripView.getOriginTrip().getTrip().getId().equals(bean.getTrip().getId())) {
                mTripView.resetContent();
            }
        }
    }

    @Override
    public ArrayList<Point> sortPoint(ArrayList<Point> points) {

        ArrayList<Point> pointsDayHolder = new ArrayList<>();

        if (points.size() != 0) {
            int sortNumber = 1;
            do {
                for (int i = 0; i < points.size(); i++) {
                    if (points.get(i).getSorte() == sortNumber) {
                        pointsDayHolder.add(points.get(i));
                        sortNumber++;
                    }
                }
            } while (pointsDayHolder.size() != points.size());
        }
        return pointsDayHolder;
    }

    @Override
    public void getAddPointData(TripContract.GetAddPointDataCallback callback) {
        callback.onCompleted(mBean, mTripView.getToday());
    }

    @Override
    public void showVoteView(int position) {
        mTripView.showVoteViewUi(position);
    }

    @Override
    public void showPointDeleteView(int position) {
        mTripView.showPointDeleteView(position);
    }

    @Override
    public void vote(String pointId, String type) {
        FireBaseManager.getInstance().votePoint(
                mBean.getTrip().getId(),
                pointId,
                type,
                mBean.getTrip().getOwners().size(),
                UserManager.getInstance().getUser().getEmail());
    }

    @Override
    public void openExitDialog() {

    }

    @Override
    public void leaveThisTrip(TripContract.LeaveOrNotCallback callback) {
        if (isCreater()) {
            callback.onFailure();
            mTripView.showToast();
        } else {
            FireBaseManager.getInstance().removeOwner(UserManager.getInstance()
                    .getUser().getEmail(), mBean.getTrip().getId());
            callback.onCompleted();
        }
    }

    @Override
    public void openAddTripOwnerDialog() {

    }

    @Override
    public void openAddPointRequestDialog() {

    }

    @Override
    public void openDeletePointRequestDialog() {

    }

    @Override
    public void detachTripListener() {
        FireBaseManager.getInstance().closeListener();
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
        FireBaseManager.getInstance().setListener(documentId, mBean.getTrip().getAddPointTimes(),
                new FireBaseManager.EvenHappendCallback() {
                    @Override
                    public void onCompleted(String tripId) {
                        loadTripData(tripId);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
    }

    @Override
    public void start() {

    }
    
    private boolean isOwner() {
        return mBean.getTrip().getOwners().contains(UserManager.getInstance().getUser().getEmail());
    }

    private boolean isCreater() {
        return mBean.getTrip().getCreater().equals(UserManager.getInstance().getUser().getEmail());
    }

}
