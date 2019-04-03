package com.topdsr2.gotrip;


import android.support.annotation.NonNull;

import com.topdsr2.gotrip.data.GoTripRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainPresenter implements MainContract.Presenter {

    private final GoTripRepository mGoTripRepository;
    private MainContract.View mMainView;


    public MainPresenter(
            @NonNull GoTripRepository goTripRepository,
            @NonNull MainContract.View mainView) {
        mGoTripRepository = checkNotNull(goTripRepository, "goTripRepository null");
        mMainView = checkNotNull(mainView, "mainView null");
        mMainView.setPresenter(this);
    }


    @Override
    public void start() {

    }

}
