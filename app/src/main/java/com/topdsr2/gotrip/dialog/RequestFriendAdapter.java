package com.topdsr2.gotrip.dialog;

import android.icu.lang.UScript;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.topdsr2.gotrip.data.object.User;
import com.topdsr2.gotrip.home.HomeContract;
import com.topdsr2.gotrip.util.HomeAvatarOutlineProvider;
import com.topdsr2.gotrip.util.ImageManager;
import com.topdsr2.gotrip.util.RequestAvatarOutlineProvider;

import java.util.ArrayList;
import java.util.Iterator;

public class RequestFriendAdapter extends RecyclerView.Adapter {

    private MainContract.Presenter mMainPresenter;
    private Request mRequest;

    public RequestFriendAdapter(MainContract.Presenter presenter) {
        mMainPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FriendViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_request, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((FriendViewHolder)viewHolder).mTitleText.setText(mRequest.getUsers().get(position).getName().trim());

        ImageManager.getInstance().setImageByUrl(((FriendViewHolder) viewHolder).mPhotoImageView,
                mRequest.getUsers().get(position).getUserImage());

    }

    @Override
    public int getItemCount() {
        if (mRequest != null) {
            return mRequest.getUsers().size();
        }
        return 0;
    }

    private class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleText;
        private Button mAgreeImageView;
        private Button mDisagreeImageView;
        private ImageView mPhotoImageView;

        public FriendViewHolder(@NonNull View itemView) {
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
                    mMainPresenter.agreeFriendRequest(mRequest.getUsers().
                            get(getAdapterPosition()).getEmail());
                    removeSelectItem(getAdapterPosition());
                    break;
                case R.id.image_request_disagree:
                    mMainPresenter.disagreeFriendRequest(mRequest.getUsers().
                            get(getAdapterPosition()).getEmail());
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
        Iterator<User> iterator = mRequest.getUsers().iterator();
        while (iterator.hasNext()) {
            if ((iterator.next()).getEmail().equals(mRequest.getUsers().get(position).getEmail())) {
                iterator.remove();
                notifyItemChanged(position);
            }
        }
    }
}
