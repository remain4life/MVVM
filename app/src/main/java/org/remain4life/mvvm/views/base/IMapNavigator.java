package org.remain4life.mvvm.views.base;

import com.yandex.mapkit.geometry.Point;

public interface IMapNavigator extends INavigator {
    void updateUserLocation(Point userPoint);
    boolean isLocationEnabled();
}
