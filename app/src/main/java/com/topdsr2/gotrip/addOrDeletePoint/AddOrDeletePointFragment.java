package com.topdsr2.gotrip.addOrDeletePoint;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Point;

import java.util.ArrayList;

public class AddOrDeletePointFragment extends BottomSheetDialogFragment implements AddOrDeletePointContract.View{

    private AddOrDeletePointContract.Presenter mPresenter;

    private EditText mPlaceEditText;
    private EditText mNoteEditText;
    private EditText mCostEditText;
    private Spinner mTypeSpinner;
    private Spinner mTimeSpinner;
    private Button mCheckButton;

    public AddOrDeletePointFragment() {
    }

    public static AddOrDeletePointFragment newInstance() {
        return new AddOrDeletePointFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addordeletepoint, container, false);

        mPlaceEditText = root.findViewById(R.id.edit_point_place);
        mNoteEditText = root.findViewById(R.id.edit_point_note);
        mCostEditText = root.findViewById(R.id.edit_point_cost);
        mTypeSpinner = root.findViewById(R.id.spinner_point_type);
        mTimeSpinner = root.findViewById(R.id.spinner_point_time);
        mCheckButton = root.findViewById(R.id.button_point_check);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadPointData();

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
    public void showAddOrDeleteUi() {

    }

    @Override
    public void setPresenter(AddOrDeletePointContract.Presenter presenter) {
        mPresenter = presenter;
    }


    private void sendData(){

        Point point = new Point();
        ArrayList<String> images = new ArrayList<>();
        images.add("123");
        images.add("321");

        point.setCost(Integer.parseInt(mCostEditText.getText().toString()));
        point.setTitle(mPlaceEditText.getText().toString());
        point.setArrivalTime(getTime());
        point.setDescribe(mNoteEditText.getText().toString());
        point.setIconType(getIcontype());
        point.setImages(images);

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

    private long getTime() {
        long i = 0;
        switch (mTimeSpinner.getSelectedItem().toString()) {

            case "01:00":
                return i = 1555261200;
            case "02:00":
                return i = 1555264800;
            case "03:00":
                return i = 1555268400;
            case "04:00":
                return i = 1555272000;
            case "05:00":
                return i = 1555275600;
            case "06:00":
                return i = 1555279200;
            case "07:00":
                return i = 1555282800;
            case "08:00":
                return i = 1555286400;
            case "09:00":
                return i = 1555290000;
            case "10:00":
                return i = 1555293600;
            case "11:00":
                return i = 1555297200;
            case "12:00":
                return i = 1555300800;
            case "13:00":
                return i = 1555304400;
            case "14:00":
                return i = 1555308000;
            case "15:00":
                return i = 1555311600;
            case "16:00":
                return i = 1555315200;
            case "17:00":
                return i = 1555318800;
            case "18:00":
                return i = 1555322400;
            case "19:00":
                return i = 1555326000;
            case "20:00":
                return i = 1555329600;
            case "21:00":
                return i = 1555333200;
            case "22:00":
                return i = 1555336800;
            case "23:00":
                return i = 1555340400;
            case "24:00":
                return i = 1555344000;
            default:
                break;
        }
        return i = 1555261200;
    }

    private void showSuccess(){
        Dialog signinDialog = new Dialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_success,null);
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
