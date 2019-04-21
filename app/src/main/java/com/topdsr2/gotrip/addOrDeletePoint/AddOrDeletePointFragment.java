package com.topdsr2.gotrip.addOrDeletePoint;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.TripAndPoint;

import java.util.ArrayList;
import java.util.Calendar;

public class AddOrDeletePointFragment extends BottomSheetDialogFragment implements AddOrDeletePointContract.View {

    private AddOrDeletePointContract.Presenter mPresenter;

    private TripAndPoint mBean;
    private int mToday;
    private long mTime;

    private EditText mPlaceEditText;
    private EditText mNoteEditText;
    private EditText mCostEditText;
    private Spinner mTypeSpinner;
    private Button mTimeButton;
    private Button mCheckButton;

    public AddOrDeletePointFragment() {
    }

    public static AddOrDeletePointFragment newInstance() {
        return new AddOrDeletePointFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.loadPointData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addordeletepoint, container, false);

        mPlaceEditText = root.findViewById(R.id.edit_point_place);
        mNoteEditText = root.findViewById(R.id.edit_point_note);
        mCostEditText = root.findViewById(R.id.edit_point_cost);
        mTypeSpinner = root.findViewById(R.id.spinner_point_type);
        mTimeButton = root.findViewById(R.id.button_point_time);
        mCheckButton = root.findViewById(R.id.button_point_check);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTimeInMillis(mBean.getTrip().getTripStart() * 1000);

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mTime = ((long) hourOfDay * 60 * 60) + ((long)minute * 60)
                                + mBean.getTrip().getTripStart()
                                + ((long) (mToday - 1) * 60 * 60 * 24);

                        mTimeButton.setText(hourOfDay + ":" + minute);

                    }
                }, hour, minute, false).show();


            }
        });

        mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
                showSuccess();
                dismiss();
            }
        });
    }

    @Override
    public void showAddOrDeleteUi(TripAndPoint bean, int today) {
        mBean = bean;
        mToday = today;
    }

    @Override
    public void setPresenter(AddOrDeletePointContract.Presenter presenter) {
        mPresenter = presenter;
    }


    private void sendData() {

        Point point = new Point();
        ArrayList<String> images = new ArrayList<>();

        point.setCost(Integer.parseInt(mCostEditText.getText().toString()));
        point.setTitle(mPlaceEditText.getText().toString());
        point.setArrivalTime(mTime);
        point.setDescribe(mNoteEditText.getText().toString());
        point.setIconType(getIcontype());
        point.setImages(images);
        point.setDay(mToday);

        mPresenter.sendNewPoint(point);

    }

    private String getIcontype() {
        String str = "";
        switch (mTypeSpinner.getSelectedItem().toString()) {

            case "飯店":
                return str = "hotel";
            case "餐廳":
                return str = "restaurant";
            case "景點":
                return str = "attraction";
            default:
                break;
        }
        return str = "hotel";
    }

    private void showSuccess() {
        Dialog signinDialog = new Dialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_success, null);
        signinDialog.setContentView(view);
        signinDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                signinDialog.dismiss();
            }
        }, 2000);
        dismiss();
    }

}
