package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.IPhotosNavigator;

public class PhotosViewModel extends BaseViewModel<IPhotosNavigator> {
    public PhotosViewModel(@NonNull Context context, IPhotosNavigator navigator) {
        super(context, navigator);
    }
}
