package com.topdsr2.gotrip.util;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
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

import javax.annotation.Nullable;

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
    private static final String ADDPOINTTIMES = "addPointTimes";



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
    private ListenerRegistration registration;

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





    public void getSelectedTrip(int id, FindTripCallback callback) {
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
                        Log.v("FindTripCallback error",errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }



    public void setListener(String documentId, EvenHappendCallback callback) {
        registration = db.collection(TRIP)
                            .document(documentId)
                            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    callback.onCompleted();
                                }
                            });
    }



    public void updatePointToFireBase(String documentId,Point point, int dayPoints, int today) {

        int i = dayPoints - 1;
        int oldSorte = dayPoints;
        int newSorte = dayPoints + 1;

        if (dayPoints >= point.getSorte()) {
            changePointSorte(documentId, today, oldSorte, newSorte, new GetPointDocumentIdCallback() {
                @Override
                public void onCompleted(String id) {
                    updatePointToFireBase(documentId,point,i, today);
                }
                @Override
                public void onError(String errorMessage) {

                }
            });

        } else if (dayPoints < point.getSorte()) {
            readyToAddPoint(documentId, point);
            updateTripPointTimes(documentId);
        }
    }

    public void deletePoint(String documentId,int sorte, int positionHolder, int dayPoints, int today) {

        int i = sorte + 1;


        if (sorte == positionHolder){
            readyToDeletePoint(documentId, sorte, today, new DeletePointCallback() {
                @Override
                public void onCompleted() {
                    deletePoint(documentId, i, positionHolder, dayPoints, today);
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        } else if (sorte > positionHolder && sorte <= dayPoints - 1){
            changePointSorte(documentId, today, sorte, sorte - 1, new GetPointDocumentIdCallback() {
                @Override
                public void onCompleted(String id) {
                    deletePoint(documentId, i, positionHolder, dayPoints, today);
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        } else {
            updateTripPointTimes(documentId);
        }



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


    private void changePointSorte(String tripId, int day, int oldSorte, int newSorte, GetPointDocumentIdCallback callback) {

        db.collection(TRIP)
                .document(tripId)
                .collection(POINT)
                .whereEqualTo(DAY,day)
                .whereEqualTo(SORTE,oldSorte)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                            Map<String,Object> update = new HashMap<>();
                            update.put(SORTE, newSorte);

                            db.collection(TRIP)
                                    .document(tripId)
                                    .collection(POINT)
                                    .document(document.getId())
                                    .update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    callback.onCompleted(document.getId());

                                }
                            });
                        }
                    }
                });
    }


    private void readyToAddPoint(String documentId, Point pointOld) {

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
                .add(pointNew);
    }

    private void readyToDeletePoint(String documentId, int pointSorte, int day, DeletePointCallback callback) {

        db.collection(TRIP)
                .document(documentId)
                .collection(POINT)
                .whereEqualTo(DAY, day)
                .whereEqualTo(SORTE, pointSorte)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    db.collection(TRIP)
                            .document(documentId)
                            .collection(POINT)
                            .document(documentSnapshot.getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    callback.onCompleted();
                                }
                            });
                }
            }
        });
    }

    private void updateTripPointTimes(String id) {
        db.collection(TRIP)
                .document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                            Trip trip = documentSnapshot.toObject(Trip.class);

                            Map<String, Object> update = new HashMap<>();
                            update.put(ADDPOINTTIMES, trip.getAddPointTimes() + 1);

                            db.collection(TRIP)
                                    .document(id)
                                    .update(update);

                    }
                });
    }

    public void closeListener() {
//        registration.remove();
    }


    public interface FindTripCallback {

        void onCompleted(TripAndPoint bean);

        void onError(String errorMessage);
    }

    public interface EvenHappendCallback {

        void onCompleted();

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

    interface DeletePointCallback {

        void onCompleted();

        void onError(String errorMessage);
    }



}
