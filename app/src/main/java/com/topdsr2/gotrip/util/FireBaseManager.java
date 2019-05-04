package com.topdsr2.gotrip.util;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.topdsr2.gotrip.data.GoTripLocalDataSource;
import com.topdsr2.gotrip.data.GoTripRemoteDataSource;
import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.Request;
import com.topdsr2.gotrip.data.object.SearchData;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.data.object.TripAndPoint;
import com.topdsr2.gotrip.data.object.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static com.google.firebase.firestore.Query.Direction.DESCENDING;

public class FireBaseManager {

    private static final String TRIP = "Trip";
    private static final String ID = "id";
    private static final String COUNTRY = "country";
    private static final String CREATER = "creater";
    private static final String DESCRIBE = "describe";
    private static final String MAINIMAGE = "mainImage";
    private static final String ONTRIP = "onTrip";
    private static final String COMPLETE = "complete";
    private static final String OWNER = "owners";
    private static final String TITLE = "title";
    private static final String TRIPDAY = "tripDay";
    private static final String TRIPSTART = "tripStart";
    private static final String TRIPEND = "tripEnd";
    private static final String COLLECTIONNUMBER = "collectionNumber";
    private static final String ADDPOINTTIMES = "addPointTimes";


    private static final String POINT = "Point";
    private static final String ARRIVALTIME = "arrivalTime";
    private static final String COST = "cost";
    private static final String ICONTYPE = "iconType";
    private static final String SORTE = "sorte";
    private static final String DAY = "day";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String AGREE = "agree";
    private static final String DISAGREE = "disagree";
    private static final String VOTESTATUS = "voteStatus";

    private static final String GEOPOINT = "Geopoint";
    private static final String GEO = "geo";
    private static final String IMAGES = "images";

    private static final String USER = "User";
    private static final String EMAIL = "email";
    private static final String FRIENDS = "friends";
    private static final String NAME = "name";
    private static final String USERIMAGE = "userImage";
    private static final String TRIPCOLLECTION = "tripCollection";
    private static final String FRIENDREQUESTS = "friendRequests";
    private static final String TRIPREQUESTS = "tripRequests";

    private static final String TRUE = "true";


    private final GoTripRepository mGoTripRepository;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration mRegistration;

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


