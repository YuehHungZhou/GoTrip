package com.topdsr2.gotrip;

import android.app.Application;
import android.content.Context;

public class GoTrip extends Application {

    private static Context mContext;

    public GoTrip() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getmContext() {
        return mContext;
    }
}
