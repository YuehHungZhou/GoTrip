package com.topdsr2.gotrip.home;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.SearchData;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.data.object.User;
import com.topdsr2.gotrip.dialog.LoginDialog;
import com.topdsr2.gotrip.util.FireBaseManager;
import com.topdsr2.gotrip.util.UserManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

public class HomePresenter implements HomeContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private final HomeContract.View mHomeView;

    public HomePresenter(
            @NonNull GoTripRepository goTripRepository,
            @NonNull HomeContract.View homeView) {
        mGoTripRepository = checkNotNull(goTripRepository, "GoTripRepository null");
        mHomeView = checkNotNull(homeView, "HomeView null");
        mHomeView.setPresenter(this);
    }

    @Override
    public void loadHomeData() {
        FireBaseManager.getInstance().getAllTypeTrip(new FireBaseManager.GetAllTripCallback() {
            @Override
            public void onCompleted(ArrayList<Trip> trips) {
                setHomeData(trips);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void setHomeData(ArrayList<Trip> trips) {
        mHomeView.showHomeUi(trips);

    }

    @Override
    public void openTrip() {

    }

    @Override
    public void hideBtmNavi() {

    }

    @Override
    public void loadTripData(String tripId) {

    }

    @Override
    public void openHomeFilterDialog() {

    }

    @Override
    public void searchData(SearchData searchData) {

        long startTime = 0;
        long endTime = 0;

        try {
            startTime = paseStartTime(searchData);
            endTime = paseEndTime(searchData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FireBaseManager.getInstance().getSearchTrip(searchData, startTime, endTime,
                new FireBaseManager.GetAllTripCallback() {
                    @Override
                    public void onCompleted(ArrayList<Trip> trips) {
                        mHomeView.showHomeUi(trips);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
    }

    @Override
    public void loadUserTripCollection() {
        mHomeView.showUserTripCollection(UserManager.getInstance().getUser().getTripCollection());
    }

    @Override
    public void changeCollection(ArrayList<String> tripCollection) {
        UserManager.getInstance().getUser().setTripCollection(tripCollection);
    }


    private long paseStartTime(SearchData searchData) throws ParseException {
        String str = "";
        if (searchData.getMonth() != 0) {
            str = searchData.getYear() + "-" + searchData.getMonth();
        } else {
            str = searchData.getYear() + "-" + 1;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = (Date) sdf.parse(str);

        long time = date.getTime() / 1000;
        return  time;
    }

    private long paseEndTime(SearchData searchData) throws ParseException {
        String str = "";

        if (searchData.getMonth() != 0) {
            if (searchData.getMonth() == 12) {
                str = (searchData.getYear() + 1) + "-" + 1;
            } else {
                str = searchData.getYear() + "-" + (searchData.getMonth() + 1);
            }
        } else {
            str = (searchData.getYear() + 1) + "-" + 1;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = (Date) sdf.parse(str);

        long time = date.getTime() / 1000;
        return  time;
    }

    @Override
    public void start() {

    }
}
