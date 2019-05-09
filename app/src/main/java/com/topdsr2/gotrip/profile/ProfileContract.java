package com.topdsr2.gotrip.profile;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;
import com.topdsr2.gotrip.data.object.Request;
import com.topdsr2.gotrip.data.object.User;
import com.topdsr2.gotrip.profile.item.ProfileItemFragment;

public interface ProfileContract {

    interface View extends BaseView<Presenter> {

        void showProfileUi(User user);

        void showRequestUi(Request request);

        void checkUserData();

    }

    interface Presenter extends BasePresenter {

        void loadUserData();

        ProfileItemFragment findNewTrip();

        ProfileItemFragment findCompleteTrip();

        ProfileItemFragment findCollectionTrip();

        void openLogoutDialog();

        void openRequestDialog();

        void loadRequestData();

        Request getRequestData();

        void checkUserData();

    }
}
