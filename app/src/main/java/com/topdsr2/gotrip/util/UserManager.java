package com.topdsr2.gotrip.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.topdsr2.gotrip.data.GoTripLocalDataSource;
import com.topdsr2.gotrip.data.GoTripRemoteDataSource;
import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class UserManager {

    private final GoTripRepository mGoTripRepository;
    private User mUser;
    private CallbackManager mCallbackManager;


    private static class UserManagerHolder {
        private static final UserManager INSTANCE = new UserManager();
    }

    private UserManager() {
        mGoTripRepository = GoTripRepository.getInstance(
                GoTripRemoteDataSource.getInstance(),
                GoTripLocalDataSource.getInstance());
    }

    public static UserManager getInstance() {
        return UserManagerHolder.INSTANCE;
    }


    public void loginByFacebook(Context context, final LoadCallback loadCallback) {

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest graphRequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    if (response.getConnection().getResponseCode() == 200) {

                                        String email = object.getString("email");
                                        String name = object.getString("name");
                                        Profile profile = Profile.getCurrentProfile();
                                        Uri userPhoto = profile.getProfilePictureUri(300, 300);

                                        addUserDataToifrebase(email, name, userPhoto.toString(),loadCallback);



                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        loginFacebook(context);

    }

    private void loginFacebook(Context context) {

        LoginManager.getInstance().logInWithReadPermissions(
                (Activity) context, Arrays.asList("email"));
    }

    private void addUserDataToifrebase(String email, String name, String photoUri, LoadCallback loadCallback) {
        FireBaseManager.getInstance().addUserData(email, name, photoUri, new FireBaseManager.GetUserDataCallback() {


            @Override
            public void onCompleted(User user) {
                mUser = user;
                loadCallback.onSuccess();

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public CallbackManager getFbCallbackManager() {
        return mCallbackManager;
    }

    public interface LoadCallback {

        void onSuccess();

        void onFail(String errorMessage);

        void onInvalidToken(String errorMessage);
    }


}
