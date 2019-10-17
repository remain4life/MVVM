package org.remain4life.mvvm.views.base;

import android.support.v4.app.Fragment;

public interface IMainNavigator extends INavigator {
    void setTitle(String title);
    void setContentFragment(Fragment fragment);
}
