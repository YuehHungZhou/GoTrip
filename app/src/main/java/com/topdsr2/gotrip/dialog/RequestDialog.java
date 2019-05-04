package com.topdsr2.gotrip.dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.MainContract;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Request;

public class RequestDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private Request mRequest;

    private MainContract.Presenter mMainPresenter;
    private RequestFriendAdapter mRequestFriendAdapter;
    private RequestTripAdapter mRequestTripAdapter;

    public RequestDialog() {
    }

    public void setMainPresenter(MainContract.Presenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRequestFriendAdapter = new RequestFriendAdapter(mMainPresenter);
        mRequestTripAdapter = new RequestTripAdapter(mMainPresenter);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_request, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RecyclerView friendRecyclerView = root.findViewById(R.id.recycle_friend_request);
        friendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendRecyclerView.setAdapter(mRequestFriendAdapter);

        RecyclerView tripRecyclerView = root.findViewById(R.id.recycle_trip_request);
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tripRecyclerView.setAdapter(mRequestTripAdapter);

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRequestTripAdapter.updateData(mRequest);
        mRequestFriendAdapter.updateData(mRequest);

    }

    @Override
    public void onClick(View v) {

    }

    public void setData(Request request) {
        mRequest = request;
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
