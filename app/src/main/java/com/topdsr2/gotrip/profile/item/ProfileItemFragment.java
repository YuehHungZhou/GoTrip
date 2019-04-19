package com.topdsr2.gotrip.profile.item;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topdsr2.gotrip.MainMvpController;
import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.profile.ProfileContract;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.topdsr2.gotrip.MainMvpController.COLLECTIONTRIP;
import static com.topdsr2.gotrip.MainMvpController.COMPLETETRIP;
import static com.topdsr2.gotrip.MainMvpController.NEWTRIP;

public class ProfileItemFragment extends Fragment implements ProfileItemContract.View {

    private ProfileItemContract.Presenter mPresenter;
    private ProfileItemAdapter mProfileItemAdapter;

    private String mItemType;

    public ProfileItemFragment() {
    }

    public static ProfileItemFragment newInstance() {
        return  new ProfileItemFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfileItemAdapter = new ProfileItemAdapter(mPresenter, mItemType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile_child, container, false);

        RecyclerView profileRecyclerView = root.findViewById(R.id.recycle_profile_child);
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profileRecyclerView.setAdapter(mProfileItemAdapter);




        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (mItemType) {
            case NEWTRIP:
                mPresenter.loadNewTripData();
                break;
            case COMPLETETRIP:
                mPresenter.loadCompleteTripData();
                break;
            case COLLECTIONTRIP:
                mPresenter.loadCollectionTripData();
                break;
            default:
        }
    }

    @Override
    public void showTripUi() {

        mProfileItemAdapter.updateData();

    }

    @Override
    public void setPresenter(ProfileItemContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public void setItemType(@MainMvpController.ProfileItem String itemType) {
        mItemType = itemType;
    }


}
