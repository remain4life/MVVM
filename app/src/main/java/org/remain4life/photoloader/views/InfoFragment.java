package org.remain4life.photoloader.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.remain4life.photoloader.BR;
import org.remain4life.photoloader.R;
import org.remain4life.photoloader.databinding.FragmentInfoBinding;
import org.remain4life.photoloader.viewmodels.InfoViewModel;
import org.remain4life.photoloader.views.base.BaseFragment;
import org.remain4life.photoloader.views.base.IInfoNavigator;

import java.util.Objects;

public class InfoFragment extends BaseFragment<FragmentInfoBinding, InfoViewModel> implements IInfoNavigator {
    @Override
    public InfoViewModel onCreateViewModel(@Nullable Bundle savedInstanceState) {
        return new InfoViewModel(Objects.requireNonNull(getContext()), this);
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
        Intent mapIntent = MapActivity.createIntent(getContext());
        startActivity(mapIntent);
    }
}
