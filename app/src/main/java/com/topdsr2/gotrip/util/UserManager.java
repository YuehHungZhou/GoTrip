package com.topdsr2.gotrip.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;


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
                                        String userPhoto = "https://graph.facebook.com/" + object.getLong("id") + "/picture?type=large";
//                                        Profile profile = Profile.getCurrentProfile();
//                                        Uri userPhoto = profile.getProfilePictureUri(300, 300);
                                        writeInternal(email, (Activity) context);
                                        addUserDataToifrebase(email, name, userPhoto, loadCallback);
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

    public void readInternal(Activity activity, LoadCallback callback) {
        FireBaseManager.getInstance().loadIntenalData(read(activity), new FireBaseManager.GetUserDataCallback() {
            @Override
            public void onCompleted(User user) {
                mUser = user;
                callback.onSuccess();
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public void writeInternal(String email, Activity activity) {
        write(email, activity);
    }

    private String read(Activity activity) {
        FileInputStream inputStream = null;
        String eamil = "";
        try {
            inputStream = activity.openFileInput("GoTrip.txt");

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            eamil = reader.readLine();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        return eamil;
    }

    private void write(String email, Activity activity) {
        FileOutputStream outputStream = null;
        try {
            outputStream = activity.openFileOutput("GoTrip.txt", Context.MODE_PRIVATE);
            outputStream.write(email.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean fileExist(Activity activity) {
        File file = activity.getBaseContext().getFileStreamPath("GoTrip.txt");
        return file.exists();
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
