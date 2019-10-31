package org.remain4life.photoloader.views.base;

import org.remain4life.photoloader.model.PhotoItem;

public interface IPhotoViewerNavigator extends INavigator {
    PhotoItem getPhotoItemFromIntent();
    void showSavedFileDialog(String fileAddress);
    void requestWritePermission();
    boolean isWriteEnabled();
}
