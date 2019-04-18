package com.topdsr2.gotrip.profile;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.home.HomeContract;

public class ProfileAdapter extends RecyclerView.Adapter {

    private ProfileContract.Presenter mPresenter;

    public ProfileAdapter(ProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProfileViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_home, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 12;
    }

    private class ProfileViewHolder extends RecyclerView.ViewHolder {


        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
