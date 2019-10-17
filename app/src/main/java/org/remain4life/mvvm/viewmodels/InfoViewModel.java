package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;

import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.base.IInfoNavigator;

public class InfoViewModel extends BaseViewModel<IInfoNavigator> {
    public InfoViewModel(@NonNull Context context, IInfoNavigator navigator) {
        super(context, navigator);
    }

    public void onButtonClicked() {
        navigator.onButtonClicked();
    }
}
