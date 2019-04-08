package com.topdsr2.gotrip.profile;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;

public interface ProfileContract {

    interface View extends BaseView<Presenter> {

        void showProfileUi();

    }

    interface Presenter extends BasePresenter {

        void loadProfileData();

        void setProfileData();

        void openTripMap();
    }
}
