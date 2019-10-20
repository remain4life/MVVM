package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.model.PhotoItem;
import org.remain4life.mvvm.model.PhotoRepository;
import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.base.IPhotoViewerNavigator;

public class PhotoViewerViewModel extends BaseViewModel<IPhotoViewerNavigator> {

    private PhotoItem photo;
    private PhotoRepository photoRepo;

    public PhotoViewerViewModel(@NonNull Context context, IPhotoViewerNavigator navigator) {
        super(context, navigator);
        setPhoto(navigator.getPhotoItemFromIntent());
        photoRepo = PhotoRepository.getInstance();
    }

    @Bindable
    public PhotoItem getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoItem photo) {
        this.photo = photo;
        notifyPropertyChanged(BR.photo);
    }

    /**
     * Adds or removes photo from favourites
     */
    public void addToFavourites() {
        boolean newFlag = !photo.isFavourite();
        photo.setFavourite(newFlag);
        photoRepo.cacheFavourite(photo);
    }
}
