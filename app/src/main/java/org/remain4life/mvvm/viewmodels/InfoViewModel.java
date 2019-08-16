package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.InfoINavigator;

public class InfoViewModel extends BaseViewModel<InfoINavigator> {
    public InfoViewModel(@NonNull Context context, InfoINavigator navigator) {
        super(context, navigator);
    }

    public void onButtonClicked() {
        navigator.onButtonClicked();
    }
}
