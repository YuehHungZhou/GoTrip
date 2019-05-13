package com.topdsr2.gotrip.home;

import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.SearchData;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.util.Constants;
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
    public void start() {

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
            startTime = parseTime(searchData, Constants.STARTTIME);
            endTime = parseTime(searchData, Constants.ENDTIME);
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

    private long parseTime(SearchData searchData, String type) throws ParseException {
        String str = "";

        switch (type) {
            case Constants.STARTTIME:
                str = parseStartTime(searchData, str);
                break;
            case Constants.ENDTIME:
                str = parseEndTime(searchData, str);
                break;
            default:
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_YYYY_MM);
        Date date = (Date) sdf.parse(str);

        long time = date.getTime() / 1000;
        return time;
    }


    private String parseStartTime(SearchData searchData, String str) throws ParseException {

        if (searchData.getMonth() != 0) {
            str = searchData.getYear() + "-" + searchData.getMonth();
        } else {
            str = searchData.getYear() + "-" + 1;
        }

        return str;
    }

    private String parseEndTime(SearchData searchData, String str) throws ParseException {

        if (searchData.getMonth() != 0) {
            if (searchData.getMonth() == 12) {
                str = (searchData.getYear() + 1) + "-" + 1;
            } else {
                str = searchData.getYear() + "-" + (searchData.getMonth() + 1);
            }
        } else {
            str = (searchData.getYear() + 1) + "-" + 1;
        }

        return str;
    }


}
