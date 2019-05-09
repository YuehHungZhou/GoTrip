package com.topdsr2.gotrip;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.internal.CallbackManagerImpl;
import com.topdsr2.gotrip.data.object.Request;
import com.topdsr2.gotrip.dialog.AddPointRequstDialog;
import com.topdsr2.gotrip.dialog.AddTripDialog;
import com.topdsr2.gotrip.dialog.AddTripOwnerDialog;
import com.topdsr2.gotrip.dialog.DeletePointRequstDialog;
import com.topdsr2.gotrip.dialog.HomeFilterDialog;
import com.topdsr2.gotrip.dialog.LeaveDialog;
import com.topdsr2.gotrip.dialog.LoginDialog;
import com.topdsr2.gotrip.dialog.LogoutDialog;
import com.topdsr2.gotrip.dialog.RequestDialog;
import com.topdsr2.gotrip.profile.item.ProfileItemFragment;
import com.topdsr2.gotrip.util.UserManager;

import io.fabric.sdk.android.Fabric;

import static com.google.common.base.Preconditions.checkNotNull;


public class MainActivity extends BaseActivity implements MainContract.View {

    private BottomNavigationView mBottomNavigation;
    private boolean isBtmNaviVisibale = true;
    private MainMvpController mMainMvpController;
    private MainContract.Presenter mPresenter;
    private View mBadge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);
        mMainMvpController = MainMvpController.create(this);
        setBottomNavigation();
        selectedHomePage();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            UserManager.getInstance().getFbCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
    }

    private Activity getActivity() {
        return this;
    }

    private void setBottomNavigation() {
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mBottomNavigation.setOnNavigationItemSelectedListener(onNaviItemSelectedListener);
        mBottomNavigation.setItemIconTintList(null);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNaviItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                            mPresenter.checkLogIn(getActivity());
                            selectedBtmNavitItem(0);
                            return true;
                        case R.id.navigation_trip:
                            selectedBtmNavitItem(1);
                            mPresenter.checkOnTrip();
                            return true;
                        case R.id.navigation_profile:
                            selectedBtmNavitItem(2);
                            mPresenter.openProfile();
                            mPresenter.checkUserData();
                            mPresenter.loadRequestData();
                            return true;
                        default:
                            return false;
                    }
                }
            };

    @Override
    public void selectedHomePage() {
        ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.navigation_home);
    }

    private void selectedBtmNavitItem(int itemNumber) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigation.getChildAt(0);

        for (int i = 0; i < 3; i++) {
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
    public void hideBtmNaviUi() {
        mBottomNavigation.setVisibility(View.GONE);
        isBtmNaviVisibale = false;
    }

    @Override
    public void showBtmNaviUi() {
        mBottomNavigation.setVisibility(View.VISIBLE);
        isBtmNaviVisibale = true;
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

    @Override
    public void openLoginUi() {
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.setMainPresenter(mPresenter);
        loginDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void openAddTripDialogUi() {
        AddTripDialog addTripDialog = new AddTripDialog();
        addTripDialog.setMainPresenter(mPresenter);
        addTripDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void openLogoutUi() {
        LogoutDialog logoutDialog = new LogoutDialog();
        logoutDialog.setMainPresenter(mPresenter);
        logoutDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void openRequestUi() {
        RequestDialog requestDialog = new RequestDialog();
        requestDialog.setMainPresenter(mPresenter);
        requestDialog.setData(mPresenter.getRequestData());
        requestDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void openExitUi() {
        LeaveDialog leaveDialog = new LeaveDialog();
        leaveDialog.setMainPresenter(mPresenter);
        leaveDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void openHomeFilterUi() {
        HomeFilterDialog filterDialog = new HomeFilterDialog();
        filterDialog.setMainPresenter(mPresenter);
        filterDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void openAddPointRequestUi() {
        AddPointRequstDialog addPointRequstDialog = new AddPointRequstDialog();
        addPointRequstDialog.setMainPresenter(mPresenter);
        addPointRequstDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void openDeletePointRequestUi() {
        DeletePointRequstDialog deletePointRequstDialog = new DeletePointRequstDialog();
        deletePointRequstDialog.setMainPresenter(mPresenter);
        deletePointRequstDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void openAddTripOwnerUi() {
        AddTripOwnerDialog addTripOwnerDialog = new AddTripOwnerDialog();
        addTripOwnerDialog.setMainPresenter(mPresenter);
        addTripOwnerDialog.show(getSupportFragmentManager(), "");
        mPresenter.setAddTripOwnerDialog(addTripOwnerDialog);
    }

    @Override
    public void openSuccessUi() {
        Dialog successDialog = new Dialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_success, null);
        successDialog.setContentView(view);
        successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        successDialog.setCanceledOnTouchOutside(false);
        successDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                successDialog.dismiss();
            }
        }, 2000);
    }

    @Override
    public void openAddOrDeletePointUi() {
        mMainMvpController.createAddOrDeletePointView(getSupportFragmentManager());
    }

    @Override
    public ProfileItemFragment findNewTripView() {
        return mMainMvpController.findOrCreateNewTripView();
    }

    @Override
    public ProfileItemFragment findCompleteTripView() {
        return mMainMvpController.findOrCreateCompleteTripView();

    }

    @Override
    public ProfileItemFragment findCollectionTripView() {
        return mMainMvpController.findOrCreateCollectionTripView();

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.saveCollection();
    }

    @Override
    public void pressBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {

        if (isBtmNaviVisibale) {
            super.onBackPressed();
        } else {
            mPresenter.detachTripListener();
            showBtmNaviUi();
            selectedHomePage();
        }
    }
}
