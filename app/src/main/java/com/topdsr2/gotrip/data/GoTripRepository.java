package com.topdsr2.gotrip.data;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;


public class GoTripRepository implements GoTripDataSource {

    private static GoTripRepository INSTANCE = null;

    private final GoTripDataSource mGoTripRemoteDataSource;

    private final GoTripDataSource mGoTripLocalDataSource;

    private GoTripRepository(@NonNull GoTripDataSource goTripRemoteDataSource,
                             @NonNull GoTripDataSource goTripLocalDataSource) {
        mGoTripRemoteDataSource = checkNotNull(goTripRemoteDataSource);
        mGoTripLocalDataSource =  checkNotNull(goTripLocalDataSource);
    }

    public static GoTripRepository getInstance(GoTripDataSource goTripRemoteDataSource,
                                               GoTripDataSource goTripLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new GoTripRepository(goTripRemoteDataSource,goTripLocalDataSource);
        }
        return INSTANCE;
    }
}
