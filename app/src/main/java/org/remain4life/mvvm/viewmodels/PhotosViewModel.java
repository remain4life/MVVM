package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.helpers.ConnectivityStatus;
import org.remain4life.mvvm.model.PhotoRepository;
import org.remain4life.mvvm.viewmodels.base.BasePhotoModel;
import org.remain4life.mvvm.views.base.IPhotosNavigator;

import java.util.ArrayList;

public class PhotosViewModel extends BasePhotoModel<IPhotosNavigator> {

    private PhotoRepository photoRepo;

    public PhotosViewModel(@NonNull Context context, IPhotosNavigator navigator) {
        super(context, navigator);
        photoRepo = PhotoRepository.getInstance();

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
}
