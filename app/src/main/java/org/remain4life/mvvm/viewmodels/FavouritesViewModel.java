package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.FavouritesNavigator;

public class FavouritesViewModel extends BaseViewModel<FavouritesNavigator> {
    public FavouritesViewModel(@NonNull Context context, FavouritesNavigator navigator) {
        super(context, navigator);
    }
}
