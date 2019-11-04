package org.remain4life.photoloader.views.base;

import android.text.Spanned;

public interface IPhotosNavigator extends INavigator {
    Spanned getPhotosMessage(String itemsSize, String maxPhotos);
}
