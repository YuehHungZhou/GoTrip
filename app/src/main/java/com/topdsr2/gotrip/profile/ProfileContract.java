package com.topdsr2.gotrip.profile;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;
import com.topdsr2.gotrip.profile.item.ProfileItemFragment;

public interface ProfileContract {

    interface View extends BaseView<Presenter> {

        void showProfileUi();

    }

    interface Presenter extends BasePresenter {

        ProfileItemFragment findNewTrip();

        ProfileItemFragment findCompleteTrip();

        ProfileItemFragment findCollectionTrip();
    }
}
