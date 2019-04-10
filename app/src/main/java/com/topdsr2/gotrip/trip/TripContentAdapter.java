package com.topdsr2.gotrip.trip;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Point;

import java.util.ArrayList;

public class TripContentAdapter extends RecyclerView.Adapter {

    private TripContract.Presenter mPresenter;
    private ArrayList<Point> mPoints;
    private int mTripDay;
    private ArrayList<Object> mPointsByDay;
    private ArrayList<Point> mPointsHolder;


    public TripContentAdapter(TripContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TripContentViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_trip_content,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (mPointsHolder != null) {
            Point point = mPointsHolder.get(position);

            ((TripContentViewHolder) viewHolder).mTextDay.setText(Integer.toString(point.getDay()));
            ((TripContentViewHolder) viewHolder).mTextCost.setText(Integer.toString(point.getCost()));
            ((TripContentViewHolder) viewHolder).mTextTitle.setText(point.getTitle());
            ((TripContentViewHolder) viewHolder).mTextDescribe.setText(point.getDescribe());
        }
    }

    @Override
    public int getItemCount() {
        if (mPoints != null) {
            return mTripDay;
        }else {
            return 1;
        }
    }

    private class TripContentViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextDay;
        private TextView mTextTitle;
        private TextView mTextCost;
        private TextView mTextDescribe;


        public TripContentViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextDay = itemView.findViewById(R.id.text_trip_content_day);
            mTextTitle = itemView.findViewById(R.id.text_trip_content_title);
            mTextCost = itemView.findViewById(R.id.text_trip_content_cost);
            mTextDescribe = itemView.findViewById(R.id.text_trip_content_describe);
        }
    }

    public void updateData(ArrayList<Point> points) {

        if (points != null) {
            mPoints = points;
            parsePointData();
            notifyDataSetChanged();
        }
    }

    private void parsePointData() {

        mPointsByDay = new ArrayList<>();
        mPointsHolder = new ArrayList<>();

        for (int i = 0; i < mPoints.size(); i++) {
            mTripDay = 0;
            if (mPoints.get(i).getDay() > mTripDay) {
                mTripDay = mPoints.get(i).getDay();
            }
        }

        for (int i = 1;i <= mTripDay; i++) {
            ArrayList<Point> points = new ArrayList<>();

            for (int j = 0; j < mPoints.size(); j++) {
                if (mPoints.get(j).getDay() == i) {
                    points.add(mPoints.get(j));
                }
            }
            mPointsByDay.add(points);
        }


        for (int i = 0;i < mTripDay; i++) {

            for (int j = 0; j < ((ArrayList<Point>)mPointsByDay.get(i)).size(); j++) {

                if (((ArrayList<Point>)mPointsByDay.get(i)).get(j).getSorte() == 1) {
                    mPointsHolder.add(((ArrayList<Point>)mPointsByDay.get(i)).get(j));
                }

            }
        }
    }
}
