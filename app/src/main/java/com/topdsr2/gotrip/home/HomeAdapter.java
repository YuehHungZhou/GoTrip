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
import java.util.Iterator;

public class HomeAdapter extends RecyclerView.Adapter {

    private HomeContract.Presenter mPresenter;
    private ArrayList<Trip> mTrips;
    private ArrayList<String> mTripCollection;
    private boolean isCanCollete = true;

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
            ((HomeViewHolder) viewHolder).mOwners.
                    setText(Integer.toString(mTrips.get(position).getOwners().size()));
            ((HomeViewHolder) viewHolder).mCollectionNumber.
                    setText(Integer.toString(mTrips.get(position).getCollectionNumber()));

            if (mTripCollection != null) {
                if (mTripCollection.contains(mTrips.get(position).getId())) {
                    ((HomeViewHolder) viewHolder).mCollection.setVisibility(View.INVISIBLE);
                    ((HomeViewHolder) viewHolder).mCollectionTint.setVisibility(View.VISIBLE);
                } else {
                    ((HomeViewHolder) viewHolder).mCollection.setVisibility(View.VISIBLE);
                    ((HomeViewHolder) viewHolder).mCollectionTint.setVisibility(View.INVISIBLE);
                }
            }

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

                    if (isCanCollete) {
                        isCanCollete = false;
                        mCollection.setVisibility(View.INVISIBLE);
                        mCollectionTint.setVisibility(View.VISIBLE);
                        addCollection(mTrips.get(getAdapterPosition()).getId());
                        mPresenter.changeCollection(mTripCollection);
                        isCanCollete = true;
                    }
                    break;

                case R.id.image_home_collection_tint:

                    if (isCanCollete) {
                        isCanCollete = false;
                        mCollection.setVisibility(View.VISIBLE);
                        mCollectionTint.setVisibility(View.INVISIBLE);
                        removeCollection(mTrips.get(getAdapterPosition()).getId());
                        mPresenter.changeCollection(mTripCollection);
                        isCanCollete = true;
                    }
                    break;

                case R.id.cardview_home_item:
                    mPresenter.openTrip();
                    mPresenter.hideBtmNavi();
                    mPresenter.loadTripData(mTrips.get(getAdapterPosition()).getId());
                    break;
                default:
                    break;
            }
        }
    }

    private void addCollection(String collectionId) {
        mTripCollection.add(collectionId);
    }

    private void removeCollection(String collectionId) {
        Iterator<String> RemoveListIterator = mTripCollection.iterator();
        while(RemoveListIterator.hasNext()) {
            String str = RemoveListIterator.next();
            if (str.equals(collectionId)) {
                RemoveListIterator.remove();
            }
        }
    }

    public void updateData(ArrayList<Trip> trips) {
        mTrips = trips;
        notifyDataSetChanged();
    }

    public void updateTripCollection(ArrayList<String> tripCollection) {
        mTripCollection = tripCollection;
        notifyDataSetChanged();
    }
}
