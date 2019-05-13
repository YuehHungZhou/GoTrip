package com.topdsr2.gotrip.addordeletepoint;

import com.topdsr2.gotrip.BasePresenter;
import com.topdsr2.gotrip.BaseView;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.TripAndPoint;

public interface AddOrDeletePointContract {

    interface View extends BaseView<Presenter> {

        void showAddOrDeleteUi(TripAndPoint bean, int today);

    }

    interface Presenter extends BasePresenter {

        void loadPointData();

        void setPointData(TripAndPoint bean, int today);

        void sendNewPoint(Point point);
    }

}
