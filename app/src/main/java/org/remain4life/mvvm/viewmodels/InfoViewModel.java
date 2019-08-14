package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.InfoNavigator;

public class InfoViewModel extends BaseViewModel<InfoNavigator> {
    public InfoViewModel(@NonNull Context context, InfoNavigator navigator) {
        super(context, navigator);
    }

    public void onButtonClicked() {
        navigator.onButtonClicked();
    }
}
