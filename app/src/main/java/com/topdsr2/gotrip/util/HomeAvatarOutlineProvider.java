package com.topdsr2.gotrip.util;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.topdsr2.gotrip.GoTrip;
import com.topdsr2.gotrip.R;


public class HomeAvatarOutlineProvider extends ViewOutlineProvider {
    @Override
    public void getOutline(View view, Outline outline) {
        view.setClipToOutline(true);
        int radius = GoTrip.getContext().getResources().getDimensionPixelSize(R.dimen.radius_home_avatar);
        outline.setOval(0, 0, radius, radius);
    }
}
