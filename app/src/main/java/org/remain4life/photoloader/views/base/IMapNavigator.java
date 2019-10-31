package org.remain4life.photoloader.views.base;

import com.yandex.mapkit.geometry.Point;

public interface IMapNavigator extends INavigator {
    void updateUserLocation(Point userPoint);
    boolean isLocationEnabled();
}
