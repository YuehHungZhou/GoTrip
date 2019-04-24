package com.topdsr2.gotrip.profile.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Image;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.util.HomeAvatarOutlineProvider;
import com.topdsr2.gotrip.util.ImageManager;
import com.topdsr2.gotrip.util.ProfileAvatarOutlineProvider;

import java.util.ArrayList;

import static com.topdsr2.gotrip.MainMvpController.COLLECTIONTRIP;
import static com.topdsr2.gotrip.MainMvpController.COMPLETETRIP;
import static com.topdsr2.gotrip.MainMvpController.NEWTRIP;

public class ProfileItemAdapter extends RecyclerView.Adapter {

    public static final int TYPE_NEWTRIP = 0x01;
    public static final int TYPE_COMPLETETRIP = 0x02;
    public static final int TYPE_COLLECTIONTRIP = 0x03;


    private ProfileItemContract.Presenter mPresenter;
    private String mItemType;
    private ArrayList<Trip> mTrips;

    public ProfileItemAdapter(ProfileItemContract.Presenter presenter, String itemType) {
        mPresenter = presenter;
        mItemType = itemType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_NEWTRIP) {
            return new NewTripViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_profile_newtrip, viewGroup, false));
        } else if (viewType == TYPE_COMPLETETRIP) {
            return new CompleteTripViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_profile_completetrip, viewGroup, false));
        } else {
            return new CollectionTripViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_profile_collectiontrip, viewGroup, false));
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof NewTripViewHolder) {

            bindNewTripViewHolder((NewTripViewHolder) viewHolder, position);

        } else if (viewHolder instanceof CompleteTripViewHolder) {

            bindCompleteTripViewHolder((CompleteTripViewHolder) viewHolder, position);

        } else if (viewHolder instanceof CollectionTripViewHolder) {

            bindCollectionTripTripViewHolder((CollectionTripViewHolder) viewHolder, position);

        }
    }

    private void bindNewTripViewHolder(NewTripViewHolder holder, int position) {
        holder.mDeleteButton.setVisibility(View.GONE);

        if (mTrips != null && mTrips.size() != 0) {
            holder.mTitleText.setText(mTrips.get(position).getTitle());
            holder.mDescribeText.setText(mTrips.get(position).getDescribe());
            holder.mOwnerNumberText.setText(Integer.toString(mTrips.get(position).getOwners().size()));

        }
    }

    private void bindCompleteTripViewHolder(CompleteTripViewHolder holder, int position) {
        holder.mDeleteButton.setVisibility(View.GONE);


        if (mTrips != null && mTrips.size() != 0) {
            holder.mTitleText.setText(mTrips.get(position).getTitle());
            holder.mDescribeText.setText(mTrips.get(position).getDescribe());
            holder.mOwnerNumberText.setText(Integer.toString(mTrips.get(position).getOwners().size()));
            holder.mCollectionNumberText.setText(Integer.toString(mTrips.get(position).getCollectionNumber()));

            ImageManager.getInstance().setImageByUrl(((CompleteTripViewHolder) holder).mUserPhotoImage,
                    mTrips.get(position).getCreaterImage());

        }
    }

    private void bindCollectionTripTripViewHolder(CollectionTripViewHolder holder, int position) {
        holder.mDeleteButton.setVisibility(View.GONE);

        if (mTrips != null && mTrips.size() != 0) {
            holder.mTitleText.setText(mTrips.get(position).getTitle());
            holder.mDescribeText.setText(mTrips.get(position).getDescribe());
            holder.mOwnerNumberText.setText(Integer.toString(mTrips.get(position).getOwners().size()));
            holder.mCollectionNumberText.setText(Integer.toString(mTrips.get(position).getCollectionNumber()));

            ImageManager.getInstance().setImageByUrl(((CollectionTripViewHolder) holder).mBackgroundImage,
                    mTrips.get(position).getMainImage());
            ImageManager.getInstance().setImageByUrl(((CollectionTripViewHolder) holder).mUserPhotoImage,
                    mTrips.get(position).getCreaterImage());
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (mItemType) {
            case NEWTRIP:
                return TYPE_NEWTRIP;
            case COMPLETETRIP:
                return TYPE_COMPLETETRIP;
            case COLLECTIONTRIP:
                return TYPE_COLLECTIONTRIP;
            default:
                break;
        }
        return TYPE_NEWTRIP;
    }

    @Override
    public int getItemCount() {

        if (mTrips != null && mTrips.size() != 0) {
            return mTrips.size();
        }
        return 0;
    }

    private class NewTripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView mTitleText;
        private TextView mDescribeText;
        private TextView mOwnerNumberText;
        private ImageView mBackgroundImage;
        private CardView mCardView;
        private ImageButton mDeleteButton;

        public NewTripViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.text_newtrip_title);
            mDescribeText = itemView.findViewById(R.id.text_newtrip_describe);
            mOwnerNumberText = itemView.findViewById(R.id.text_newtrip_owner_number);
            mBackgroundImage = itemView.findViewById(R.id.image_newtrip);
            mCardView = itemView.findViewById(R.id.cardview_newtrip);
            mDeleteButton = itemView.findViewById(R.id.imageButton_newtrip_delete);

            mCardView.setOnClickListener(this);
            mCardView.setOnLongClickListener(this);
            mDeleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.cardview_newtrip:
                    mPresenter.loadTrip(mTrips.get(getAdapterPosition()).getId());
                    break;
                case R.id.imageButton_newtrip_delete:
                    mPresenter.deleteTrip(mTrips.get(getAdapterPosition()), TYPE_NEWTRIP);
                    break;
                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            mDeleteButton.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private class CompleteTripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView mTitleText;
        private TextView mDescribeText;
        private TextView mOwnerNumberText;
        private ImageView mUserPhotoImage;
        private TextView mCollectionNumberText;
        private ImageView mBackgroundImage;
        private CardView mCardView;
        private ImageButton mDeleteButton;

        public CompleteTripViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.text_completetrip_title);
            mDescribeText = itemView.findViewById(R.id.text_completetrip_describe);
            mOwnerNumberText = itemView.findViewById(R.id.text_completetrip_owner_number);
            mCollectionNumberText = itemView.findViewById(R.id.text_completetrip_collection_number);
            mBackgroundImage = itemView.findViewById(R.id.image_completetrip);
            mCardView = itemView.findViewById(R.id.cardview_completetrip);
            mUserPhotoImage = itemView.findViewById(R.id.image_completetrip_photo);
            mDeleteButton = itemView.findViewById(R.id.imageButton_completetrip_delete);

            mUserPhotoImage.setOutlineProvider(new HomeAvatarOutlineProvider());

            mCardView.setOnClickListener(this);
            mCardView.setOnLongClickListener(this);
            mDeleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.cardview_completetrip:
                    mPresenter.loadTrip(mTrips.get(getAdapterPosition()).getId());
                    break;
                case R.id.imageButton_completetrip_delete:
                    mPresenter.deleteTrip(mTrips.get(getAdapterPosition()), TYPE_COMPLETETRIP);
                    break;
                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            mDeleteButton.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private class CollectionTripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView mTitleText;
        private TextView mDescribeText;
        private ImageView mCollectionTintImage;
        private ImageView mUserPhotoImage;
        private TextView mOwnerNumberText;
        private TextView mCollectionNumberText;
        private ImageView mBackgroundImage;
        private CardView mCardView;
        private ImageButton mDeleteButton;

        public CollectionTripViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.text_colletiontrip_title);
            mDescribeText = itemView.findViewById(R.id.text_collectiontrip_describe);
            mCollectionTintImage = itemView.findViewById(R.id.image_collectiontrip_collection_tint);
            mUserPhotoImage = itemView.findViewById(R.id.image_collectiontrip_photo);
            mOwnerNumberText = itemView.findViewById(R.id.text_collectiontrip_owner_number);
            mCollectionNumberText = itemView.findViewById(R.id.text_collectiontrip_collection_number);
            mBackgroundImage = itemView.findViewById(R.id.image_collectiontrip);
            mCardView = itemView.findViewById(R.id.cardview_collectiontrip);
            mDeleteButton = itemView.findViewById(R.id.imageButton_collectiontrip_delete);

            mUserPhotoImage.setOutlineProvider(new HomeAvatarOutlineProvider());

            mCollectionTintImage.setOnClickListener(this);
            mCardView.setOnClickListener(this);
            mCardView.setOnLongClickListener(this);
            mDeleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.image_collectiontrip_collection_tint:
                    break;
                case R.id.cardview_collectiontrip:
                    mPresenter.loadTrip(mTrips.get(getAdapterPosition()).getId());
                    break;
                case R.id.imageButton_collectiontrip_delete:
                    mPresenter.deleteTrip(mTrips.get(getAdapterPosition()), TYPE_COLLECTIONTRIP);
                    break;
                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            mDeleteButton.setVisibility(View.VISIBLE);
            return true;
        }
    }

    public void updateData(ArrayList<Trip> trips) {
        mTrips = trips;
        notifyDataSetChanged();
    }
}
