package com.topdsr2.gotrip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends BaseActivity implements MainContract.View {

    //private BottomNavigationView mBottomNavigation;
    private MainMvpController mMainMvpController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainMvpController = MainMvpController.create(this);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {

    }
}
