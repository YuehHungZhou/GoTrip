package com.topdsr2.gotrip.dialog;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.MainContract;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.util.Constants;
import com.topdsr2.gotrip.util.UserManager;

import java.util.Calendar;

public class LogoutDialog extends BottomSheetDialogFragment  {

    MainContract.Presenter mMainPresenter;

    private Button mButton;

    public LogoutDialog() {
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
        View view = inflater.inflate(R.layout.dialog_logout, container, false);

        mButton = view.findViewById(R.id.button_logout);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookSdk.sdkInitialize(GoTrip.getContext());
                LoginManager.getInstance().logOut();

                mMainPresenter.logout();

                dismiss();
            }
        });
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
