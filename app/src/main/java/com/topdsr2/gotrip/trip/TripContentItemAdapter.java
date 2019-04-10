package com.topdsr2.gotrip.trip;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Point;

import java.util.ArrayList;

public class TripContentItemAdapter extends RecyclerView.Adapter {

    private TripContract.Presenter mPresenter;
    private ArrayList<Point> mPoints;
    private int mTripDay;
    private ArrayList<Object> mPointsByDay;

    public TripContentItemAdapter(TripContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new TripContentItemViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_trip_content_icon,viewGroup,false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class TripContentItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public TripContentItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_trip_content_icon);
        }
    }

    public void updateData(ArrayList<Point> points) {

        if (points != null) {
            mPoints = points;
            notifyDataSetChanged();
        }
    }

    private void parsePointData() {

        mPointsByDay = new ArrayList<>();

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
    }
}
