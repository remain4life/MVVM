package org.remain4life.mvvm.viewmodels.base;

import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.model.PhotoItem;
import org.remain4life.mvvm.views.base.INavigator;
import org.remain4life.mvvm.BR;

import java.util.ArrayList;
import java.util.List;

abstract public class BasePhotoModel<N extends INavigator> extends BaseViewModel<N>{
    public static final int PHOTOS_NUMBER_TO_LOAD = 50;
    public static final int UNSPLASH_LOAD_LIMIT = 30;
    protected int photosLoaded = 0;

    protected List<PhotoItem> photoItems;

    public BasePhotoModel(@NonNull Context context, N navigator) {
        super(context, navigator);
    }

    @Bindable
    public List<PhotoItem> getPhotoItems() {
        return photoItems;
    }

    public void setPhotoItems(List<PhotoItem> photoItems) {
        this.photoItems = photoItems;
        notifyPropertyChanged(BR.photoItems);
    }

    public void addPhotoItems(ArrayList<PhotoItem> newPhotoItems) {
        photoItems.addAll(newPhotoItems);
        notifyPropertyChanged(BR.photoItems);
    }

    public int getPhotosLoaded() {
        return photosLoaded;
    }

    public void setPhotosLoaded(int photosLoaded) {
        this.photosLoaded = photosLoaded;
    }
}
