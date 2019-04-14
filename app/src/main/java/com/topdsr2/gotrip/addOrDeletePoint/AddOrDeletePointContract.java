package com.topdsr2.gotrip.addOrDeletePoint;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;
import com.topdsr2.gotrip.data.object.Point;

public interface AddOrDeletePointContract {

    interface View extends BaseView<Presenter> {

        void showAddOrDeleteUi();

    }

    interface Presenter extends BasePresenter {

        void loadPointData();

        void setPointData();

        void sendNewPoint(Point point);
    }

}
