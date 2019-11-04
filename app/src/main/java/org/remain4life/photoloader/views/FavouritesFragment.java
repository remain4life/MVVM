package org.remain4life.photoloader.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import org.remain4life.photoloader.BR;
import org.remain4life.photoloader.R;
import org.remain4life.photoloader.adapters.PhotosRecyclerViewAdapter;
import org.remain4life.photoloader.databinding.FragmentFavouritesBinding;
import org.remain4life.photoloader.viewmodels.FavouritesViewModel;
import org.remain4life.photoloader.views.base.BaseFragment;
import org.remain4life.photoloader.views.base.IFavouritesNavigator;

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
        PhotosRecyclerViewAdapter adapter = new PhotosRecyclerViewAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Spanned getFavouritesMessage(String itemsSize) {
        String html = String.format(getString(R.string.title_favourites_loaded_html),
                itemsSize);

        return Html.fromHtml(html);
    }
}
