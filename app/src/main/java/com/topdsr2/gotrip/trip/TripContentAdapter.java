package com.topdsr2.gotrip.trip;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.util.Constants;

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
                .inflate(R.layout.item_trip_content, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (mPointsHolder != null) {
            if (mPointsHolder.get(position).getSorte() != -1) {
                Point point = mPointsHolder.get(position);

                ((TripContentViewHolder) viewHolder).mTextCost.setText(GoTrip.getContext().
                        getResources().getString(R.string.trip_money, point.getCost()));
                ((TripContentViewHolder) viewHolder).mTextTitle.setText(point.getTitle());
                ((TripContentViewHolder) viewHolder).mTextDescribe.setText(point.getDescribe());

                ((TripContentViewHolder) viewHolder).mTextCost.setVisibility(View.VISIBLE);
                ((TripContentViewHolder) viewHolder).mTextTitle.setVisibility(View.VISIBLE);
                ((TripContentViewHolder) viewHolder).mTextDescribe.setVisibility(View.VISIBLE);
            } else {
                ((TripContentViewHolder) viewHolder).mTextCost.setVisibility(View.INVISIBLE);
                ((TripContentViewHolder) viewHolder).mTextTitle.setVisibility(View.INVISIBLE);
                ((TripContentViewHolder) viewHolder).mTextDescribe.setVisibility(View.INVISIBLE);

            }

            ((TripContentViewHolder) viewHolder).mTextDay.setText(GoTrip.getContext().
                    getResources().getString(R.string.trip_day, (position + 1)));
        }
    }

    @Override
    public int getItemCount() {
        return mTripDay;
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

    public void updateData(ArrayList<Object> pointsByDay, ArrayList<Point> pointsHolder, int tripDay) {
        mPointsByDay = pointsByDay;
        mPointsHolder = pointsHolder;
        mTripDay = tripDay;
        notifyDataSetChanged();
    }

    public void changeSelectedIconInfo(int visitItemPosition, int position) {

        Point point = ((ArrayList<Point>) mPointsByDay.get(visitItemPosition)).get(position);
        setPointsHolder(point, visitItemPosition);
        notifyItemChanged(visitItemPosition);
    }

    public void scrollChangeIconInfo(int visitItemPosition, ArrayList<Point> pointsHolder) {

        ArrayList<Point> points = (ArrayList<Point>) mPointsByDay.get(visitItemPosition);
        if (points != null) {
            if (points.size() != 0) {
                Point point = points.get(0);
                mPresenter.moveMapToIcon(point.getLatitude(), point.getLongitude());
                setPointsHolder(point, visitItemPosition);
            }
            notifyDataSetChanged();
        }
    }

    private void setPointsHolder(Point point, int visitItemPosition) {
        mPointsHolder.clear();
        for (int i = 0; i < mTripDay; i++) {
            if (i == visitItemPosition) {
                mPointsHolder.add(point);
            } else {
                if (((ArrayList<Point>) mPointsByDay.get(i)).size() != 0) {
                    mPointsHolder.add(((ArrayList<Point>) mPointsByDay.get(i)).get(0));
                } else {
                    mPointsHolder.add(new Point());
                }
            }
        }
    }
}
