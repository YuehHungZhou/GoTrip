package com.topdsr2.gotrip.addOrDeletePoint;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topdsr2.gotrip.R;

public class AddOrDeletePointFragment extends BottomSheetDialogFragment implements AddOrDeletePointContract.View{

    private AddOrDeletePointContract.Presenter mPresenter;

    public AddOrDeletePointFragment() {
    }

    public static AddOrDeletePointFragment newInstance() {
        return new AddOrDeletePointFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addordeletepoint, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadPointData();
    }

    @Override
    public void showAddOrDeleteUi() {

    }

    @Override
    public void setPresenter(AddOrDeletePointContract.Presenter presenter) {
        mPresenter = presenter;
    }




//    setStyle(STYLE_NO_FRAME,R.style.Dialog);


}
