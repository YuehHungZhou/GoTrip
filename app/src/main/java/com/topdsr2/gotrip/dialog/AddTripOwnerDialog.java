package com.topdsr2.gotrip.dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.MainContract;
import com.topdsr2.gotrip.R;

public class AddTripOwnerDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    MainContract.Presenter mMainPresenter;

    private Button mButton;
    private EditText mEditText;

    public AddTripOwnerDialog() {
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_tirp_owner, container, false);

        mButton = view.findViewById(R.id.button_add_trip_owner);
        mEditText = view.findViewById(R.id.editText_add_trip_owner);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEditText.getText().toString().trim();
                mMainPresenter.addTripOwner(email);
            }
        });
    }



    @Override
    public void onClick(View v) {

    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
