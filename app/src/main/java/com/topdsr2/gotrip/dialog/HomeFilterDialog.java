package com.topdsr2.gotrip.dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.topdsr2.gotrip.MainContract;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.SearchData;
import com.topdsr2.gotrip.util.Constants;

public class HomeFilterDialog extends AppCompatDialogFragment implements View.OnClickListener {

    MainContract.Presenter mMainPresenter;

    private Spinner mYesrSpinner;
    private Spinner mMonthSpinner;
    private Spinner mCountrySpinner;
    private SeekBar mDaySeekBar;
    private SeekBar mCollectionSeekBar;
    private TextView mDayTextView;
    private TextView mCollectionTextView;
    private Button mButton;

    private int mDay;
    private int mCollection;

    public HomeFilterDialog() {
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_home_filter, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mYesrSpinner = view.findViewById(R.id.spinner_filter_year);
        mMonthSpinner = view.findViewById(R.id.spinner_filter_month);
        mCountrySpinner = view.findViewById(R.id.spinner_filter_country);
        mDaySeekBar = view.findViewById(R.id.seekBar_filter_day);
        mCollectionSeekBar = view.findViewById(R.id.seekBar_filter_collection);
        mButton = view.findViewById(R.id.button_filter_send);
        mDayTextView = view.findViewById(R.id.text_filter_day_number);
        mCollectionTextView = view.findViewById(R.id.text_filter_collection_number);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.filter_trip_year, R.layout.spinner_text);
        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_text);
        mYesrSpinner.setAdapter(yearAdapter);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.filter_trip_month, R.layout.spinner_text);
        monthAdapter.setDropDownViewResource(R.layout.spinner_dropdown_text);
        mMonthSpinner.setAdapter(monthAdapter);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.trip_country_list, R.layout.spinner_text);
        countryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_text);
        mCountrySpinner.setAdapter(countryAdapter);

        mDaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDayTextView.setText(getString(R.string.home_day_slash, progress, 30));
                mDay = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mCollectionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCollectionTextView.setText(getString(R.string.home_day_slash, progress, 100));
                mCollection = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchData searchData = new SearchData();
                searchData.setYear(Integer.parseInt(mYesrSpinner.getSelectedItem().toString()));

                if (!mMonthSpinner.getSelectedItem().toString().equals(Constants.NONE)) {
                    searchData.setMonth(Integer.parseInt(mMonthSpinner.getSelectedItem().toString()));
                }
                searchData.setDay(mDay);
                searchData.setCountry(mCountrySpinner.getSelectedItem().toString());
                searchData.setCollection(mCollection);

                mMainPresenter.search(searchData);

                dismiss();
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
