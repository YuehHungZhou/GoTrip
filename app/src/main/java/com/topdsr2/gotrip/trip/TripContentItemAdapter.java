package com.topdsr2.gotrip.trip;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.topdsr2.gotrip.R;

public class TripContentItemAdapter extends RecyclerView.Adapter {

    public TripContentItemAdapter() {
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
}
