package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.base.IFavouritesNavigator;

public class FavouritesViewModel extends BaseViewModel<IFavouritesNavigator> {
    public FavouritesViewModel(@NonNull Context context, IFavouritesNavigator navigator) {
        super(context, navigator);
    }
}
