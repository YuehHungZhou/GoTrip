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

            ((TripContentViewHolder) viewHolder).mTextDay.setText("Day " + Integer.toString(point.getDay()));
            ((TripContentViewHolder) viewHolder).mTextDay.setBackgroundResource(R.drawable.corner_day_color);

            ((TripContentViewHolder) viewHolder).mTextCost.setText("$ " + Integer.toString(point.getCost()));
            ((TripContentViewHolder) viewHolder).mTextCost.setBackgroundResource(R.drawable.corner_cost_color);

            ((TripContentViewHolder) viewHolder).mTextTitle.setText(point.getTitle());
            ((TripContentViewHolder) viewHolder).mTextTitle.setBackgroundResource(R.drawable.corner_title_color);

            ((TripContentViewHolder) viewHolder).mTextDescribe.setText(point.getDescribe());
            ((TripContentViewHolder) viewHolder).mTextDescribe.setBackgroundResource(R.drawable.corner_describe_color);

        }
    }

    @Override
    public int getItemCount() {
        if (mPointsHolder != null) {
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

    public void updateData(ArrayList<Object> pointsByDay, ArrayList<Point> pointsHolder, int tripDay ) {
        mPointsByDay = pointsByDay;
        mPointsHolder = pointsHolder;
        mTripDay = tripDay;
        notifyDataSetChanged();

    }

    public void changeSelectedIconInfo(int visitItemPosition, int position) {

        Point point = ((ArrayList<Point>)mPointsByDay.get(visitItemPosition)).get(position);
        setPointsHolder(point);
    }

    public void scrollChangeIconInfo(int visitItemPosition) {
        ArrayList<Point> points = (ArrayList<Point>) mPointsByDay.get(visitItemPosition);
        if (points != null) {
            Point point = points.get(0);
            setPointsHolder(point);
            mPresenter.moveMapToIcon(point.getLatitude(), point.getLongitude());
        } else {
            Point point = new Point();
            setPointsHolder(point);
        }
    }



    private void setPointsHolder(Point point) {
        mPointsHolder.clear();
        for (int i = 0;i < mTripDay;i++) {
            mPointsHolder.add(point);
        }
        notifyDataSetChanged();
    }
}
