package com.topdsr2.gotrip.trip;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TripContentItemAdapter extends RecyclerView.Adapter {

    private TripContract.Presenter mPresenter;
    private ArrayList<Object> mPointsByDay;
    private ArrayList<Point> mReadyPoints;
    private long mRoadTime;
    private int mPositionState;
    private int mIconState;

    public TripContentItemAdapter(TripContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        return new TripContentItemViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_trip_content_icon, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (mReadyPoints != null) {
            if (mReadyPoints.size() != 0) {
                switch (mReadyPoints.get(position).getIconType()) {

                    case Constants.HOTEL:
                        ((TripContentItemViewHolder) viewHolder).mIconImage
                                .setImageResource(R.mipmap.icon_luggage);
                        ((TripContentItemViewHolder) viewHolder).mConstraintLayout
                                .setBackgroundResource((R.drawable.corner_icon_yellow));
                        break;
                    case Constants.RESTAURANT:
                        ((TripContentItemViewHolder) viewHolder).mIconImage
                                .setImageResource(R.mipmap.icon_cutlery);
                        ((TripContentItemViewHolder) viewHolder).mConstraintLayout
                                .setBackgroundResource((R.drawable.corner_icon_green));
                        break;
                    case Constants.ATTRACTION:
                        ((TripContentItemViewHolder) viewHolder).mIconImage
                                .setImageResource(R.mipmap.icon_camera);
                        ((TripContentItemViewHolder) viewHolder).mConstraintLayout
                                .setBackgroundResource((R.drawable.corner_icon_blue));
                        break;
                    default:
                        break;
                }

                if (mIconState == mReadyPoints.get(position).getSorte()) {
                    ((TripContentItemViewHolder) viewHolder).mConstraintLayout
                            .setBackgroundResource((R.drawable.corner_icon_orange));
                }

                long roadTime = (mReadyPoints.get(position).getArrivalTime());
                if ((roadTime - mRoadTime) != roadTime) {
                    ((TripContentItemViewHolder) viewHolder).mRoadText
                            .setText(paseRoadTime(roadTime - mRoadTime));
                }
                ((TripContentItemViewHolder) viewHolder).mIconText.setText(paseTime(roadTime));

                mRoadTime = roadTime;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mReadyPoints != null) {
            return mReadyPoints.size();
        }
        return 0;
    }

    private class TripContentItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private ImageView mIconImage;
        private TextView mIconText;
        private TextView mRoadText;
        private ConstraintLayout mConstraintLayout;

        public TripContentItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mIconImage = itemView.findViewById(R.id.image_trip_content_icon);
            mIconText = itemView.findViewById(R.id.text_trip_icon_pointime);
            mRoadText = itemView.findViewById(R.id.text_trip_icon_roadtime);
            mConstraintLayout = itemView.findViewById(R.id.constraint_icon);

            mIconImage.setOnClickListener(this);
            mIconImage.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mIconState = mReadyPoints.get(getAdapterPosition()).getSorte();
            mPresenter.changeIconInfo(getAdapterPosition());
            mPresenter.moveMapToIcon(mReadyPoints.get(getAdapterPosition()).getLatitude(),
                    mReadyPoints.get(getAdapterPosition()).getLongitude());
            mPresenter.showVoteView(getAdapterPosition());
            notifyDataSetChanged();
        }

        @Override
        public boolean onLongClick(View v) {

            mPresenter.moveMapToIcon(mReadyPoints.get(getAdapterPosition()).getLatitude(),
                    mReadyPoints.get(getAdapterPosition()).getLongitude());
            mPresenter.changeIconInfo(getAdapterPosition());
            mPresenter.showPointDeleteView(getAdapterPosition());

            return true;
        }
    }


    public void updateData(ArrayList<Object> pointsByDay, ArrayList<Point> readyPoints) {

        mPointsByDay = pointsByDay;
        mReadyPoints = readyPoints;
        mIconState = 1;
        notifyDataSetChanged();

    }

    public void readyChangeIcon(int position) {
        mPositionState = position;
        mReadyPoints = (ArrayList<Point>) mPointsByDay.get(position);
        mIconState = 1;
        notifyDataSetChanged();
    }

    private String paseTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_HH_MM);
        Date date = new Date(time * 1000);
        String str = sdf.format(date);

        return str;
    }

    private String paseRoadTime(long time) {
        float minute = time / (60 * 60);
        return GoTrip.getContext().getResources().getString(R.string.time_hr, minute);
    }
}
