package com.topdsr2.gotrip.trip;

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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.TripAndPoint;
import com.topdsr2.gotrip.util.MapManager;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

public class TripFragment extends Fragment implements TripContract.View, PlaceSelectionListener {

    private TripContract.Presenter mPresenter;
    private GoogleMap mMap;
    private AutocompleteSupportFragment mAutocompleteSupportFragmen;
    private SupportMapFragment mSupportMapFragment;
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
                    int position = ((LinearLayoutManager)recyclerView.getLayoutManager())
                            .findFirstVisibleItemPosition();
                    recyclerView.smoothScrollToPosition(position);
                    movePositionChangeIcon(position);
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

        MapManager.getInstance().loadMap(mSupportMapFragment);

        mAddPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addPoint(mBean.getDocumentId());
            }
        });

    }

    @Override
    public void showTripUi(TripAndPoint bean) {
        mBean = bean;
        mTripContentAdapter.updateData(bean.getPoints());
        mTripContentItemAdapter.updateData(bean.getPoints());

    }


    @Override
    public void onPlaceSelected(@NonNull Place place) {
        MapManager.getInstance().searchPlace(place);
    }

    @Override
    public void onError(@NonNull Status status) {

    }

    private void movePositionChangeIcon(int position) {
        mTripContentItemAdapter.readyChangeIcon(position);
    }


}
