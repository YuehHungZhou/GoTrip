package com.topdsr2.gotrip.data;

public class GoTripLocalDataSource implements GoTripDataSource {

    private static GoTripLocalDataSource INSTANCE;

    public GoTripLocalDataSource() { }

    public static GoTripLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GoTripLocalDataSource();
        }
        return INSTANCE;
    }
}
