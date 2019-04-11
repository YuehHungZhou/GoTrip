package com.topdsr2.gotrip.trip;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.TripAndPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TripFragment extends Fragment implements TripContract.View, PlaceSelectionListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private TripContract.Presenter mPresenter;
    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mMap;
    private Marker mMarker;
    ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
    private LatLng aPoint;
    private LatLng bPoint;
    private int mVisibleItemPosition;


    private AutocompleteSupportFragment mAutocompleteSupportFragmen;
    private TripContentAdapter mTripContentAdapter;
    private TripContentItemAdapter mTripContentItemAdapter;
    private ImageView mAddPoint;
    private TripAndPoint mBean;


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
        mPresenter.loadTripData();
        placeInitialized();

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
                    mVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager())
                            .findFirstVisibleItemPosition();
                    recyclerView.smoothScrollToPosition(mVisibleItemPosition);
                    movePositionChangeIcon(mVisibleItemPosition);
                    mTripContentAdapter.scrollChangeIconInfo(mVisibleItemPosition);
                }
            }
        });
        recyclerView.setAdapter(mTripContentAdapter);

        RecyclerView recyclerViewIcon = root.findViewById(R.id.recycler_trip_content_icon);
        recyclerViewIcon.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewIcon.setAdapter(mTripContentItemAdapter);

        mAddPoint = root.findViewById(R.id.image_add_point);

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

            setMaker();
            setPolyLine();
            moveCamera();

            mMap.setOnMapClickListener(this);
            mMap.setOnMarkerClickListener(this);
        });

        mAddPoint.setOnClickListener(v -> mPresenter.addPoint(mBean.getDocumentId()));

    }

    @Override
    public void showTripUi(TripAndPoint bean) {
        mBean = bean;
        mTripContentAdapter.updateData(bean.getPoints());
        mTripContentItemAdapter.updateData(bean.getPoints());

    }

    @Override
    public void changeIconInfoUi(int posotion) {
        mTripContentAdapter.changeSelectedIconInfo(mVisibleItemPosition, posotion);
    }

    private void movePositionChangeIcon(int position) {
        mTripContentItemAdapter.readyChangeIcon(position);
    }


    @Override
    public void onPlaceSelected(@NonNull Place place) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 10));
        mMap.addMarker(new MarkerOptions().position(place.getLatLng())
                .title("Name:" + place.getName() + ". Address:" + place.getAddress()));

    }

    @Override
    public void onError(@NonNull Status status) {

    }


    @Override
    public void onMapClick(LatLng latLng) {
        if (mMarker != null) {
            mMarker.remove();
        }
        latLngs.add(new LatLng(latLng.latitude,latLng.longitude));
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("new"));
        mMarker = marker;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mMarker != null) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMarker = marker;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), 10));
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

    private void setMaker() {
        aPoint = new LatLng(37.4219999, -122.0862462);
        bPoint = new LatLng(37.4629101, -122.2449094);
        final LatLng cPoint = new LatLng(37.3092293, -122.1136845);

        MarkerOptions markerOptions1 = new MarkerOptions().position(aPoint).title("Marker in Sydney").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).snippet("Sydney is one of the most popular city in Australia");
        MarkerOptions markerOptions2 = new MarkerOptions().position(bPoint).title("Marker in Sydney").snippet("Sydney is one of the most popular city in Australia");
        MarkerOptions markerOptions3 = new MarkerOptions().position(cPoint).title("Marker in Sydney").snippet("Sydney is one of the most popular city in Australia");

        mMap.addMarker(markerOptions1);
        mMap.addMarker(markerOptions2);
        mMap.addMarker(markerOptions3);

    }

    private void setPolyLine() {
        latLngs.add(aPoint);
        LatLng[] latLngs1 = new LatLng[]{aPoint, bPoint};
        PolylineOptions polylineOpt1 = new PolylineOptions().add(latLngs1).pattern(setDash()).color(Color.BLUE);

        Polyline polyline = mMap.addPolyline(polylineOpt1);
        polyline.setWidth(10);
    }

    private void moveCamera() {
        CameraPosition googlePlex = CameraPosition.builder()
                .target(aPoint)
                .zoom(10)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);
    }


    private void addpolyLine(LatLng latLng) {
        LatLng[] latLngs1 = new LatLng[]{latLngs.get(latLngs.size()-2), latLng};
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
        }
    }

}
