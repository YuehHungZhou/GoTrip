package com.topdsr2.gotrip.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.util.ImageManager;
import com.topdsr2.gotrip.util.HomeAvatarOutlineProvider;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter {

    private HomeContract.Presenter mPresenter;
    private ArrayList<Trip> mTrips;

    public HomeAdapter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HomeViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_home, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (mTrips != null) {
            ImageManager.getInstance().setImageByUrl(((HomeViewHolder) viewHolder).mBackgroundImage,
                    mTrips.get(position).getMainImage());
            ImageManager.getInstance().setImageByUrl(((HomeViewHolder) viewHolder).mUserPhoto,
                    mTrips.get(position).getCreaterImage());

            ((HomeViewHolder) viewHolder).mTitle.setText(mTrips.get(position).getTitle());
            ((HomeViewHolder) viewHolder).mDescribe.setText(mTrips.get(position).getDescribe());
            ((HomeViewHolder) viewHolder).mOwners.setText(Integer.toString(mTrips.get(position).getOwners().size()));
            ((HomeViewHolder) viewHolder).mCollectionNumber.setText(Integer.toString(mTrips.get(position).getCollectionNumber()));
        }
    }

    @Override
    public int getItemCount() {
        if (mTrips != null) {
            return mTrips.size();
        }
        return 0;
    }

    private class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private TextView mDescribe;
        private ImageView mCollection;
        private ImageView mCollectionTint;
        private ImageView mUserPhoto;
        private TextView mOwners;
        private TextView mCollectionNumber;
        private ImageView mBackgroundImage;
        private CardView mCardView;


        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.text_home_title);
            mDescribe = itemView.findViewById(R.id.text_home_describe);
            mCollection = itemView.findViewById(R.id.image_home_collection);
            mCollectionTint = itemView.findViewById(R.id.image_home_collection_tint);
            mUserPhoto = itemView.findViewById(R.id.image_home_photo);
            mUserPhoto.setOutlineProvider(new HomeAvatarOutlineProvider());
            mOwners = itemView.findViewById(R.id.text_home_owner_number);
            mCollectionNumber = itemView.findViewById(R.id.text_home_collection_number);
            mBackgroundImage = itemView.findViewById(R.id.image_home_backgroud);
            mCardView = itemView.findViewById(R.id.cardview_home_item);

            mCollection.setOnClickListener(this);
            mCollectionTint.setOnClickListener(this);
            mCardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_home_collection:
                    mCollectionTint.setVisibility(View.VISIBLE);
                    break;
                case R.id.image_home_collection_tint:
                    mCollectionTint.setVisibility(View.INVISIBLE);
                    break;
                case R.id.cardview_home_item:
                    mPresenter.loadTrip(mTrips.get(getAdapterPosition()).getId());
                    break;
                default:
                    break;
            }
        }
    }

    public void updateData(ArrayList<Trip> trips) {
        mTrips = trips;
        notifyDataSetChanged();
    }
}
