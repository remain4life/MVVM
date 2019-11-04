package org.remain4life.photoloader.model;

import org.remain4life.photoloader.viewmodels.base.IPhotosLoadObserver;

public interface IPhotosSubject {
    void registerObserver(IPhotosLoadObserver loadObserver);
    void removeObserver(IPhotosLoadObserver loadObserver);
    void notifyObservers(int loadPhotosNumber);
}

