package org.remain4life.mvvm.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.R;
import org.remain4life.mvvm.databinding.FragmentInfoBinding;
import org.remain4life.mvvm.viewmodels.InfoViewModel;
import org.remain4life.mvvm.views.base.BaseFragment;

import java.util.Objects;

public class InfoFragment extends BaseFragment<FragmentInfoBinding, InfoViewModel> implements InfoNavigator{
    @Override
    public InfoViewModel onCreateViewModel(@Nullable Bundle savedInstanceState) {
        return new InfoViewModel(Objects.requireNonNull(getActivity()), this);
    }

    @Override
    public int getVariable() {
        return BR.infoViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_info;
    }

    @Override
    public void onButtonClicked() {
        Toast.makeText(getContext(), "Button clicked", Toast.LENGTH_SHORT).show();
    }
}
