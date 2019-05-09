package com.topdsr2.gotrip.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.MainContract;
import com.topdsr2.gotrip.R;

public class DeletePointRequstDialog extends BottomSheetDialogFragment {

    MainContract.Presenter mMainPresenter;

    private Button mButton;

    public DeletePointRequstDialog() {
    }

    public void setMainPresenter(MainContract.Presenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_delete_point_request, container, false);

        mButton = view.findViewById(R.id.button_trip_delete_point_request);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMainPresenter.deletePoint();
                showSuccess();
            }
        });
    }

    private void showSuccess() {
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

        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
