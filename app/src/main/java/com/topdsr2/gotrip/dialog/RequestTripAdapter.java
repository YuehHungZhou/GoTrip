package com.topdsr2.gotrip.dialog;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.topdsr2.gotrip.MainContract;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.Request;
import com.topdsr2.gotrip.data.object.Trip;
import com.topdsr2.gotrip.util.ImageManager;
import com.topdsr2.gotrip.util.RequestAvatarOutlineProvider;

import java.util.Iterator;

public class RequestTripAdapter extends RecyclerView.Adapter {

    private MainContract.Presenter mMainPresenter;
    private Request mRequest;

    public RequestTripAdapter(MainContract.Presenter presenter) {
        mMainPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TripViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_request, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((TripViewHolder)viewHolder).mTitleText.setText(mRequest.getTrips().get(position).getTitle().trim());

        ImageManager.getInstance().setImageByUrl(((TripViewHolder) viewHolder).mPhotoImageView,
                mRequest.getTrips().get(position).getCreaterImage());
    }

    @Override
    public int getItemCount() {

        if (mRequest != null) {
            return mRequest.getTrips().size();
        }
        return 0;
    }

    private class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleText;
        private Button mAgreeImageView;
        private Button mDisagreeImageView;
        private ImageView mPhotoImageView;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.text_request_title);
            mAgreeImageView = itemView.findViewById(R.id.image_request_agree);
            mDisagreeImageView = itemView.findViewById(R.id.image_request_disagree);
            mPhotoImageView = itemView.findViewById(R.id.image_request_photo);

            mPhotoImageView.setOutlineProvider(new RequestAvatarOutlineProvider());


            mAgreeImageView.setOnClickListener(this);
            mDisagreeImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_request_agree:
                    mMainPresenter.agreeTripRequest(mRequest.getTrips().get(getAdapterPosition()).getId());
                    removeSelectItem(getAdapterPosition());
                    break;
                case R.id.image_request_disagree:
                    mMainPresenter.disagreeTripRequest(mRequest.getTrips().get(getAdapterPosition()).getId());
                    removeSelectItem(getAdapterPosition());
                    break;
                default:
                    break;
            }

        }
    }

    public void updateData(Request request) {
        mRequest = request;
        notifyDataSetChanged();
    }

    private void removeSelectItem(int position) {
        Iterator<Trip> iterator = mRequest.getTrips().iterator();
        while (iterator.hasNext()) {
            if ((iterator.next()).getId().equals(mRequest.getTrips().get(position).getId())) {
                iterator.remove();
                notifyItemChanged(position);
            }
        }
    }
}
