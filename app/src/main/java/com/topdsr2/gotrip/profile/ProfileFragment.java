package com.topdsr2.gotrip.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.topdsr2.gotrip.R;
import com.topdsr2.gotrip.data.object.User;
import com.topdsr2.gotrip.util.ImageManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProfileFragment extends Fragment implements ProfileContract.View {

    private ProfileContract.Presenter mPresenter;
    private ProfileAdapter mProfileAdapter;
    private User mUser;

    private ImageView mPhotoImage;
    private TextView mNameText;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;


    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfileAdapter = new ProfileAdapter(getChildFragmentManager(), mPresenter);
        mPresenter.loadUserData();

    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mTabLayout = root.findViewById(R.id.tab_profile);
        mViewPager = root.findViewById(R.id.pager_profile);
        mPhotoImage = root.findViewById(R.id.image_profile_photo);
        mNameText = root.findViewById(R.id.text_profile_name);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNameText.setText(mUser.getName());
        ImageManager.getInstance().setImageByUrl(mPhotoImage, mUser.getUserImage());

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mProfileAdapter);
        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }


    @Override
    public void showProfileUi(User user) {
        mUser = user;
    }

}
