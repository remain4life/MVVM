package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.model.PhotoRepository;
import org.remain4life.mvvm.viewmodels.base.BasePhotoModel;
import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.base.IFavouritesNavigator;

import java.util.ArrayList;

public class FavouritesViewModel extends BasePhotoModel<IFavouritesNavigator> {

    private PhotoRepository photoRepo;

    public FavouritesViewModel(@NonNull Context context, IFavouritesNavigator navigator) {
        super(context, navigator);
        photoRepo = PhotoRepository.getInstance();

        reloadFavourites();
    }

    /**
     * Reloads all favourite photos from DB
     */
    public void reloadFavourites() {
        photosLoaded = 0;
        photoItems = new ArrayList<>();
        photoRepo.loadPhotoItemsFromDB(this, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadFavourites();
    }
}
