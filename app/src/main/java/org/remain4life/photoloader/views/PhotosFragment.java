package org.remain4life.photoloader.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import org.remain4life.photoloader.BR;
import org.remain4life.photoloader.R;
import org.remain4life.photoloader.adapters.PhotosRecyclerViewAdapter;
import org.remain4life.photoloader.databinding.FragmentPhotosBinding;
import org.remain4life.photoloader.helpers.Application;
import org.remain4life.photoloader.viewmodels.PhotosViewModel;
import org.remain4life.photoloader.views.base.BaseFragment;
import org.remain4life.photoloader.views.base.IPhotosNavigator;

import java.util.Objects;

public class PhotosFragment extends BaseFragment<FragmentPhotosBinding, PhotosViewModel> implements IPhotosNavigator {

    private PhotosRecyclerViewAdapter adapter;

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
        adapter = new PhotosRecyclerViewAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Spanned getPhotosMessage(String itemsSize, String maxPhotos) {
        Context context = Application.getApplication().getApplicationContext();
        String html = String.format(context.getString(R.string.title_photos_loaded_html),
                itemsSize,
                maxPhotos);

        return Html.fromHtml(html);
    }

    @Override
    public void update() {
        adapter.notifyDataSetChanged();
        super.update();
    }
}