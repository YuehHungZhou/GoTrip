package com.topdsr2.gotrip.trip;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.R;

public class TripContentAdapter extends RecyclerView.Adapter {

    private TripContract.Presenter mPresenter;


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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TripContentItemAdapter tripContentItemAdapter = new TripContentItemAdapter();
        ((TripContentViewHolder) viewHolder).mRecyclerView
                .setLayoutManager(new LinearLayoutManager(
                        GoTrip.getmContext(), LinearLayoutManager.HORIZONTAL, false));
        ((TripContentViewHolder) viewHolder).mRecyclerView.setAdapter(tripContentItemAdapter);


    }

    @Override
    public int getItemCount() {
        return 2;
    }

    private class TripContentViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView mRecyclerView;
        private TextView mTextDay;
        private TextView mTextTitle;
        private TextView mTextCost;
        private TextView mTextDescribe;


        public TripContentViewHolder(@NonNull View itemView) {
            super(itemView);

            mRecyclerView = itemView.findViewById(R.id.recycler_trip_content_icon);
            mTextDay = itemView.findViewById(R.id.text_trip_content_day);
            mTextTitle = itemView.findViewById(R.id.text_trip_content_title);
            mTextCost = itemView.findViewById(R.id.text_trip_content_cost);
            mTextDescribe = itemView.findViewById(R.id.text_trip_content_describe);
        }
    }
}
