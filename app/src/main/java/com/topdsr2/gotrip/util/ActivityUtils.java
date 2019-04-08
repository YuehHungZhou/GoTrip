package com.topdsr2.gotrip.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.topdsr2.gotrip.MainMvpController;
import com.topdsr2.gotrip.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActivityUtils {

    public static void showOrAddFragmentByTag(@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment,
                                              @MainMvpController.FragmentType String fragmentTag) {
        checkNotNull(fragmentManager);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment element : fragmentManager.getFragments()) {
            if (!element.isHidden()) {
                transaction.hide(element);
                break;
            }
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.layout_main_container, fragment, fragmentTag);
        }

        transaction.commit();
    }

    public static void addFragmentByTag(@NonNull FragmentManager fragmentManager,
                                        @NonNull Fragment fragment,
                                        @MainMvpController.FragmentType String fragmentTag) {
        checkNotNull(fragmentManager);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment element : fragmentManager.getFragments()) {
            if (!element.isHidden()) {
                transaction.hide(element);
                transaction.addToBackStack(null);
                break;
            }
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.layout_main_container, fragment, fragmentTag);
        }

        transaction.commit();
    }

}
