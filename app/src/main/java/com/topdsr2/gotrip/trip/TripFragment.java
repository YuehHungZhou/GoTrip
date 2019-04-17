package com.topdsr2.gotrip.trip;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.polyak.iconswitch.IconSwitch;
import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.TripAndPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TripFragment extends Fragment implements TripContract.View, View.OnClickListener, PlaceSelectionListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private TripContract.Presenter mPresenter;
    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mMap;
    private Marker mMarker;
    private Marker mSelectedMarker;
    private ArrayList<LatLng> mLatLngs = new ArrayList<LatLng>();

    private int mVisibleItemPosition;
    private int mTouchedIconPosition;

    private AutocompleteSupportFragment mAutocompleteSupportFragmen;
    private TripContentAdapter mTripContentAdapter;
    private TripContentItemAdapter mTripContentItemAdapter;
    private TripAndPoint mBean;
    private ArrayList<Object> mPointsByDay;
    private ArrayList<Point> mPointsHolder;
    private ArrayList<Point> mReadyPoints;
    int mTripDay = 0;

    private ImageView mReloadImage;
    private ConstraintLayout mConstraintLayoutAdd;
    private ConstraintLayout mConstraintLayouDelete;
    private ConstraintLayout mConstraintLayouFriend;
    private IconSwitch mIconSwitchAdd;
    private IconSwitch mIconSwitchDelete;
    private ImageButton mFriendImageButton;
    private Button mAddFriendButton;
    private EditText mAddEditText;


    public TripFragment() {
    }

    public static TripFragment newInstance() {
        return new TripFragment();
    }

    @Override
    public void setPresenter(TripContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTripContentAdapter = new TripContentAdapter(mPresenter);
        mTripContentItemAdapter = new TripContentItemAdapter(mPresenter);
        placeInitialized();

        mPresenter.loadTripData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trip, container, false);

        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        mAutocompleteSupportFragmen = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete);

        RecyclerView recyclerView = root.findViewById(R.id.recycle_trip_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                            .findFirstVisibleItemPosition();
                    recyclerView.smoothScrollToPosition(mVisibleItemPosition);
                    mTripContentAdapter.scrollChangeIconInfo(mVisibleItemPosition, mPointsHolder);
                    mTripContentItemAdapter.readyChangeIcon(mVisibleItemPosition);
                    setMaker(((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition)));
                }
            }
        });
        recyclerView.setAdapter(mTripContentAdapter);

        RecyclerView recyclerViewIcon = root.findViewById(R.id.recycler_trip_content_icon);
        recyclerViewIcon.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewIcon.setAdapter(mTripContentItemAdapter);

        mReloadImage = root.findViewById(R.id.image_reload_point);
        mConstraintLayoutAdd = root.findViewById(R.id.constrant_add_question);
        mConstraintLayouDelete = root.findViewById(R.id.constrant_delete_question);
        mIconSwitchAdd = root.findViewById(R.id.icon_switch_add);
        mIconSwitchDelete = root.findViewById(R.id.icon_switch_delete);

        mConstraintLayouFriend = root.findViewById(R.id.constraint_friend);
        mFriendImageButton = root.findViewById(R.id.imageButton_trip_friend);
        mAddFriendButton = root.findViewById(R.id.button_trip_add_friend);
        mAddEditText = root.findViewById(R.id.edit_trip_add_friend);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAutocompleteSupportFragmen.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        mAutocompleteSupportFragmen.setOnPlaceSelectedListener(this);

        mSupportMapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setTrafficEnabled(true);
            mMap.clear();

            mMap.setOnMapClickListener(this);
            mMap.setOnMarkerClickListener(this);
        });

        mReloadImage.setOnClickListener(this);

        mFriendImageButton.setOnClickListener(this);
        mAddFriendButton.setOnClickListener(this);

        mIconSwitchAdd.setCheckedChangeListener(current -> {
            switch (mIconSwitchAdd.getChecked()) {

                case RIGHT:
                    mConstraintLayoutAdd.setVisibility(View.INVISIBLE);
                    mIconSwitchAdd.setChecked(IconSwitch.Checked.LEFT);
                    mPresenter.openAddOrDeletePoint();

                    break;
                default:
                    break;
            }
        });

        mIconSwitchDelete.setCheckedChangeListener(current -> {
            switch (mIconSwitchDelete.getChecked()) {

                case RIGHT:
                    mConstraintLayouDelete.setVisibility(View.INVISIBLE);
                    mIconSwitchDelete.setChecked(IconSwitch.Checked.LEFT);
                    mPresenter.deletePoint(mTouchedIconPosition);
                    break;
                default:
                    break;
            }
        });

    }

    @Override
    public void showTripUi(TripAndPoint bean) {
        mBean = bean;

        parsePointData();
        setMaker(((ArrayList<Point>) mPointsByDay.get(0)));

        mTripContentAdapter.updateData(mPointsByDay, mPointsHolder, mTripDay);
        mTripContentItemAdapter.updateData(mPointsByDay, mReadyPoints);


    }

    @Override
    public void reLoadData() {
        mPresenter.loadTripData();
    }

    @Override
    public int getToday() {
        return mVisibleItemPosition + 1;
    }

    @Override
    public int getPointNumber() {
        return mLatLngs.size();
    }

    @Override
    public Point addPointData(Point point) {

        point.setLatitude(mSelectedMarker.getPosition().latitude);
        point.setLongitude(mSelectedMarker.getPosition().longitude);
        point.setDay(mVisibleItemPosition + 1);
        point.setSorte(sorte(point.getArrivalTime()));

        return point;
    }

    @Override
    public void showPointDeleteView(int position) {
        mTouchedIconPosition = position;
        mConstraintLayouDelete.setVisibility(View.VISIBLE);

    }

    @Override
    public void reSetTripListener() {
        mPresenter.setTripListener(mBean.getDocumentId());
    }

    @Override
    public void changeIconInfoUi(int posotion) {
        mTripContentAdapter.changeSelectedIconInfo(mVisibleItemPosition, posotion);
    }


    @Override
    public void onPlaceSelected(@NonNull Place place) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 20));
        mMap.addMarker(new MarkerOptions().position(place.getLatLng())
                .title("Name:" + place.getName() + ". Address:" + place.getAddress()));

    }

    @Override
    public void onError(@NonNull Status status) {

    }


    @Override
    public void onMapClick(LatLng latLng) {

        mConstraintLayoutAdd.setVisibility(View.INVISIBLE);

        if (mMarker != null) {
            mMarker.remove();
        }

        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("new"));

        for (int i = 0; i < mLatLngs.size(); i++) {
            if (mLatLngs.get(i).latitude == latLng.latitude) {
                if (mLatLngs.get(i).longitude == latLng.longitude) {
                    marker.remove();
                }
            }
        }

        mMarker = marker;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (mMarker != null) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 20));

        mConstraintLayoutAdd.setVisibility(View.VISIBLE);

        mSelectedMarker = marker;

        return true;
    }

    @Override
    public void moveCameraToMarker(Double latitude, Double longitude) {
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(latitude, longitude))
                .zoom(13)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 3000, null);
    }


    private void setMaker(ArrayList<Point> points) {

        mMap.clear();
        mLatLngs.clear();
        mPresenter.setTripListener(mBean.getDocumentId());

        for (int i = 0; i < points.size(); i++) {
            LatLng latLng = new LatLng(points.get(i).getLatitude(), points.get(i).getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(markerOptions);

            mLatLngs.add(latLng);
        }
        setPolyLine();
        moveCamera();
    }

    private void setPolyLine() {

        if (mLatLngs.size() > 1) {
            for (int i = 0; i < mLatLngs.size() - 1; i++) {
                LatLng[] latLngs = new LatLng[]{mLatLngs.get(i), mLatLngs.get(i + 1)};
                PolylineOptions polylineOpt = new PolylineOptions().add(latLngs).pattern(setDash()).color(Color.BLUE);
                Polyline polyline = mMap.addPolyline(polylineOpt);
                polyline.setWidth(10);
            }
        }
    }

    private void moveCamera() {
        CameraPosition googlePlex = CameraPosition.builder()
                .target(mLatLngs.get(0))
                .zoom(10)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onCancel() {

            }
        });
    }


    private void addpolyLine(LatLng latLng) {
        LatLng[] latLngs1 = new LatLng[]{mLatLngs.get(mLatLngs.size() - 2), latLng};
        PolylineOptions polylineOpt = new PolylineOptions().add(latLngs1).pattern(setDash()).color(Color.BLUE);
        Polyline polyline = mMap.addPolyline(polylineOpt);
        polyline.setWidth(10);
    }

    private List<PatternItem> setDash() {
        Dash myDASH = new Dash(50);
        Gap myGAP = new Gap(20);
        List<PatternItem> PATTERN_DASHED = Arrays.asList(myDASH, myGAP);
        return PATTERN_DASHED;
    }

    private void placeInitialized() {
        if (!Places.isInitialized()) {
            Places.initialize(GoTrip.getmContext(), "AIzaSyAjuPCcWs8ZbWwnIU8EmkgXZBgsfkOgPp0");
            PlacesClient placesClient = Places.createClient(getContext());

        }
    }


    private void parsePointData() {

        mPointsByDay = new ArrayList<>();
        mPointsHolder = new ArrayList<>();
        mReadyPoints = new ArrayList<>();
        mTripDay = 0;


        for (int i = 0; i < mBean.getPoints().size(); i++) {
            if (mBean.getPoints().get(i).getDay() > mTripDay) {
                mTripDay = mBean.getPoints().get(i).getDay();
            }
        }

        for (int i = 1; i <= mTripDay; i++) {
            ArrayList<Point> points = new ArrayList<>();

            for (int j = 0; j < mBean.getPoints().size(); j++) {
                if (mBean.getPoints().get(j).getDay() == i) {

                    points.add(mBean.getPoints().get(j));
                }
            }
            mPointsByDay.add(sortPoint(points));
        }

        mReadyPoints = (ArrayList<Point>) mPointsByDay.get(0);

        for (int i = 0; i < mTripDay; i++) {

            for (int j = 0; j < ((ArrayList<Point>) mPointsByDay.get(i)).size(); j++) {

                if (((ArrayList<Point>) mPointsByDay.get(i)).get(j).getSorte() == 1) {
                    mPointsHolder.add(((ArrayList<Point>) mPointsByDay.get(i)).get(j));
                }
            }
        }
    }

    private ArrayList<Point> sortPoint(ArrayList<Point> points) {
        ArrayList<Point> pointsDayHolder = new ArrayList<>();
        int sortNumber = 1;
        do {
            for (int i = 0; i < points.size(); i++) {
                if (points.get(i).getSorte() == sortNumber) {
                    pointsDayHolder.add(points.get(i));
                    sortNumber++;
                }
            }
        } while (pointsDayHolder.size() != points.size());

        return pointsDayHolder;
    }

    private int sorte(long time) {
        int sorte = 0;
        for (int i = 0; i < ((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition)).size(); i++) {
            if (time <= ((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition)).get(i).getArrivalTime()) {
                sorte = ((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition)).get(i).getSorte();
                break;
            }
        }
        return sorte;
    }


    @Override
    public void onStop() {
        super.onStop();
//        mPresenter.removeListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_reload_point:
                mPresenter.loadTripData();
                break;
            case R.id.imageButton_trip_friend:
                mConstraintLayouFriend.setVisibility(View.VISIBLE);

                break;
            case R.id.button_trip_add_friend:
                mPresenter.addTripRequest(mAddEditText.getText().toString());
                break;
            default:
                break;

        }
    }
}
