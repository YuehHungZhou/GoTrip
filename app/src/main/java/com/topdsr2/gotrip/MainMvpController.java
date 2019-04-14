package com.topdsr2.gotrip;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.topdsr2.gotrip.addOrDeletePoint.AddOrDeletePointFragment;
import com.topdsr2.gotrip.addOrDeletePoint.AddOrDeletePointPresenter;
import com.topdsr2.gotrip.data.GoTripLocalDataSource;
import com.topdsr2.gotrip.data.GoTripRemoteDataSource;
import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.home.HomeFragment;
import com.topdsr2.gotrip.home.HomePresenter;
import com.topdsr2.gotrip.profile.ProfileFragment;
import com.topdsr2.gotrip.profile.ProfilePresenter;
import com.topdsr2.gotrip.trip.TripFragment;
import com.topdsr2.gotrip.trip.TripPresenter;
import com.topdsr2.gotrip.util.ActivityUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.google.common.base.Preconditions.checkNotNull;


public class MainMvpController {

    private final FragmentActivity mActivity;
    private MainPresenter mMainPresenter;

    private HomePresenter mHomePresenter;
    private TripPresenter mTripPresenter;
    private ProfilePresenter mProfilePresenter;
    private AddOrDeletePointPresenter mAddOrDeletePointPresenter;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            HOME, TRIP, PROFILE, ADDORDELETEPOINT
    })
    public @interface FragmentType {}
    static final String HOME = "HOME";
    static final String TRIP = "TRIP";
    static final String PROFILE = "PROFILE";
    static final String ADDORDELETEPOINT = "ADDORDELETEPOINT";



    private MainMvpController(@NonNull FragmentActivity activity) {
        mActivity = activity;
    }


    static MainMvpController create(@NonNull FragmentActivity activity) {
        checkNotNull(activity);
        MainMvpController mainMvpController = new MainMvpController(activity);
        mainMvpController.createMainPresenter();
        return mainMvpController;
    }

    private MainPresenter createMainPresenter() {
        mMainPresenter = new MainPresenter(GoTripRepository.getInstance(
                GoTripRemoteDataSource.getInstance(),
                GoTripLocalDataSource.getInstance()), (MainActivity) mActivity);

        return mMainPresenter;
    }

    void findOrCreateHomeView() {

        HomeFragment homeFragment = findOrCreateHomeFragment();

        if (mHomePresenter == null) {
            mHomePresenter = new HomePresenter(GoTripRepository.getInstance(
                    GoTripRemoteDataSource.getInstance(),
                    GoTripLocalDataSource.getInstance()), homeFragment);
            mMainPresenter.setHomePresenter(mHomePresenter);
            homeFragment.setPresenter(mMainPresenter);
        }
    }

    void findOrCreateTripView() {

        TripFragment tripFragment = findOrCreateTripFragment();

        if (mTripPresenter == null) {
            mTripPresenter = new TripPresenter(GoTripRepository.getInstance(
                    GoTripRemoteDataSource.getInstance(),
                    GoTripLocalDataSource.getInstance()), tripFragment);
            mMainPresenter.setTripPresenter(mTripPresenter);
            tripFragment.setPresenter(mMainPresenter);
        }
    }

    void findOrCreateProfileView() {

        ProfileFragment profileFragment = findOrCreateProfileFragment();

        if (mProfilePresenter == null) {
            mProfilePresenter = new ProfilePresenter(GoTripRepository.getInstance(
                    GoTripRemoteDataSource.getInstance(),
                    GoTripLocalDataSource.getInstance()), profileFragment);
            mMainPresenter.setProfilePresenter(mProfilePresenter);
            profileFragment.setPresenter(mMainPresenter);
        }
    }

    void createAddOrDeletePointView(FragmentManager fragmentManager){
        AddOrDeletePointFragment pointFragment = createAddorDeletePointFragment();

        mAddOrDeletePointPresenter = new AddOrDeletePointPresenter(GoTripRepository.getInstance(
                GoTripRemoteDataSource.getInstance(),
                GoTripLocalDataSource.getInstance()), pointFragment);
        mAddOrDeletePointPresenter.setPointData();
        mMainPresenter.setAddOrDeletePointPresenter(mAddOrDeletePointPresenter);
        pointFragment.setPresenter(mMainPresenter);

        pointFragment.show(fragmentManager,"");
    }

    @NonNull
    private HomeFragment findOrCreateHomeFragment() {

        HomeFragment homeFragment =
                (HomeFragment) getFragmentManager().findFragmentByTag(HOME);
        if (homeFragment == null) {
            homeFragment = homeFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), homeFragment, HOME);

        return homeFragment;
    }

    @NonNull
    private TripFragment findOrCreateTripFragment() {

        TripFragment tripFragment =
                (TripFragment) getFragmentManager().findFragmentByTag(TRIP);
        if (tripFragment == null) {
            tripFragment = TripFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), tripFragment, TRIP);

        return tripFragment;
    }

    @NonNull
    private ProfileFragment findOrCreateProfileFragment() {

        ProfileFragment profileFragment =
                (ProfileFragment) getFragmentManager().findFragmentByTag(PROFILE);
        if (profileFragment == null) {
            profileFragment = ProfileFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), profileFragment, PROFILE);

        return profileFragment;
    }

    @NonNull
    private AddOrDeletePointFragment createAddorDeletePointFragment() {

        AddOrDeletePointFragment addOrDeletePointFragment = AddOrDeletePointFragment.newInstance();

//        ActivityUtils.addFragmentByTag(
//                getFragmentManager(), addOrDeletePointFragment, ADDORDELETEPOINT);

        return addOrDeletePointFragment;
    }



    private FragmentManager getFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }


}
