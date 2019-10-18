package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.R;
import org.remain4life.mvvm.model.PhotoItem;
import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.base.IPhotoViewerNavigator;

public class PhotoViewerViewModel extends BaseViewModel<IPhotoViewerNavigator> {

    private PhotoItem photo;

    public PhotoViewerViewModel(@NonNull Context context, IPhotoViewerNavigator navigator) {
        super(context, navigator);
        setPhoto(navigator.getPhotoItemFromIntent());
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
     * Adds or removes photon from favourites
     */
    public void addToFavourites() {
        boolean newFlag = !photo.isFavourite();
        photo.setFavourite(newFlag);

        // TODO write to DB
    }
}
