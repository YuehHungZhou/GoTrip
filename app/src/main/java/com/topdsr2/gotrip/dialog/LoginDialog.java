package com.topdsr2.gotrip.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
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
        mLoginButton = view.findViewById(R.id.button_login);
        mTitleTextView = view.findViewById(R.id.text_login);
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
                showSuccess();
            }


            @Override
            public void onFail(String errorMessage) {

            }

            @Override
            public void onInvalidToken(String errorMessage) {

            }
        });
    }


    private void showSuccess() {
        Dialog signinDialog = new Dialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_success, null);
        signinDialog.setContentView(view);
        signinDialog.setCanceledOnTouchOutside(false);
        signinDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                signinDialog.dismiss();
            }
        }, 2000);
        dismiss();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = (accessToken != null);
        if (!isLoggedIn) {
            mMainPresenter.notSignin();
        }
    }
}