    public void setListener(String documentId, int addTimes, EvenHappendCallback callback) {

        if (mRegistration == null) {
            mRegistration = db.collection(TRIP)
                    .document(documentId)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                System.err.println("Listen failed: " + e);
                                return;
                            }
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                if (Integer.parseInt(documentSnapshot.getData().get(ADDPOINTTIMES).toString().trim()) != addTimes) {
                                    callback.onCompleted(documentSnapshot.getData().get(ID).toString().trim());
                                }
                            } else {
                                System.out.print("Current data: null");
                            }
                        }
                    });
        }
    }

    public void closeListener() {
        if (mRegistration != null) {
            mRegistration.remove();
        }
    }

    public void getAllTypeTrip(GetAllTripCallback callback) {
        ArrayList<Trip> trips = new ArrayList<>();
        db.collection(TRIP)
                .orderBy(TRIPSTART, DESCENDING).limit(50)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Trip trip = documentSnapshot.toObject(Trip.class);
                            trips.add(trip);
                        }
                        callback.onCompleted(trips);
                    }
                });
    }

    public void getSearchTrip(SearchData searchData, long startTime, long endTime, GetAllTripCallback callback) {
        ArrayList<Trip> trips = new ArrayList<>();

        db.collection(TRIP)
                .whereGreaterThanOrEqualTo(TRIPSTART, startTime)
                .whereLessThan(TRIPSTART, endTime)
                .orderBy(TRIPSTART, DESCENDING).limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Trip trip = documentSnapshot.toObject(Trip.class);

                            if (trip.getCountry().equals(searchData.getCountry())
                                    && trip.getCollectionNumber() >= searchData.getCollection()
                                    && trip.getTripDay() >= searchData.getDay()) {

                                trips.add(trip);

                            }
                        }
                        callback.onCompleted(trips);
                    }
                });
    }

    public void addUserCollection(String documentId, String email) {
        db.collection(USER)
                .whereEqualTo(EMAIL, email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Map<String, Object> addRequest = new HashMap<>();
                            addRequest.put(TRIPCOLLECTION, FieldValue.arrayUnion(documentId));

                            db.collection(USER)
                                    .document(documentSnapshot.getId())
                                    .update(addRequest);
                        }
                    }
                });
    }

    public void removeUserCollection(String documentId, String email) {
        db.collection(USER)
                .whereEqualTo(EMAIL, email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Map<String, Object> addRequest = new HashMap<>();
                            addRequest.put(TRIPCOLLECTION, FieldValue.arrayRemove(documentId));

                            db.collection(USER)
                                    .document(documentSnapshot.getId())
                                    .update(addRequest);
                        }
                    }
                });
    }

    public void setUserCollection(ArrayList<String> tripCollection, String email) {
        db.collection(USER)
                .whereEqualTo(EMAIL, email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Map<String, Object> addRequest = new HashMap<>();
                            addRequest.put(TRIPCOLLECTION, tripCollection);

                            db.collection(USER)
                                    .document(documentSnapshot.getId())
                                    .update(addRequest);
                        }
                    }
                });
    }

    public void getSelectedTrip(String tripId, FindTripCallback callback) {
        TripAndPoint bean = new TripAndPoint();

        getTripDocumentIdAndTrip(tripId, new GetDocumentIdAndTripCallback() {
            @Override
            public void onCompleted(String documentId, Trip trip) {
                bean.setDocumentId(documentId);
                bean.setTrip(trip);

                getTripPoint(documentId, new GetPointsCallback() {
                    @Override
                    public void onCompleted(ArrayList<Point> points) {
                        bean.setPoints(points);
                        callback.onCompleted(bean);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.v("FindTripCallback error", errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public void updatePointToFireBase(String documentId, Point point, int dayPoints, int today) {

        int i = dayPoints - 1;
        int oldSorte = dayPoints;
        int newSorte = dayPoints + 1;

        if (dayPoints >= point.getSorte()) {
            changePointSorte(documentId, today, oldSorte, newSorte, new GetPointDocumentIdCallback() {
                @Override
                public void onCompleted(String id) {
                    updatePointToFireBase(documentId, point, i, today);
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

    public void deletePoint(String documentId, int sorte, int positionHolder, int dayPoints, int today) {

        int i = sorte + 1;

        if (sorte == positionHolder) {
            readyToDeletePoint(documentId, sorte, today, new DeletePointCallback() {
                @Override
                public void onCompleted() {
                    deletePoint(documentId, i, positionHolder, dayPoints, today);
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        } else if (sorte > positionHolder && sorte <= dayPoints) {
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

    private void getTripDocumentIdAndTrip(String tripId, GetDocumentIdAndTripCallback callback) {

        db.collection(TRIP)
                .whereEqualTo(ID, tripId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Trip trip = document.toObject(Trip.class);
                            callback.onCompleted(document.getId(), trip);
                        }
                    }
                });
    }


    private void getTripPoint(String documentId, GetPointsCallback callback) {

        ArrayList<Point> points = new ArrayList<>();

        db.collection(TRIP)
                .document(documentId)
                .collection(POINT)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.getData().size() != 0) {
                                points.add(document.toObject(Point.class));
                            }

                        }
                        callback.onCompleted(points);
                    }
                });
    }

    private void changePointSorte(String documentId, int day, int oldSorte, int newSorte, GetPointDocumentIdCallback callback) {

        db.collection(TRIP)
                .document(documentId)
                .collection(POINT)
                .whereEqualTo(DAY, day)
                .whereEqualTo(SORTE, oldSorte)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                            Map<String, Object> update = new HashMap<>();
                            update.put(SORTE, newSorte);

                            db.collection(TRIP)
                                    .document(documentId)
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


    private void readyToAddPoint(String documentId, Point point) {

        db.collection(TRIP)
                .document(documentId)
                .collection(POINT)
                .add(point)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Map<String, Object> pointId = new HashMap<>();
                        pointId.put(ID, documentReference.getId().trim());

                        db.collection(TRIP)
                                .document(documentId)
                                .collection(POINT)
                                .document(documentReference.getId())
                                .update(pointId);

                    }
                });
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
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
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

    private void updateTripPointTimes(String documentId) {
        db.collection(TRIP)
                .document(documentId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Trip trip = documentSnapshot.toObject(Trip.class);

                        Map<String, Object> update = new HashMap<>();
                        update.put(ADDPOINTTIMES, trip.getAddPointTimes() + 1);

                        db.collection(TRIP)
                                .document(documentId)
                                .update(update);

                    }
                });
    }

    public void addUserData(String email, String name, String photoUri, GetUserDataCallback callback) {

        checkHasUserData(email, new GetUserDocumentIdCallback() {
            @Override
            public void onCompleted(String documentId) {

                getUserProfile(documentId, new GetUserProfileCallback() {
                    @Override
                    public void onCompleted(User user) {
                        callback.onCompleted(user);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

            }

            @Override
            public void onFailure() {

                Map<String, Object> userData = new HashMap<>();
                ArrayList<String> friends = new ArrayList<>();
                ArrayList<String> tripCollection = new ArrayList<>();
                ArrayList<String> tripRequests = new ArrayList<>();
                ArrayList<String> friendRequests = new ArrayList<>();
                userData.put(EMAIL, email);
                userData.put(NAME, name);
                userData.put(USERIMAGE, photoUri);
                userData.put(FRIENDS, friends);
                userData.put(TRIPCOLLECTION, tripCollection);
                userData.put(FRIENDREQUESTS, friendRequests);
                userData.put(TRIPREQUESTS, tripRequests);

                db.collection(USER)
                        .add(userData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                addUserData(email, name, photoUri, callback);
                            }
                        });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }

    private void checkHasUserData(String email, GetUserDocumentIdCallback callback) {

        db.collection(USER)
                .whereEqualTo(EMAIL, email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots.size() == 0) {
                            callback.onFailure();
                        } else {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                callback.onCompleted(document.getId());
                            }
                        }
                    }
                });
    }


    private void getUserProfile(String documentId, GetUserProfileCallback callback) {
        db.collection(USER)
                .document(documentId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        callback.onCompleted(user);
                    }
                });
    }

    public void loadIntenalData(String email, GetUserDataCallback callback) {

        db.collection(USER)
                .whereEqualTo(EMAIL, email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            User user = document.toObject(User.class);
                            callback.onCompleted(user);
                        }
                    }
                });
    }

    public void addFriendRequest(String email, String userEmail, AddFriendCallback callback) {
        checkHasUserData(email, new GetUserDocumentIdCallback() {
            @Override
            public void onCompleted(String documentId) {

                Map<String, Object> addRequest = new HashMap<>();
                addRequest.put(FRIENDREQUESTS, FieldValue.arrayUnion(userEmail));
                db.collection(USER).document(documentId)
                        .update(addRequest);

                callback.onCompleted();

            }

            @Override
            public void onFailure() {
                callback.OnFailure();
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public void agreeFriendRequest(String userEmail, String email) {
        removeFriendRequest(userEmail, email);

        addFriend(userEmail, email);
        addFriend(email, userEmail);
    }

    public void addFriend(String email, String addEmail) {
        db.collection(USER)
                .whereEqualTo(EMAIL, email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, Object> removeRequest = new HashMap<>();
                        removeRequest.put(FRIENDS, FieldValue.arrayUnion(addEmail));

                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            db.collection(USER).document(document.getId())
                                    .update(removeRequest);
                        }

                    }
                });
    }

    public void removeFriendRequest(String userEmail, String requestEmail) {
        db.collection(USER)
                .whereEqualTo(EMAIL, userEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, Object> removeRequest = new HashMap<>();
                        removeRequest.put(FRIENDREQUESTS, FieldValue.arrayRemove(requestEmail));

                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            db.collection(USER).document(document.getId())
                                    .update(removeRequest);
                        }

                    }
                });
    }

    public void addTripRequest(String AddEmail, String documentId, AddFriendCallback callback) {
        checkHasUserData(AddEmail, new GetUserDocumentIdCallback() {
            @Override
            public void onCompleted(String document) {

                Map<String, Object> addRequest = new HashMap<>();
                addRequest.put(TRIPREQUESTS, FieldValue.arrayUnion(documentId));
                db.collection(USER).document(document)
                        .update(addRequest);

                callback.onCompleted();

            }

            @Override
            public void onFailure() {
                callback.OnFailure();
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public void agreeTripRequest(String email, String documentId) {
        removeTripRequest(email, documentId);

        Map<String, Object> addRequest = new HashMap<>();
        addRequest.put(OWNER, FieldValue.arrayUnion(email));
        db.collection(TRIP).document(documentId)
                .update(addRequest);
    }

    public void removeTripRequest(String email, String documentId) {

        db.collection(USER)
                .whereEqualTo(EMAIL, email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, Object> removeRequest = new HashMap<>();
                        removeRequest.put(TRIPREQUESTS, FieldValue.arrayRemove(documentId));

                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            db.collection(USER)
                                    .document(document.getId())
                                    .update(removeRequest);
                        }

                    }
                });
    }

    public void removeOwner(String email, String documentId) {
        Map<String, Object> addRequest = new HashMap<>();
        addRequest.put(OWNER, FieldValue.arrayRemove(email));
        db.collection(TRIP).document(documentId)
                .update(addRequest);
    }

    public void getRequest(String email, GetRequestCallback callback) {
        Request request = new Request();
        final int[] i = {0};

        db.collection(USER)
                .whereEqualTo(EMAIL, email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            User user = document.toObject(User.class);

                            if (user.getFriendRequests().size() == 0) {
                                i[0]++;
                                if (i[0] == 2) {
                                    callback.onCompleted(request);
                                }
                            } else {
                                getFriendRequestData(request.getUsers(), user.getFriendRequests(), user.getFriendRequests().size(), 0, new GetFriendRequestDataCallback() {
                                    @Override
                                    public void onCompleted(ArrayList<User> users) {
                                        request.setUsers(users);
                                        i[0]++;
                                        if (i[0] == 2) {
                                            callback.onCompleted(request);
                                        }
                                    }

                                    @Override
                                    public void onError(String errorMessage) {

                                    }
                                });
                            }

                            if (user.getTripRequests().size() == 0) {
                                i[0]++;
                                if (i[0] == 2) {
                                    callback.onCompleted(request);
                                }
                            } else {
                                getTripRequestData(request.getTrips(), user.getTripRequests(), user.getTripRequests().size(), 0, new GetTripRequestDataCallback() {
                                    @Override
                                    public void onCompleted(ArrayList<Trip> trips) {
                                        request.setTrips(trips);
                                        i[0]++;
                                        if (i[0] == 2) {
                                            callback.onCompleted(request);
                                        }
                                    }

                                    @Override
                                    public void onError(String errorMessage) {

                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void getFriendRequestData(ArrayList<User> users, ArrayList<String> friendRequest, int size, int handleNumber, GetFriendRequestDataCallback callback) {
        int i = handleNumber + 1;

        db.collection(USER)
                .whereEqualTo(EMAIL, friendRequest.get(handleNumber))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            users.add((document.toObject(User.class)));

                            if (i < size) {
                                getFriendRequestData(users, friendRequest, size, i, callback);
                            } else {
                                callback.onCompleted(users);
                            }
                        }
                    }
                });
    }

    private void getTripRequestData(ArrayList<Trip> trips, ArrayList<String> tripRequest, int size, int handleNumber, GetTripRequestDataCallback callback) {
        int i = handleNumber + 1;

        db.collection(TRIP)
                .document(tripRequest.get(handleNumber))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        trips.add((documentSnapshot.toObject(Trip.class)));

                        if (i < size) {
                            getTripRequestData(trips, tripRequest, size, i, callback);
                        } else {
                            callback.onCompleted(trips);
                        }
                    }
                });
    }

    public void getHasOnTrip(String email, GetUserOnTripCallback callback) {
        db.collection(TRIP)
                .whereArrayContains(OWNER, email)
                .whereEqualTo(ONTRIP, Constants.TRUE)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots.size() == 0) {
                            callback.onFailure();
                        } else {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                callback.onCompleted(document.getData().get(ID).toString().trim());
                            }
                        }
                    }
                });
    }

    public void getNewTripData(String email, GetUserTripCallback callback) {
        ArrayList<Trip> trips = new ArrayList<>();

        db.collection(TRIP)
                .whereArrayContains(OWNER, email)
                .whereEqualTo(COMPLETE, Constants.FALSE)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.getData().size() != 0) {

                                trips.add(document.toObject(Trip.class));
                            }
                        }
                        callback.onCompleted(trips);

                    }
                });
    }

    public void getCompleteTripData(String email, GetUserTripCallback callback) {
        ArrayList<Trip> trips = new ArrayList<>();

        db.collection(TRIP)
                .whereArrayContains(OWNER, email)
                .whereEqualTo(COMPLETE, Constants.TRUE)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                            if (document.getData().size() != 0) {

                                trips.add(document.toObject(Trip.class));
                            }
                        }
                        callback.onCompleted(trips);
                    }
                });
    }

    public void getCollectionTripData(ArrayList<String> tripCollection, GetUserTripCallback callback) {
        ArrayList<Trip> trips = new ArrayList<>();

        if (tripCollection.size() != 0) {
            getColletionTrip(trips, tripCollection, tripCollection.size(),
                    0, new GetCollectionTripCallback() {
                        @Override
                        public void onCompleted(ArrayList<Trip> trips) {
                            callback.onCompleted(trips);
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });
        } else {
            callback.onCompleted(trips);
        }
    }

    private void getColletionTrip(ArrayList<Trip> trips, ArrayList<String> collections, int collectionsSize, int handleNumber,
                                  GetCollectionTripCallback callback) {

        int i = handleNumber + 1;


        db.collection(TRIP)
                .whereEqualTo(ID, collections.get(handleNumber))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            trips.add(document.toObject(Trip.class));

                            if (i < collectionsSize) {
                                getColletionTrip(trips, collections, collectionsSize, i, callback);
                            } else {
                                callback.onCompleted(trips);
                            }
                        }
                    }
                });
    }

    public void addNewTrip(Trip trip, AddNewTripCallback callback) {

        db.collection(TRIP)
                .add(trip)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Map<String, Object> addRequest = new HashMap<>();
                        addRequest.put(ID, documentReference.getId());
                        addRequest.put(OWNER, FieldValue.arrayUnion(trip.getCreater()));

                        db.collection(TRIP)
                                .document(documentReference.getId())
                                .update(addRequest);


                        Point point = new Point();

                        db.collection(TRIP)
                                .document(documentReference.getId())
                                .collection(POINT)
                                .add(point)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        callback.onCompleted(documentReference.getId());
                                    }
                                });
                    }
                });
    }

    public void deleteTrip(String documentId, DeleteTripCallback callback) {
        db.collection(TRIP)
                .document(documentId)
                .collection(POINT)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        final int[] i = {0};

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.getData().size() != 0) {
                                deleteAllPoints(documentId, document.getId(), new DeletePointCallback() {
                                    @Override
                                    public void onCompleted() {
                                        i[0]++;
                                        if (i[0] == queryDocumentSnapshots.size()) {
                                            db.collection(TRIP)
                                                    .document(documentId)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            callback.onCompleted();
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onError(String errorMessage) {

                                    }
                                });

                            } else {
                                db.collection(TRIP)
                                        .document(documentId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                callback.onCompleted();
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    private void deleteAllPoints(String documentId, String pointId, DeletePointCallback callback) {
        db.collection(TRIP)
                .document(documentId)
                .collection(POINT)
                .document(pointId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onCompleted();
                    }
                });
    }

    public void removeCollectionTrip(String documentId, String email, RemoveUserCollectionCallback callback) {

        db.collection(USER)
                .whereEqualTo(ID, email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Map<String, Object> removeRequest = new HashMap<>();
                        removeRequest.put(TRIPCOLLECTION, FieldValue.arrayRemove(documentId));

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            db.collection(USER)
                                    .document(document.getId())
                                    .update(removeRequest)
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

    public void votePoint(String documentId, String pointId, String type, int tripOwners, String email) {
        db.collection(TRIP)
                .document(documentId)
                .collection(POINT)
                .document(pointId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Point point = documentSnapshot.toObject(Point.class);
                        Map<String, Object> voteRequest = new HashMap<>();

                        switch (type) {
                            case AGREE:
                                voteRequest.put(AGREE, FieldValue.arrayUnion(email));

                                if (((point.getAgree().size() + 1) * 2) > tripOwners) {
                                    voteRequest.put(VOTESTATUS, AGREE);
                                }

                                db.collection(TRIP)
                                        .document(documentId)
                                        .collection(POINT)
                                        .document(pointId)
                                        .update(voteRequest);
                                break;

                            case DISAGREE:
                                voteRequest.put(DISAGREE, FieldValue.arrayUnion(email));

                                if (((point.getAgree().size() + 1) * 2) > tripOwners) {
                                    voteRequest.put(VOTESTATUS, DISAGREE);
                                }

                                db.collection(TRIP)
                                        .document(documentId)
                                        .collection(POINT)
                                        .document(pointId)
                                        .update(voteRequest);
                                break;
                            default:
                                break;
                        }

                        updateTripPointTimes(documentId);
                    }
                });
    }


    public interface FindTripCallback {

        void onCompleted(TripAndPoint bean);

        void onError(String errorMessage);
    }

    public interface GetAllTripCallback {

        void onCompleted(ArrayList<Trip> trips);

        void onError(String errorMessage);
    }

    public interface GetUserTripCallback {

        void onCompleted(ArrayList<Trip> trips);

        void onFailure(ArrayList<Trip> trips);

        void onError(String errorMessage);
    }

    public interface EvenHappendCallback {

        void onCompleted(String tripId);

        void onError(String errorMessage);
    }

    public interface GetUserDataCallback {

        void onCompleted(User user);

        void onError(String errorMessage);
    }

    public interface AddFriendCallback {

        void onCompleted();

        void OnFailure();

        void onError(String errorMessage);
    }

    public interface GetUserOnTripCallback {

        void onCompleted(String tripId);

        void onFailure();

        void onError(String errorMessage);
    }

    public interface AddNewTripCallback {

        void onCompleted(String tripId);

        void onFailure();

        void onError(String errorMessage);
    }

    public interface DeleteTripCallback {

        void onCompleted();

        void onError(String errorMessage);
    }

    public interface RemoveUserCollectionCallback {

        void onCompleted();

        void onError(String errorMessage);
    }

    public interface GetRequestCallback {

        void onCompleted(Request request);

        void onError(String errorMessage);
    }

    interface GetDocumentIdAndTripCallback {

        void onCompleted(String id, Trip trip);

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

    interface GetUserDocumentIdCallback {

        void onCompleted(String id);

        void onFailure();

        void onError(String errorMessage);
    }

    interface GetUserProfileCallback {

        void onCompleted(User user);

        void onError(String errorMessage);
    }

    interface AddUserData {

        void onCompleted();

        void onError(String errorMessage);
    }

    interface GetCollectionTripCallback {

        void onCompleted(ArrayList<Trip> trips);

        void onError(String errorMessage);
    }

    interface GetFriendRequestDataCallback {

        void onCompleted(ArrayList<User> users);

        void onError(String errorMessage);
    }

    interface GetTripRequestDataCallback {

        void onCompleted(ArrayList<Trip> trips);

        void onError(String errorMessage);
    }


}
