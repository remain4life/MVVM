package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.PhotosNavigator;

public class PhotosViewModel extends BaseViewModel<PhotosNavigator> {
    public PhotosViewModel(@NonNull Context context, PhotosNavigator navigator) {
        super(context, navigator);
    }
}
