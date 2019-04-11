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
import java.util.HashMap;
import java.util.Map;

public class FireBaseManager {

    private static final String TRIP = "Trip";
    private static final String ID = "id";
    private static final String COUNTRY = "country";
    private static final String CREATER = "creater";
    private static final String DESCRIBE = "describe";
    private static final String MAINIMAGE = "mainImage";
    private static final String ONTRIP = "onTrip";
    private static final String OWNER = "owners";
    private static final String TITLE = "title";
    private static final String TRIPSTART = "tripStart";
    private static final String TRIPEND = "tripEnd";

    private static final String POINT = "Point";
    private static final String ARRIVALTIME = "arrivalTime";
    private static final String COST = "cost";
    private static final String ICONTYPE = "iconType";
    private static final String SORTE = "sorte";
    private static final String DAY = "day";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private static final String GEOPOINT = "Geopoint";
    private static final String GEO = "geo";
    private static final String IMAGES = "images";

    private static final String USER = "User";
    private static final String EMAIL = "email";
    private static final String FRIENDS = "friends";
    private static final String NAME = "name";
    private static final String TRIPCOLLECTION = "tripCollection";

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

        getTripDocumentIdAndTrip(id, new GetDocumentIdAndTripCallback() {
            @Override
            public void onCompleted(String id,Trip trip) {
                bean.setDocumentId(id);
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

    public void addPointToFireBase(String documentId) {

        ArrayList<String> images = new ArrayList<>();
        images.add("123");
        images.add("321");
        long i = 1554963108;


        Map<String, Object> point = new HashMap<>();
        point.put(ARRIVALTIME,i);
        point.put(COST, 100);
        point.put(DAY, 1);
        point.put(DESCRIBE, "woenvowknevbwpeivn");
        point.put(LATITUDE, 37.7853889);
        point.put(LONGITUDE, -122.4056973);
        point.put(ICONTYPE, "goo");
        point.put(IMAGES, images);
        point.put(SORTE, 3);
        point.put(TITLE, "taiai");

        db.collection(TRIP)
                .document(documentId)
                .collection(POINT)
                .add(point);
    }

    public void updatePointInformation(String documentId,Point point) {

        getPointDocumentId(documentId, point, new GetPointDocumentIdCallback() {
            @Override
            public void onCompleted(String id) {
                setUpdatePoint(documentId, id ,point);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void getTripDocumentIdAndTrip(int id, GetDocumentIdAndTripCallback callback) {

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

    private void getPointDocumentId(String tripId,Point point,GetPointDocumentIdCallback callback) {


        db.collection(TRIP)
                .document(tripId)
                .collection(POINT)
                .whereEqualTo(DAY,point.getDay())
                .whereEqualTo(SORTE,point.getSorte())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            callback.onCompleted(document.getId());
                        }
                    }
                });
    }

    private void setUpdatePoint(String documentId, String pointId, Point pointOld) {

        Map<String, Object> pointNew = new HashMap<>();
        pointNew.put(ARRIVALTIME, pointOld.getArrivalTime());
        pointNew.put(COST, pointOld.getCost());
        pointNew.put(DAY, pointOld.getDay());
        pointNew.put(DESCRIBE, pointOld.getDescribe());
        pointNew.put(LATITUDE, pointOld.getLatitude());
        pointNew.put(LONGITUDE, pointOld.getLongitude());
        pointNew.put(ICONTYPE, pointOld.getIconType());
        pointNew.put(IMAGES, pointOld.getImages());
        pointNew.put(SORTE, pointOld.getSorte());
        pointNew.put(TITLE, pointOld.getTitle());

        db.collection(TRIP)
                .document(documentId)
                .collection(POINT)
                .document(pointId)
                .set(pointNew);
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

    interface GetPointDocumentIdCallback {

        void onCompleted(String id);

        void onError(String errorMessage);
    }



}
