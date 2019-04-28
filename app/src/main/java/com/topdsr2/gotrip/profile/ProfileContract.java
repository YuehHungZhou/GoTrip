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

        void checkUser();

    }

    interface Presenter extends BasePresenter {

        void loadUserData();

        ProfileItemFragment findNewTrip();

        ProfileItemFragment findCompleteTrip();

        ProfileItemFragment findCollectionTrip();

        void openLogoutView();

        void openRequestView();

        void setRequestData(Request request);

        Request getRequestData();

        void checkUser();

    }
}
