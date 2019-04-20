package com.topdsr2.gotrip.profile;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static com.topdsr2.gotrip.MainMvpController.COLLECTIONTRIP;
import static com.topdsr2.gotrip.MainMvpController.COMPLETETRIP;
import static com.topdsr2.gotrip.MainMvpController.NEWTRIP;

public class ProfileAdapter extends FragmentPagerAdapter {

    private ProfileContract.Presenter mPresenter;
    private FragmentManager mFragmentManager;
    private String[] mItemTypes = new String[]{NEWTRIP, COMPLETETRIP, COLLECTIONTRIP};


    public ProfileAdapter(FragmentManager fragmentManager, ProfileContract.Presenter presenter) {
        super(fragmentManager);

        mFragmentManager = fragmentManager;
        mPresenter = presenter;
    }


    @Override
    public Fragment getItem(int i) {
        switch (mItemTypes[i]) {
            case NEWTRIP:
                return mPresenter.findNewTrip();
            case COMPLETETRIP:
                return mPresenter.findCompleteTrip();
            case COLLECTIONTRIP:
            default:
                return mPresenter.findCollectionTrip();
        }
    }

    @Override
    public int getCount() {
        return mItemTypes.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mItemTypes[position];
    }

    public String getItemType(int position) {
        return mItemTypes[position];
    }

}
