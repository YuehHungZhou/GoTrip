package com.topdsr2.gotrip.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.topdsr2.gotrip.MainContract;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.util.UserManager;


/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class LoginDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private Button mLoginButton;
    private TextView mTitleTextView;
    MainContract.Presenter mMainPresenter;


    public LoginDialog() {
    }

    public void setMainPresenter(MainContract.Presenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_FRAME, R.style.Dialog);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_login, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mLoginButton = view.findViewById(R.id.button_login);
        mTitleTextView = view.findViewById(R.id.text_login_title);
        mTitleTextView.setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), "veganstyle.ttf"));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLoginButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        UserManager.getInstance().loginByFacebook(getActivity(), new UserManager.LoadCallback() {

            @Override
            public void onSuccess() {
                mMainPresenter.openHome();
                mMainPresenter.openSuccessDialog();
                dismiss();
            }

            @Override
            public void onFail(String errorMessage) {

            }

            @Override
            public void onInvalidToken(String errorMessage) {

            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (!UserManager.getInstance().getLoginState()) {
            mMainPresenter.notSignin();
        }
    }
}
