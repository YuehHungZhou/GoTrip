package com.topdsr2.gotrip.util;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.R;


public class ProfileAvatarOutlineProvider extends ViewOutlineProvider {
    @Override
    public void getOutline(View view, Outline outline) {
        view.setClipToOutline(true);
        int radius = GoTrip.getmContext().getResources().getDimensionPixelSize(R.dimen.radius_profile_avatar);
        outline.setOval(0, 0, radius, radius);
    }
}
