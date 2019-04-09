package com.topdsr2.gotrip.util;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.data.GoTripLocalDataSource;
import com.topdsr2.gotrip.data.GoTripRemoteDataSource;
import com.topdsr2.gotrip.data.GoTripRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapManager {

    private final GoTripRepository mGoTripRepository;
    private GoogleMap mMap;
    ArrayList<LatLng> latLngs = new ArrayList<LatLng>();

    private LatLng aPoint;
    private LatLng bPoint;

    private static class MapManagerHolder {
        private static final MapManager INSTANCE = new MapManager();
    }

    private MapManager() {
        mGoTripRepository = GoTripRepository.getInstance(
                GoTripRemoteDataSource.getInstance(),
                GoTripLocalDataSource.getInstance());

        placeInitialized();

    }

    public static MapManager getInstance() {
        return MapManagerHolder.INSTANCE;
    }

    private void placeInitialized() {
        if (!Places.isInitialized()) {
            Places.initialize(GoTrip.getmContext(), "AIzaSyAjuPCcWs8ZbWwnIU8EmkgXZBgsfkOgPp0");
        }
    }


    public void loadMap(SupportMapFragment supportMapFragment) {
        supportMapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setTrafficEnabled(true);
            mMap.clear();

            mMap.setOnMapClickListener(latLng -> {
                addMaker(latLng);
            });


            setMaker();
            setPolyLine();
            moveCamera();
        });
    }

    private void setMaker() {
        aPoint = new LatLng(37.4219999, -122.0862462);
        bPoint = new LatLng(37.4629101, -122.2449094);
        final LatLng cPoint = new LatLng(37.3092293, -122.1136845);

        MarkerOptions markerOptions1 = new MarkerOptions().position(aPoint).title("Marker in Sydney").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).snippet("Sydney is one of the most popular city in Australia");
        MarkerOptions markerOptions2 = new MarkerOptions().position(bPoint).title("Marker in Sydney").snippet("Sydney is one of the most popular city in Australia");
        MarkerOptions markerOptions3 = new MarkerOptions().position(cPoint).title("Marker in Sydney").snippet("Sydney is one of the most popular city in Australia");

        mMap.addMarker(markerOptions1).showInfoWindow();
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

    private void addMaker(LatLng latLng) {
        latLngs.add(new LatLng(latLng.latitude,latLng.longitude));
        mMap.addMarker(new MarkerOptions().position(latLng).title("new"));
        addPolyLine(latLng);
    }

    private void addPolyLine(LatLng latLng) {
        LatLng[] latLngs1 = new LatLng[]{latLngs.get(latLngs.size()-2), latLng};
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
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

    }

    private List<PatternItem> setDash() {
        Dash myDASH = new Dash(50);
        Gap myGAP = new Gap(20);
        List<PatternItem> PATTERN_DASHED = Arrays.asList(myDASH, myGAP);
        return PATTERN_DASHED;
    }

    public void searchPlace(Place place) {
        try {
            if (mMap == null) {
                Log.v("map error", "null mMap");
            } else {
                moveToSelected(place);
            }
        } catch (Exception ex) {
            Log.e("error",""+ex);
        }
    }

    private void moveToSelected(Place place) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 10));
        mMap.addMarker(new MarkerOptions().position(place.getLatLng())
                .title("Name:" + place.getName() + ". Address:" + place.getAddress()));
    }

}
