package com.topdsr2.gotrip.trip;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Point;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TripContentItemAdapter extends RecyclerView.Adapter {

    private TripContract.Presenter mPresenter;
    private ArrayList<Point> mPoints;
    private int mTripDay;
    private ArrayList<Object> mPointsByDay;
    private ArrayList<Point> mReadyPoints;
    private long mRoadTime;
    private int mPsositionState;

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (mReadyPoints != null) {
            switch (mReadyPoints.get(position).getIconType()) {

                case  "hotel":
                    ((TripContentItemViewHolder) viewHolder).mIconImage.setImageResource(R.mipmap.icon_luggage);
                    break;
                case  "restaurant":
                    ((TripContentItemViewHolder) viewHolder).mIconImage.setImageResource(R.mipmap.icon_cutlery);
                    break;
                case  "attraction":
                    ((TripContentItemViewHolder) viewHolder).mIconImage.setImageResource(R.mipmap.icon_camera);
                    break;
                default:
                    break;

            }

            long roadTime = (mReadyPoints.get(position).getArrivalTime());
            if ((roadTime - mRoadTime) != roadTime) {
                ((TripContentItemViewHolder) viewHolder).mRoadText.setText(paseRoadTime(roadTime - mRoadTime));
            }
            ((TripContentItemViewHolder) viewHolder).mIconText.setText(paseTime(roadTime));

            mRoadTime = roadTime;

        }


    }

    @Override
    public int getItemCount() {
        if (mReadyPoints != null) {
            return mReadyPoints.size();
        }
        return 0;
    }

    private class TripContentItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mIconImage;
        private TextView mIconText;
        private TextView mRoadText;


        public TripContentItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mIconImage = itemView.findViewById(R.id.image_trip_content_icon);
            mIconText = itemView.findViewById(R.id.text_trip_icon_pointime);
            mRoadText = itemView.findViewById(R.id.text_trip_icon_roadtime);

            mIconImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mPresenter.changeIconInfo(getAdapterPosition());
        }
    }



    public void updateData(ArrayList<Point> points) {

        if (points != null) {
            mPoints = points;
            parsePointData();
            notifyDataSetChanged();
        }
    }

    public void readyChangeIcon(int position) {
        if (mPsositionState != position) {
            mPsositionState = position;
            mReadyPoints = (ArrayList<Point>) mPointsByDay.get(position);
            notifyDataSetChanged();
        }
    }

    private void parsePointData() {

        mPointsByDay = new ArrayList<>();
        mReadyPoints = new ArrayList<>();

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

        mReadyPoints = (ArrayList<Point>) mPointsByDay.get(0);
    }

    private String paseTime(long time) {
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
        Date date = new Date(time * 1000);
        String str = sdf.format(date);

        return  str;
    }

    private String paseRoadTime(long time) {
        float minute = time/(60*60);
        String str = minute + " hr";

        return  str;
    }
}
