package org.remain4life.mvvm.views.base;

import org.remain4life.mvvm.model.PhotoItem;

public interface IPhotoViewerNavigator extends INavigator {
    PhotoItem getPhotoItemFromIntent();
}
