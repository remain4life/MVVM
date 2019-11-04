package org.remain4life.photoloader.viewmodels;

import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.text.Spanned;

import org.remain4life.photoloader.helpers.ConnectivityStatus;
import org.remain4life.photoloader.model.PhotoRepository;
import org.remain4life.photoloader.viewmodels.base.BasePhotoModel;
import org.remain4life.photoloader.viewmodels.base.IPhotosLoadObserver;
import org.remain4life.photoloader.views.base.IPhotosNavigator;

import java.util.ArrayList;

public class PhotosViewModel extends BasePhotoModel<IPhotosNavigator> implements IPhotosLoadObserver {

    private PhotoRepository photoRepo;

    public PhotosViewModel(@NonNull Context context, IPhotosNavigator navigator) {
        super(context, navigator);
        photoRepo = PhotoRepository.getInstance();
        photoRepo.registerObserver(this);

        reloadAllPhotos();
    }

    /**
     * Resets loaded photos counter and loads photos again accordig to connectivity status
     */
    public void reloadAllPhotos() {
        photosLoaded = 0;
        photoItems = new ArrayList<>();
        if (ConnectivityStatus.isConnected(context)) {
            photoRepo.loadPhotos(this, true);
        } else {
            photoRepo.loadPhotoItemsFromDB(this, false);
        }
    }

    public Spanned getPhotosMessage(String itemsSize, String maxPhotos) {
        return navigator.getPhotosMessage(itemsSize, maxPhotos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        photoRepo.removeObserver(this);
    }

    @Override
    public void onUserDataChanged() {
        reloadAllPhotos();
    }

    @Bindable
    public int getPhotosToLoad(){
        return photoRepo.getPhotosToLoad();
    }
}
