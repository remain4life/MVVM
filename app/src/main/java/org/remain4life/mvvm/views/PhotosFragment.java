package org.remain4life.mvvm.views;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.R;
import org.remain4life.mvvm.databinding.FragmentPhotosBinding;
import org.remain4life.mvvm.viewmodels.PhotosViewModel;
import org.remain4life.mvvm.views.base.BaseFragment;

import java.util.Objects;

public class PhotosFragment extends BaseFragment<FragmentPhotosBinding, PhotosViewModel> implements IPhotosNavigator {
    @Override
    public PhotosViewModel onCreateViewModel(@Nullable Bundle savedInstanceState) {
        return new PhotosViewModel(Objects.requireNonNull(getActivity()), this);
    }

    @Override
    public int getVariable() {
        return BR.photosViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_photos;
    }
}