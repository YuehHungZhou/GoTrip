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
import android.widget.TextView;

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
import com.topdsr2.gotrip.util.Constants;
import com.topdsr2.gotrip.util.UserManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.polyak.iconswitch.IconSwitch.Checked.LEFT;

public class TripFragment extends Fragment implements TripContract.View, View.OnClickListener,
        PlaceSelectionListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private TripContract.Presenter mPresenter;
    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mMap;
    private Marker mMarker;
    private Marker mSelectedMarker;
    private ArrayList<LatLng> mLatLngs = new ArrayList<LatLng>();

    private int mVisibleItemPosition;
    private int mTouchedIconPosition;
    private boolean isOwner;

    private AutocompleteSupportFragment mAutocompleteSupportFragmen;
    private TripContentAdapter mTripContentAdapter;
    private TripContentItemAdapter mTripContentItemAdapter;
    private TripAndPoint mBean;
    private ArrayList<Object> mPointsByDay;
    private ArrayList<Point> mPointsHolder;
    private ArrayList<Point> mReadyPoints;
    private int mTripDay = 0;
    private boolean mFriendStatus;

    private ImageView mReloadImage;
    private ConstraintLayout mConstraintLayouAdd;
    private ConstraintLayout mConstraintLayouDelete;
    private ConstraintLayout mConstraintLayouFriend;
    private ConstraintLayout mConstraintLayoutSearch;
    private ConstraintLayout mConstraintLayoutVote;
    private TextView mVoteTitleText;
    private ImageButton mVoteAgreeImage;
    private ImageButton mVoteDisagreeImage;
    private IconSwitch mIconSwitchAdd;
    private IconSwitch mIconSwitchDelete;
    private ImageButton mFriendImageButton;
    private ImageButton mAddFriendImageButton;
    private ImageButton mTalkFriendImageButton;
    private Button mAddFriendButton;
    private EditText mAddEditText;
    private RecyclerView mInfoRecyclerView;
    private RecyclerView mIconRecyclerView;


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

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trip, container, false);

        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        mAutocompleteSupportFragmen = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete);

        mInfoRecyclerView = root.findViewById(R.id.recycle_trip_content);
        mInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mInfoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        mInfoRecyclerView.setAdapter(mTripContentAdapter);

        mIconRecyclerView = root.findViewById(R.id.recycler_trip_content_icon);
        mIconRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mIconRecyclerView.setAdapter(mTripContentItemAdapter);

        mReloadImage = root.findViewById(R.id.image_reload_point);
        mConstraintLayouAdd = root.findViewById(R.id.constrant_add_question);
        mConstraintLayouDelete = root.findViewById(R.id.constrant_delete_question);
        mConstraintLayoutSearch = root.findViewById(R.id.constraint_trip_search);
        mConstraintLayoutVote = root.findViewById(R.id.constraint_vote);
        mVoteTitleText = root.findViewById(R.id.text_trip_vote_title);
        mVoteAgreeImage = root.findViewById(R.id.image_trip_vote_agree);
        mVoteDisagreeImage = root.findViewById(R.id.image_trip_vote_disagree);
        mIconSwitchAdd = root.findViewById(R.id.icon_switch_add);
        mIconSwitchDelete = root.findViewById(R.id.icon_switch_delete);

        mConstraintLayouFriend = root.findViewById(R.id.constraint_friend);
        mFriendImageButton = root.findViewById(R.id.imageButton_trip_friend);
        mAddFriendImageButton = root.findViewById(R.id.imageButton_add_friend);
        mTalkFriendImageButton = root.findViewById(R.id.imageButton_talk_friend);
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
        mAddFriendImageButton.setOnClickListener(this);
        mTalkFriendImageButton.setOnClickListener(this);
        mAddFriendButton.setOnClickListener(this);
        mVoteAgreeImage.setOnClickListener(this);
        mVoteDisagreeImage.setOnClickListener(this);

        mIconSwitchAdd.setCheckedChangeListener(current -> {
            switch (mIconSwitchAdd.getChecked()) {

                case RIGHT:
                    mConstraintLayouAdd.setVisibility(View.INVISIBLE);
                    mIconSwitchAdd.setChecked(LEFT);
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
                    mIconSwitchDelete.setChecked(LEFT);
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

        mPresenter.checkIsOwner();

        mPointsByDay = new ArrayList<>();
        mPointsHolder = new ArrayList<>();
        mReadyPoints = new ArrayList<>();
        mTripDay = mBean.getTrip().getTripDay();

        parsePointData();
        if (mMap != null) {
            setMaker(((ArrayList<Point>) mPointsByDay.get(0)));
        }

        mTripContentAdapter.updateData(mPointsByDay, mPointsHolder, mTripDay);
        mTripContentItemAdapter.updateData(mPointsByDay, mReadyPoints);

        mInfoRecyclerView.smoothScrollToPosition(0);
    }


    @Override
    public int getToday() {
        return mVisibleItemPosition + 1;
    }

    @Override
    public int getPointNumber() {
        return ((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition)).size();
    }

    @Override
    public Point addPointData(Point point) {

        point.setLatitude(mSelectedMarker.getPosition().latitude);
        point.setLongitude(mSelectedMarker.getPosition().longitude);
        point.setSorte(sorte(point.getArrivalTime()));

        return point;
    }

    @Override
    public void showPointDeleteView(int position) {
        mTouchedIconPosition = position;
        if (isOwner) {
            mConstraintLayouDelete.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void reSetTripListener() {
        mPresenter.setTripListener(mBean.getDocumentId());
    }

    @Override
    public void closeFunction(boolean ownerStatus) {
        isOwner = ownerStatus;
        mReloadImage.setVisibility(View.INVISIBLE);
        mFriendImageButton.setVisibility(View.INVISIBLE);
        mAddFriendImageButton.setVisibility(View.INVISIBLE);
        mTalkFriendImageButton.setVisibility(View.INVISIBLE);
        mConstraintLayoutSearch.setVisibility(View.INVISIBLE);

    }

    @Override
    public void openFunction(boolean ownerStatus) {
        isOwner = ownerStatus;
        mReloadImage.setVisibility(View.VISIBLE);
        mFriendImageButton.setVisibility(View.VISIBLE);
        mAddFriendImageButton.setVisibility(View.VISIBLE);
        mTalkFriendImageButton.setVisibility(View.VISIBLE);
        mConstraintLayoutSearch.setVisibility(View.VISIBLE);

    }

    @Override
    public void showVoteViewUi(int position) {
        mTouchedIconPosition = position;
        if (isOwner) {
            if (checkCanVote(mVisibleItemPosition, mTouchedIconPosition)) {
                mConstraintLayoutVote.setVisibility(View.VISIBLE);

                switch (checkIsVoted(mVisibleItemPosition, mTouchedIconPosition)) {
                    case Constants.AGREE:
                        mVoteTitleText.setText("已贊成");
                        mVoteAgreeImage.setVisibility(View.VISIBLE);
                        mVoteDisagreeImage.setVisibility(View.INVISIBLE);
                        mVoteAgreeImage.setClickable(false);
                        break;
                    case Constants.DISAGREE:
                        mVoteTitleText.setText("已不贊成");
                        mVoteAgreeImage.setVisibility(View.INVISIBLE);
                        mVoteDisagreeImage.setVisibility(View.VISIBLE);
                        mVoteDisagreeImage.setClickable(false);
                        break;
                    case Constants.NOTVOTE:
                        mVoteTitleText.setText("加入旅程");
                        mVoteAgreeImage.setVisibility(View.VISIBLE);
                        mVoteDisagreeImage.setVisibility(View.VISIBLE);
                        mVoteAgreeImage.setClickable(true);
                        mVoteDisagreeImage.setClickable(true);
                        break;
                    default:
                        break;
                }
            }
        }
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
    public void onStop() {
        super.onStop();
        mPresenter.removeListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_reload_point:
//                mPresenter.loadTripData();
                break;
            case R.id.imageButton_trip_friend:
                friendAnimate();
                break;
            case R.id.imageButton_add_friend:
                mConstraintLayouFriend.setVisibility(View.VISIBLE);
                break;
            case R.id.imageButton_talk_friend:
                break;
            case R.id.button_trip_add_friend:
                mPresenter.addTripRequest(mAddEditText.getText().toString().trim());
                break;
            case R.id.image_trip_vote_agree:
                mPresenter.vote(
                        ((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition)).get(mTouchedIconPosition).getId(),
                        Constants.AGREE);
                break;
            case R.id.image_trip_vote_disagree:
                mPresenter.vote(
                        ((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition)).get(mTouchedIconPosition).getId(),
                        Constants.DISAGREE);
                break;
            default:
                break;

        }
    }


    @Override
    public void onMapClick(LatLng latLng) {

        mConstraintLayouAdd.setVisibility(View.INVISIBLE);
        mConstraintLayouDelete.setVisibility(View.INVISIBLE);
        mConstraintLayoutVote.setVisibility(View.INVISIBLE);


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
        } else {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 20));

        if (isOwner) {
            mConstraintLayouAdd.setVisibility(View.VISIBLE);
        }

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

        if (points.size() != 0) {
            mMap.clear();
            mLatLngs.clear();
            mPresenter.setTripListener(mBean.getDocumentId());

            for (int i = 0; i < points.size(); i++) {
                LatLng latLng = new LatLng(points.get(i).getLatitude(), points.get(i).getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.addMarker(markerOptions);
                mLatLngs.add(latLng);
            }
            setPolyLine(points);
            moveCamera();
        } else {
            mMap.clear();
        }

    }

    private void setPolyLine(ArrayList<Point> points) {

        if (mLatLngs.size() > 1) {
            for (int i = 0; i < mLatLngs.size() - 1; i++) {
                if (points.get(i).getVoteStatus().equals(Constants.AGREE)
                        && points.get(i + 1).getVoteStatus().equals(Constants.AGREE)) {
                    LatLng[] latLngs = new LatLng[]{mLatLngs.get(i), mLatLngs.get(i + 1)};
                    PolylineOptions agreePolyline = new PolylineOptions().add(latLngs).color(Color.parseColor("#FF0000"));
                    Polyline polyline = mMap.addPolyline(agreePolyline);
                    polyline.setWidth(10);

                } else {
                    LatLng[] latLngs = new LatLng[]{mLatLngs.get(i), mLatLngs.get(i + 1)};
                    PolylineOptions dashPolyline = new PolylineOptions().add(latLngs).pattern(setDash()).color(Color.parseColor("#FFEDCC70"));
                    Polyline polyline = mMap.addPolyline(dashPolyline);
                    polyline.setWidth(10);
                }
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

    private List<PatternItem> setDash() {
        Dash dash = new Dash(50);
        Gap gap = new Gap(20);
        List<PatternItem> patternDashed = Arrays.asList(dash, gap);
        return patternDashed;
    }

    private void placeInitialized() {
        if (!Places.isInitialized()) {
            Places.initialize(GoTrip.getmContext(), "AIzaSyAjuPCcWs8ZbWwnIU8EmkgXZBgsfkOgPp0");
            PlacesClient placesClient = Places.createClient(getContext());

        }
    }

    private void parsePointData() {

        for (int i = 1; i <= mTripDay; i++) {
            ArrayList<Point> points = new ArrayList<>();

            if (mBean.getPoints().size() != 0) {
                for (int j = 0; j < mBean.getPoints().size(); j++) {
                    if (mBean.getPoints().get(j).getDay() == i) {

                        points.add(mBean.getPoints().get(j));
                    }
                }
            }
            mPointsByDay.add(sortPoint(points));
        }

        mReadyPoints = (ArrayList<Point>) mPointsByDay.get(0);

        for (int i = 0; i < mTripDay; i++) {

            if (((ArrayList<Point>) mPointsByDay.get(i)).size() != 0) {
                for (int j = 0; j < ((ArrayList<Point>) mPointsByDay.get(i)).size(); j++) {

                    if (((ArrayList<Point>) mPointsByDay.get(i)).get(j).getSorte() == 1) {
                        mPointsHolder.add(((ArrayList<Point>) mPointsByDay.get(i)).get(j));
                    }
                }
            } else {
                mPointsHolder.add(new Point());
            }
        }
    }

    private ArrayList<Point> sortPoint(ArrayList<Point> points) {
        ArrayList<Point> pointsDayHolder = new ArrayList<>();

        if (points.size() != 0) {
            int sortNumber = 1;
            do {
                for (int i = 0; i < points.size(); i++) {
                    if (points.get(i).getSorte() == sortNumber) {
                        pointsDayHolder.add(points.get(i));
                        sortNumber++;
                    }
                }
            } while (pointsDayHolder.size() != points.size());
        }

        return pointsDayHolder;
    }

    private int sorte(long time) {
        ArrayList<Point> points = ((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition));
        int size = points.size();
        int sorte = 0;

        if (size != 0) {

            if (points.get(size - 1).getArrivalTime() <= time) {
                sorte = points.get(size - 1).getSorte() + 1;
            } else {
                for (int i = 0; i < size; i++) {
                    if (time <= points.get(i).getArrivalTime()) {
                        sorte = points.get(i).getSorte();
                        break;
                    }
                }
            }

        } else {
            sorte = 1;
        }
        return sorte;
    }

    private String checkIsVoted(int visibleItemPosition, int touchedIconPosition) {
        String email = UserManager.getInstance().getUser().getEmail();
        if (((ArrayList<Point>) mPointsByDay.get(visibleItemPosition)).get(touchedIconPosition).getAgree().contains(email.trim())) {
            return Constants.AGREE;
        } else if (((ArrayList<Point>) mPointsByDay.get(visibleItemPosition)).get(touchedIconPosition).getDisagree().contains(email.trim())) {
            return Constants.DISAGREE;
        } else {
            return Constants.NOTVOTE;
        }
    }

    private boolean checkCanVote(int visibleItemPosition, int touchedIconPosition) {
        String voteStatus = ((ArrayList<Point>) mPointsByDay.get(visibleItemPosition)).get(touchedIconPosition).getVoteStatus();
        if (voteStatus.equals(Constants.AGREE) || voteStatus.equals(Constants.DISAGREE)) {
            return false;
        }
        return true;
    }

    private void friendAnimate() {
        if (mFriendStatus) {
            mFriendStatus = false;
            mAddFriendImageButton.animate().rotation(0).translationX(0).translationY(0);
            mTalkFriendImageButton.animate().rotation(0).translationX(0).translationY(0).setStartDelay(500);
        } else {
            mAddFriendImageButton.clearAnimation();
            mTalkFriendImageButton.clearAnimation();

            mFriendStatus = true;
            mAddFriendImageButton.animate().rotation(720).translationX(-80).translationY(220);
            mTalkFriendImageButton.animate().rotation(720).translationX(-200).translationY(120).setStartDelay(500);
        }
    }
}
