package com.topdsr2.gotrip.trip;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class TripFragment extends Fragment implements TripContract.View, View.OnClickListener,
        PlaceSelectionListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener,
        GoogleMap.OnCameraMoveStartedListener {

    private TripContract.Presenter mPresenter;
    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mMap;

    private AutocompleteSupportFragment mAutocompleteSupportFragment;
    private TripContentAdapter mTripContentAdapter;
    private TripContentItemAdapter mTripContentItemAdapter;

    private TripAndPoint mBean;
    private ArrayList<Object> mPointsByDay;
    private ArrayList<Point> mPointsHolder;
    private ArrayList<Point> mReadyPoints;
    private int mVisibleItemPosition;
    private int mTouchedIconPosition;
    private boolean isOwner;
    private int mTripDay = 0;
    private boolean mFriendStatus;
    private Marker mMarker;
    private Marker mSelectedMarker;
    private ArrayList<LatLng> mLatLngs = new ArrayList<LatLng>();

    private ImageView mQuestionImage;
    private ConstraintLayout mLayoutSearch;
    private ConstraintLayout mLayoutVote;
    private TextView mVotedAgreeText;
    private TextView mVotedDisagreeText;
    private TextView mVoteTitleText;
    private Button mVoteAgreeButton;
    private Button mVoteDisagreeButton;
    private ImageButton mFriendImageButton;
    private ImageButton mAddFriendImageButton;
    private ImageButton mTalkFriendImageButton;
    private ImageButton mExitImageButton;
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
        mVisibleItemPosition = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trip, container, false);

        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mAutocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager()
                .findFragmentById(R.id.place_autocomplete);
        mAutocompleteSupportFragment.a.setHint(getString(R.string.trip_search_hint));
        mAutocompleteSupportFragment.a.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getContext().getResources().getDimension(R.dimen.text));

        mInfoRecyclerView = root.findViewById(R.id.recycle_trip_content);
        mInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
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
                    setMarker(((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition)));
                }
            }
        });
        mInfoRecyclerView.setAdapter(mTripContentAdapter);

        mIconRecyclerView = root.findViewById(R.id.recycler_trip_content_icon);
        mIconRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mIconRecyclerView.setAdapter(mTripContentItemAdapter);

        mQuestionImage = root.findViewById(R.id.image_reload_point);
        mLayoutSearch = root.findViewById(R.id.constraint_trip_search);
        mLayoutVote = root.findViewById(R.id.constraint_vote);
        mVotedAgreeText = root.findViewById(R.id.text_trip_vote_agree);
        mVotedDisagreeText = root.findViewById(R.id.text_trip_vote_disagree);
        mVoteTitleText = root.findViewById(R.id.text_trip_vote_title);
        mVoteAgreeButton = root.findViewById(R.id.button_trip_agree);
        mVoteDisagreeButton = root.findViewById(R.id.button_trip_disagree);

        mFriendImageButton = root.findViewById(R.id.imageButton_trip_friend);
        mAddFriendImageButton = root.findViewById(R.id.imageButton_add_friend);
        mTalkFriendImageButton = root.findViewById(R.id.imageButton_talk_friend);
        mExitImageButton = root.findViewById(R.id.imageButton_exit);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAutocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.PHOTO_METADATAS));
        mAutocompleteSupportFragment.setOnPlaceSelectedListener(this);

        mSupportMapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setTrafficEnabled(false);
            mMap.clear();

            mMap.setOnMapClickListener(this);
            mMap.setOnMarkerClickListener(this);
            mMap.setOnCameraMoveStartedListener(this);
        });

        mQuestionImage.setOnClickListener(this);
        mFriendImageButton.setOnClickListener(this);
        mAddFriendImageButton.setOnClickListener(this);
        mTalkFriendImageButton.setOnClickListener(this);
        mExitImageButton.setOnClickListener(this);
        mVoteAgreeButton.setOnClickListener(this);
        mVoteDisagreeButton.setOnClickListener(this);
    }

    @Override
    public void showTripUi(TripAndPoint bean) {

        mPresenter.checkDifferentTrip(bean);
        mBean = bean;
        mPresenter.checkIsOwner();

        parsePointData();

        mTripContentAdapter.updateData(mPointsByDay, mPointsHolder, mTripDay);
        mTripContentItemAdapter.updateData(mPointsByDay, mReadyPoints);

        if (mMap != null) {

            setMarker(((ArrayList<Point>) mPointsByDay.get(0)));

            if (mVisibleItemPosition > 0) {
                mTripContentAdapter.scrollChangeIconInfo(mVisibleItemPosition, mPointsHolder);
                mTripContentItemAdapter.readyChangeIcon(mVisibleItemPosition);
                setMarker(((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition)));
            } else {
                mInfoRecyclerView.smoothScrollToPosition(0);
            }

            if (((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition)).size() != 0) {
                moveCamera();
            }
        }
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
    public int getTouchedIconPosition() {
        return mTouchedIconPosition;
    }


    @Override
    public Point addPointData(Point point) {

        point.setLatitude(mSelectedMarker.getPosition().latitude);
        point.setLongitude(mSelectedMarker.getPosition().longitude);
        point.setSorte(sorte(point.getArrivalTime()));

        return point;
    }

    @Override
    public void closeFunction(boolean ownerStatus) {
        isOwner = ownerStatus;
        mQuestionImage.setVisibility(View.INVISIBLE);
        mFriendImageButton.setVisibility(View.INVISIBLE);
        mAddFriendImageButton.setVisibility(View.INVISIBLE);
        mTalkFriendImageButton.setVisibility(View.INVISIBLE);
        mExitImageButton.setVisibility(View.INVISIBLE);
        mLayoutSearch.setVisibility(View.INVISIBLE);

    }

    @Override
    public void openFunction(boolean ownerStatus) {
        isOwner = ownerStatus;
        mQuestionImage.setVisibility(View.VISIBLE);
        mFriendImageButton.setVisibility(View.VISIBLE);
        mAddFriendImageButton.setVisibility(View.VISIBLE);
        mTalkFriendImageButton.setVisibility(View.VISIBLE);
        mExitImageButton.setVisibility(View.VISIBLE);
        mLayoutSearch.setVisibility(View.VISIBLE);

    }

    @Override
    public void showPointDeleteView(int position) {
        mTouchedIconPosition = position;
        if (isOwner) {
            mPresenter.openDeletePointRequestDialog();
        }
    }

    @Override
    public void showVoteViewUi(int position) {
        mTouchedIconPosition = position;
        if (isOwner) {
            if (checkCanVote()) {
                setVoteNumber();
                mLayoutVote.setVisibility(View.VISIBLE);

                switch (checkIsVoted(mVisibleItemPosition, mTouchedIconPosition)) {
                    case Constants.AGREE:
                        mVoteTitleText.setText(getString(R.string.trip_vote_isagree));
                        mVoteDisagreeButton.setVisibility(View.INVISIBLE);
                        mVoteAgreeButton.setVisibility(View.VISIBLE);
                        mVoteAgreeButton.setClickable(false);
                        voteAgreeAnimation();
                        break;
                    case Constants.DISAGREE:
                        mVoteTitleText.setText(getString(R.string.trip_vote_isdisagree));
                        mVoteAgreeButton.setVisibility(View.INVISIBLE);
                        mVoteDisagreeButton.setVisibility(View.VISIBLE);
                        mVoteDisagreeButton.setClickable(false);
                        voteDisagreeAnimation();
                        break;
                    case Constants.NOTVOTE:
                        voteBackAnimation();
                        mVoteTitleText.setText(getString(R.string.trip_vote));
                        mVoteAgreeButton.setVisibility(View.VISIBLE);
                        mVoteDisagreeButton.setVisibility(View.VISIBLE);
                        mVoteAgreeButton.setClickable(true);
                        mVoteDisagreeButton.setClickable(true);
                        break;
                    default:
                        break;
                }
            } else {
                mLayoutVote.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void showToast() {
        Toast.makeText(getContext(), getString(R.string.delete_trip_owner), Toast.LENGTH_LONG).show();
    }

    @Override
    public TripAndPoint getOriginTrip() {
        return mBean;
    }

    @Override
    public void resetContent() {
        mVisibleItemPosition = 0;
    }

    @Override
    public void changeIconInfoUi(int posotion) {
        mTripContentAdapter.changeSelectedIconInfo(mVisibleItemPosition, posotion);
    }

    @Override
    public void onPlaceSelected(@NonNull Place place) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 20));
        mMap.addMarker(new MarkerOptions().position(place.getLatLng())
                .title(getString(R.string.trip_info_name, place.getName())
                        + getString(R.string.trip_info_address, place.getAddress()))).showInfoWindow();
    }

    @Override
    public void onError(@NonNull Status status) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_reload_point:
                break;
            case R.id.imageButton_trip_friend:
                friendAnimate();
                break;
            case R.id.imageButton_add_friend:
                friendAnimate();
                mPresenter.openAddTripOwnerDialog();
                break;
            case R.id.imageButton_talk_friend:
                friendAnimate();
                Toast.makeText(getContext(),
                        getString(R.string.toast_trip_chatroom), Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton_exit:
                friendAnimate();
                mPresenter.openExitDialog();
                break;
            case R.id.button_trip_agree:
                mPresenter.vote(((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition))
                                .get(mTouchedIconPosition).getId(), Constants.AGREE);
                break;
            case R.id.button_trip_disagree:
                mPresenter.vote(((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition))
                                .get(mTouchedIconPosition).getId(), Constants.DISAGREE);
                break;
            default:
                break;

        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

        mLayoutVote.setVisibility(View.INVISIBLE);

        if (mMarker != null) {
            mMarker.remove();
        }

        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.trip_info_title)));

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

        mLayoutVote.setVisibility(View.INVISIBLE);

        if (mMarker != null) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 20));

        if (isOwner) {
            mPresenter.openAddPointRequestDialog();
        }

        mSelectedMarker = marker;

        return true;
    }

    private void setMarker(ArrayList<Point> points) {

        if (points.size() != 0) {
            mMap.clear();
            mLatLngs.clear();

            for (int i = 0; i < points.size(); i++) {
                LatLng latLng = new LatLng(points.get(i).getLatitude(), points.get(i).getLongitude());
                mMap.addMarker(setMarkerIcon(points.get(i), latLng, 100, 100));
                mLatLngs.add(latLng);
            }
            setPolyLine(points);
        } else {
            mMap.clear();
        }

    }

    private MarkerOptions setMarkerIcon(Point point, LatLng latLng, int height, int width) {

        BitmapDrawable bitmapdraw;

        if (point.getVoteStatus().equals(Constants.AGREE)) {
            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.red_placeholder);
        } else if (point.getVoteStatus().equals(Constants.DISAGREE)) {
            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.grad_placeholder);
        } else {
            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.white_placeholder);
        }

        Bitmap marker = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), width, height, false);
        MarkerOptions markerOptions =
                new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(marker));
        return markerOptions;
    }

    private void setPolyLine(ArrayList<Point> points) {

        if (mLatLngs.size() > 1) {
            for (int i = 0; i < mLatLngs.size() - 1; i++) {
                if (points.get(i).getVoteStatus().equals(Constants.AGREE)
                        && points.get(i + 1).getVoteStatus().equals(Constants.AGREE)) {
                    LatLng[] latLngs = new LatLng[]{mLatLngs.get(i), mLatLngs.get(i + 1)};
                    PolylineOptions agreePolyline = new PolylineOptions().add(latLngs)
                            .color(GoTrip.getContext().getColor(R.color.polyline));
                    Polyline polyline = mMap.addPolyline(agreePolyline);
                    polyline.setWidth(10);

                } else {
                    LatLng[] latLngs = new LatLng[]{mLatLngs.get(i), mLatLngs.get(i + 1)};
                    PolylineOptions dashPolyline = new PolylineOptions().add(latLngs).pattern(setDash())
                            .color(GoTrip.getContext().getColor(R.color.polyline_dash));
                    Polyline polyline = mMap.addPolyline(dashPolyline);
                    polyline.setWidth(10);
                }
            }
        }
    }

    @Override
    public void onCameraMoveStarted(int i) {
        mLayoutVote.setVisibility(View.INVISIBLE);
    }

    private void moveCamera() {
        CameraPosition googlePlex = CameraPosition.builder()
                .target(mLatLngs.get(0))
                .zoom(10)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 2000, null);
    }

    @Override
    public void moveCameraToMarker(Double latitude, Double longitude) {
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(latitude, longitude))
                .zoom(14)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);
    }

    private List<PatternItem> setDash() {
        Dash dash = new Dash(50);
        Gap gap = new Gap(20);
        List<PatternItem> patternDashed = Arrays.asList(dash, gap);
        return patternDashed;
    }

    private void placeInitialized() {
        if (!Places.isInitialized()) {
            Places.initialize(GoTrip.getContext(), getString(R.string.google_map_key));
            PlacesClient placesClient = Places.createClient(getContext());
        }
    }

    private void parsePointData() {

        mPointsByDay = new ArrayList<>();
        mPointsHolder = new ArrayList<>();
        mReadyPoints = new ArrayList<>();
        mTripDay = mBean.getTrip().getTripDay();

        for (int i = 1; i <= mTripDay; i++) {
            ArrayList<Point> points = new ArrayList<>();

            if (mBean.getPoints().size() != 0) {
                for (int j = 0; j < mBean.getPoints().size(); j++) {
                    if (mBean.getPoints().get(j).getDay() == i) {

                        points.add(mBean.getPoints().get(j));
                    }
                }
            }
            mPointsByDay.add(mPresenter.sortPoint(points));
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
        if (((ArrayList<Point>) mPointsByDay.get(visibleItemPosition))
                .get(touchedIconPosition).getAgree().contains(email.trim())) {
            return Constants.AGREE;
        } else if (((ArrayList<Point>) mPointsByDay.get(visibleItemPosition))
                .get(touchedIconPosition).getDisagree().contains(email.trim())) {
            return Constants.DISAGREE;
        } else {
            return Constants.NOTVOTE;
        }
    }

    private boolean checkCanVote() {
        String voteStatus = ((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition))
                .get(mTouchedIconPosition).getVoteStatus();
        if (voteStatus.equals(Constants.AGREE) || voteStatus.equals(Constants.DISAGREE)) {
            return false;
        }
        return true;
    }

    private void setVoteNumber() {
        int agree = ((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition))
                .get(mTouchedIconPosition).getAgree().size();
        int disagree = ((ArrayList<Point>) mPointsByDay.get(mVisibleItemPosition))
                .get(mTouchedIconPosition).getDisagree().size();

        mVotedAgreeText.setText(getString(R.string.trip_vote_agree_title, agree));
        mVotedDisagreeText.setText(getString(R.string.trip_vote_disagree_title, disagree));
    }

    private void friendAnimate() {
        if (mFriendStatus) {
            mFriendStatus = false;
            mAddFriendImageButton.animate().rotation(0).translationX(0).translationY(0);
            mTalkFriendImageButton.animate().rotation(0).translationX(0).translationY(0).setStartDelay(100);
            mExitImageButton.animate().rotation(0).translationX(0).translationY(0).setStartDelay(200);

        } else {
            mAddFriendImageButton.clearAnimation();
            mTalkFriendImageButton.clearAnimation();
            mFriendStatus = true;

            mAddFriendImageButton.animate().rotation(720).translationX(0).translationY(-250);
            mTalkFriendImageButton.animate().rotation(720).translationX(-150).translationY(-150).setStartDelay(100);
            mExitImageButton.animate().rotation(720).translationX(-250).translationY(-0).setStartDelay(200);

        }
    }

    private void voteAgreeAnimation() {
        mVoteAgreeButton.clearAnimation();
        mVoteDisagreeButton.animate().translationX(0).translationY(0);
        mVoteAgreeButton.animate().translationX(-170).translationY(0);
    }

    private void voteDisagreeAnimation() {
        mVoteDisagreeButton.clearAnimation();
        mVoteAgreeButton.animate().translationX(0).translationY(0);
        mVoteDisagreeButton.animate().translationX(170).translationY(0);
    }

    private void voteBackAnimation() {
        mVoteAgreeButton.animate().translationX(0).translationY(0);
        mVoteDisagreeButton.animate().translationX(0).translationY(0);
    }
}
