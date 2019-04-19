package com.topdsr2.gotrip.profile.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topdsr2.gotrip.R;

import static com.topdsr2.gotrip.MainMvpController.COLLECTIONTRIP;
import static com.topdsr2.gotrip.MainMvpController.COMPLETETRIP;
import static com.topdsr2.gotrip.MainMvpController.NEWTRIP;

public class ProfileItemAdapter extends RecyclerView.Adapter {

    private static final int TYPE_NEWTRIP   = 0x01;
    private static final int TYPE_COMPLETETRIP  = 0x02;
    private static final int TYPE_COLLECTIONTRIP   = 0x03;


    private ProfileItemContract.Presenter mPresenter;
    private String mItemType;

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof NewTripViewHolder) {

            bindNewTripViewHolder((NewTripViewHolder) viewHolder);

        } else  if (viewHolder instanceof CompleteTripViewHolder) {

            bindCompleteTripViewHolder((CompleteTripViewHolder) viewHolder);

        } else  if (viewHolder instanceof CollectionTripViewHolder) {

            bindCollectionTripTripViewHolder((CollectionTripViewHolder) viewHolder);

        }
    }

    private void bindNewTripViewHolder(NewTripViewHolder holder) {

    }

    private void bindCompleteTripViewHolder(CompleteTripViewHolder holder) {

    }

    private void bindCollectionTripTripViewHolder(CollectionTripViewHolder holder) {

    }

    @Override
    public int getItemViewType(int position) {

        switch (mItemType) {
            case  NEWTRIP :
                return TYPE_NEWTRIP;
            case  COMPLETETRIP :
                return TYPE_COMPLETETRIP;
            case  COLLECTIONTRIP :
                return TYPE_COLLECTIONTRIP;
            default:
                break;
        }
        return TYPE_NEWTRIP;
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    private class NewTripViewHolder extends RecyclerView.ViewHolder {

        public NewTripViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class CompleteTripViewHolder extends RecyclerView.ViewHolder {

        public CompleteTripViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class CollectionTripViewHolder extends RecyclerView.ViewHolder {

        public CollectionTripViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void updateData() {

        notifyDataSetChanged();
    }
}
