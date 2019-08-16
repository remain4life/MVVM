package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.FavouritesINavigator;

public class FavouritesViewModel extends BaseViewModel<FavouritesINavigator> {
    public FavouritesViewModel(@NonNull Context context, FavouritesINavigator navigator) {
        super(context, navigator);
    }
}
