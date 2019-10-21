package org.remain4life.mvvm.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.R;
import org.remain4life.mvvm.adapters.PhotosRecyclerViewAdapter;
import org.remain4life.mvvm.databinding.FragmentFavouritesBinding;
import org.remain4life.mvvm.viewmodels.FavouritesViewModel;
import org.remain4life.mvvm.views.base.BaseFragment;
import org.remain4life.mvvm.views.base.IFavouritesNavigator;

import java.util.Objects;

public class FavouritesFragment extends BaseFragment<FragmentFavouritesBinding, FavouritesViewModel> implements IFavouritesNavigator {

    @Override
    public FavouritesViewModel onCreateViewModel(@Nullable Bundle savedInstanceState) {
        return new FavouritesViewModel(Objects.requireNonNull(getActivity()), this);
    }

    @Override
    public int getVariable() {
        return BR.favViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_favourites;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        PhotosRecyclerViewAdapter adapter = new PhotosRecyclerViewAdapter(getActivity());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);

        super.onViewCreated(view, savedInstanceState);
    }
}
