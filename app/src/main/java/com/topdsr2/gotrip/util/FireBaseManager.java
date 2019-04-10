package com.topdsr2.gotrip.util;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.topdsr2.gotrip.data.GoTripLocalDataSource;
import com.topdsr2.gotrip.data.GoTripRemoteDataSource;
import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.data.object.TripAndPoint;

import java.util.ArrayList;

public class FireBaseManager {

    private static final String TRIP = "Trip";
    private static final String ID = "id";
    private static final String COUNTRY = "country";
    private static final String CREATER = "creater";
    private static final String DESCRIBE = "describe";
    private static final String MAINIMAGE = "mainimage";
    private static final String ONTRIP = "ontrip";
    private static final String OWNER = "owner";
    private static final String TITLE = "title";
    private static final String TOTALCOST = "totalcost";
    private static final String TRIPSTART = "tripstart";
    private static final String TRIPEND = "tripend";

    private static final String POINT = "Point";
    private static final String ARRIVALTIME = "arrivaltime";
    private static final String COST = "cost";
    private static final String ICONTYPE = "icontype";
    private static final String SORTE = "sorte";
    private static final String DAY = "day";

    private static final String GEOPOINT = "Geopoint";
    private static final String GEO = "geo";
    private static final String IMAGES = "images";

    private static final String USER = "User";
    private static final String EMAIL = "email";
    private static final String FRIENDS = "friends";
    private static final String NAME = "name";
    private static final String TRIPCOLLECTION = "tripcollection";

    private static final String TRUE = "true";



    private final GoTripRepository mGoTripRepository;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static class FireBaseManagerHolder {
        private static final FireBaseManager INSTANCE = new FireBaseManager();
    }

    private FireBaseManager() {
        mGoTripRepository = GoTripRepository.getInstance(
                GoTripRemoteDataSource.getInstance(),
                GoTripLocalDataSource.getInstance());
    }

    public static FireBaseManager getInstance() {
        return FireBaseManagerHolder.INSTANCE;
    }





    public void getSelectedTrip(int id,findTripCallback callback) {
        TripAndPoint bean = new TripAndPoint();

        getTripDocumentId(id, new GetDocumentIdAndTripCallback() {
            @Override
            public void onCompleted(String id,Trip trip) {
                bean.setTrip(trip);

                getTripPoint(id, new GetPointsCallback() {
                    @Override
                    public void onCompleted(ArrayList<Point> points) {
                        bean.setPoints(points);
                        callback.onCompleted(bean);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void getTripDocumentId(int id, GetDocumentIdAndTripCallback callback) {

        db.collection(TRIP)
                .whereEqualTo(ID,id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Trip trip = document.toObject(Trip.class);
                            callback.onCompleted(document.getId(),trip);
                        }
                    }
                });
    }



    private void getTripPoint(String tripId,GetPointsCallback callback) {

        ArrayList<Point> points = new ArrayList<>();

        db.collection(TRIP)
                .document(tripId)
                .collection(POINT)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Point point = document.toObject(Point.class);
                            points.add(point);
                        }
                        callback.onCompleted(points);
                    }
                });
    }


    public interface findTripCallback {

        void onCompleted(TripAndPoint bean);

        void onError(String errorMessage);
    }

    interface GetDocumentIdAndTripCallback {

        void onCompleted(String id,Trip trip);

        void onError(String errorMessage);
    }

    interface GetPointsCallback {

        void onCompleted(ArrayList<Point> points);

        void onError(String errorMessage);
    }





}
