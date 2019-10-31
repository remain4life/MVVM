package org.remain4life.mvvm.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.R;
import org.remain4life.mvvm.adapters.PhotosRecyclerViewAdapter;
import org.remain4life.mvvm.databinding.FragmentPhotosBinding;
import org.remain4life.mvvm.viewmodels.PhotosViewModel;
import org.remain4life.mvvm.views.base.BaseFragment;
import org.remain4life.mvvm.views.base.IPhotosNavigator;

import java.util.Objects;

public class PhotosFragment extends BaseFragment<FragmentPhotosBinding, PhotosViewModel> implements IPhotosNavigator {

    @Override
    public PhotosViewModel onCreateViewModel(@Nullable Bundle savedInstanceState) {
        return new PhotosViewModel(Objects.requireNonNull(getContext()), this);
    }

    @Override
    public int getVariable() {
        return BR.photosViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_photos;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        PhotosRecyclerViewAdapter adapter = new PhotosRecyclerViewAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);

        super.onViewCreated(view, savedInstanceState);
    }
}