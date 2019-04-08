package com.topdsr2.gotrip.trip;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TripFragment extends Fragment implements TripContract.View {

    private TripContract.Presenter mPresenter;
    private GoogleMap mMap;
    ArrayList<LatLng> latLngs = new ArrayList<LatLng>();


    public TripFragment() {
    }

    public static TripFragment newInstance() {
        return new TripFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setPresenter(TripContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trip, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment

        if (!Places.isInitialized()) {
            Places.initialize(GoTrip.getmContext(), "AIzaSyAjuPCcWs8ZbWwnIU8EmkgXZBgsfkOgPp0");
        }

        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                try {

                    if (mMap == null) {
                        Log.v("map error", "null mMap");
                    } else {

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 10));
                        mMap.addMarker(new MarkerOptions().position(place.getLatLng())
                                .title("Name:" + place.getName() + ". Address:" + place.getAddress()));

                    }
                } catch (Exception ex) {
                    Log.e("error",""+ex);
                }

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });



        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setTrafficEnabled(true);


                mMap.clear();

                final LatLng aPoint = new LatLng(37.4219999, -122.0862462);
                final LatLng bPoint = new LatLng(37.4629101, -122.2449094);
                final LatLng cPoint = new LatLng(37.3092293, -122.1136845);

                MarkerOptions markerOptions1 = new MarkerOptions().position(aPoint).title("Marker in Sydney").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).snippet("Sydney is one of the most popular city in Australia");
                MarkerOptions markerOptions2 = new MarkerOptions().position(bPoint).title("Marker in Sydney").snippet("Sydney is one of the most popular city in Australia");
                MarkerOptions markerOptions3 = new MarkerOptions().position(cPoint).draggable(true).title("Marker in Sydney").snippet("Sydney is one of the most popular city in Australia");

                mMap.addMarker(markerOptions1).showInfoWindow();
                mMap.addMarker(markerOptions2);
                mMap.addMarker(markerOptions3);


                latLngs.add(aPoint);
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        mMap.addMarker(new MarkerOptions().position(latLng).title("new"));
                        mMap.addPolyline(new PolylineOptions().add(latLngs.get(latLngs.size()-1),latLng));
                        latLngs.add(new LatLng(latLng.latitude,latLng.longitude));

                        Log.v("size",latLngs.size()+"");
                    }
                });

                LatLng[] latLngs1 = new LatLng[]{aPoint, bPoint};
                LatLng[] latLngs2 = new LatLng[]{bPoint, cPoint};

                Dash myDASH = new Dash(50);
                Gap myGAP = new Gap(20);
                List<PatternItem> PATTERN_DASHED = Arrays.asList(myDASH, myGAP);

                PolylineOptions polylineOpt1 = new PolylineOptions().add(latLngs1).pattern(PATTERN_DASHED).color(Color.BLUE);
                PolylineOptions polylineOpt2 = new PolylineOptions().add(latLngs2).color(Color.BLACK);

                mMap.addPolyline(polylineOpt2);
                Polyline polyline = mMap.addPolyline(polylineOpt1);
                polyline.setWidth(10);



                CameraPosition googlePlex = CameraPosition.builder()
                        .target(aPoint)
                        .zoom(10)
                        .bearing(0)
                        .tilt(45)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);



            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void showTripUi() {

    }
}
