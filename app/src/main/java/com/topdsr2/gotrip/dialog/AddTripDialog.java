package com.topdsr2.gotrip.dialog;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.topdsr2.gotrip.MainContract;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.util.Constants;
import com.topdsr2.gotrip.util.UserManager;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTripDialog extends AppCompatDialogFragment implements View.OnClickListener {

    MainContract.Presenter mMainPresenter;

    private TextView mTitleText;
    private TextView mDescribeText;
    private TextView mCountryText;
    private Button mStartTimeButton;
    private Button mEndTimeButton;
    private Button mSendButton;
    private EditText mTitleEditText;
    private EditText mDescribeEditText;
    private Spinner mSpinner;
    private long mStartTime;
    private long mEndTime;
    private int mTrevalDay;

    public AddTripDialog() {
    }

    public void setMainPresenter(MainContract.Presenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_trip, container, false);

        mTitleText = view.findViewById(R.id.text_add_trip_title);
        mDescribeText = view.findViewById(R.id.text_add_trip_describe);
        mCountryText = view.findViewById(R.id.text_add_trip_country);
        mStartTimeButton = view.findViewById(R.id.button_add_trip_starttime);
        mEndTimeButton = view.findViewById(R.id.button_add_trip_endtime);
        mSendButton = view.findViewById(R.id.button_add_trip_send);
        mTitleEditText = view.findViewById(R.id.editText_add_trip_title);
        mDescribeEditText = view.findViewById(R.id.editText_add_trip_describe);
        mSpinner = view.findViewById(R.id.spinner_add_trip);

        mStartTimeButton.setOnClickListener(this);
        mEndTimeButton.setOnClickListener(this);
        mSendButton.setOnClickListener(this);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_trip_starttime:
                openCalendar(v, Constants.STARTTIME);
                break;

            case R.id.button_add_trip_endtime:
                openCalendar(v, Constants.ENDTIME);
                break;

            case R.id.button_add_trip_send:
                mTrevalDay = ((int)((mEndTime - mStartTime) / (60 * 60 * 24))) + 1;

                Trip trip = new Trip();



                trip.setTripStart(mStartTime);
                trip.setTripEnd(mEndTime);
                trip.setTripDay(mTrevalDay);
                trip.setTitle(mTitleEditText.getText().toString());
                trip.setDescribe(mDescribeEditText.getText().toString());
                trip.setCountry(mSpinner.getSelectedItem().toString());
                trip.setCreater(UserManager.getInstance().getUser().getEmail());
                trip.setCreaterImage(UserManager.getInstance().getUser().getUserImage());

                mMainPresenter.addNewTrip(trip);

                dismiss();
                break;
            default:
                break;

        }
    }

    private void openCalendar(View v, String timeFrom) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String data = String.valueOf(year) + "-"
                                + String.valueOf(month + 1) + "-"
                                + String.valueOf(dayOfMonth);
                calendar.clear();
                calendar.set(year, month, dayOfMonth);

                if (timeFrom.equals(Constants.STARTTIME)) {
                    mStartTime = calendar.getTimeInMillis() / 1000;
                    mStartTimeButton.setText(data);

                } else {
                    mEndTime = calendar.getTimeInMillis() / 1000;
                    mEndTimeButton.setText(data);
                }

            }
        }, year, month, day).show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
