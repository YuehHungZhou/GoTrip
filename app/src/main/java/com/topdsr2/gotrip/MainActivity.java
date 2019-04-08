package com.topdsr2.gotrip;

import static com.google.common.base.Preconditions.checkNotNull;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;



public class MainActivity extends BaseActivity implements MainContract.View {

    //private BottomNavigationView mBottomNavigation;
    private BottomNavigationView mBottomNavigation;
    private MainMvpController mMainMvpController;
    private MainContract.Presenter mPresenter;
    private View mBadge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainMvpController = MainMvpController.create(this);

        setBottomNavigation();
        selectedHomePage();

    }



    private void setBottomNavigation() {
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigation.setItemIconTintList(null);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    selectedBottomNavigationViewItem(0);
                    mPresenter.openHome();
                    return true;
                case R.id.navigation_trip:
                    selectedBottomNavigationViewItem(1);
                    mPresenter.openTrip();
                    return true;
                case R.id.navigation_profile:
                    selectedBottomNavigationViewItem(2);
                    mPresenter.openProfile();
                    return true;
                default:
                    return false;
                }
            }
        };

    private void selectedHomePage() {
        ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.navigation_home);
    }

    private void selectedBottomNavigationViewItem(int itemNumber) {
        BottomNavigationMenuView menuView =
                (BottomNavigationMenuView) mBottomNavigation.getChildAt(0);

        for (int i = 0;i < 3;i++) {
            if (i == itemNumber) {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(itemNumber);
                mBadge = LayoutInflater.from(this).inflate(R.layout.badge_main_bottom, itemView, true);
            } else {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                mBadge = LayoutInflater.from(this).inflate(R.layout.badge_main_bottom_notselected, itemView, true);
            }
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void openHomeUi() {
        mMainMvpController.findOrCreateHomeView();
    }

    @Override
    public void openTripUi() {
        mMainMvpController.findOrCreateTripView();

    }

    @Override
    public void openProfileUi() {
        mMainMvpController.findOrCreateProfileView();

    }
}
