package com.topdsr2.gotrip.trip;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.util.MapManager;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

public class TripFragment extends Fragment implements TripContract.View, PlaceSelectionListener {

    private TripContract.Presenter mPresenter;
    private GoogleMap mMap;
    private AutocompleteSupportFragment mAutocompleteSupportFragmen;
    private SupportMapFragment mSupportMapFragment;


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

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trip, container, false);

        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        mAutocompleteSupportFragmen = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAutocompleteSupportFragmen.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        mAutocompleteSupportFragmen.setOnPlaceSelectedListener(this);

        MapManager.getInstance().loadMap(mSupportMapFragment);

    }

    @Override
    public void showTripUi() {

    }


    @Override
    public void onPlaceSelected(@NonNull Place place) {
        MapManager.getInstance().searchPlace(place);
    }

    @Override
    public void onError(@NonNull Status status) {

    }


}
