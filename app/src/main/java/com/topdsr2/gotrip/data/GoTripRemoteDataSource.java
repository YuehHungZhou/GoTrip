package com.topdsr2.gotrip.data;

public class GoTripRemoteDataSource implements GoTripDataSource {

    private static GoTripRemoteDataSource INSTANCE;

    public GoTripRemoteDataSource() { }

    public static GoTripRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GoTripRemoteDataSource();
        }
        return INSTANCE;
    }
}
