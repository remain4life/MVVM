package org.remain4life.mvvm.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.BuildConfig;
import org.remain4life.mvvm.helpers.Application;
import org.remain4life.mvvm.helpers.PhotosQuery;
import org.remain4life.mvvm.model.PhotoItem;
import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.base.IPhotosNavigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PhotosViewModel extends BaseViewModel<IPhotosNavigator> {
    public static final int PHOTOS_NUMBER_TO_LOAD = 50;
    private static final int UNSPLASH_LOAD_LIMIT = 30;
    private int photosLoaded = 0;

    private List<PhotoItem> items;

    public PhotosViewModel(@NonNull Context context, IPhotosNavigator navigator) {
        super(context, navigator);

        loadPhotos();
    }

    /**
     * Loads photos data from Unsplash and fills the items of RecyclerView.
     * Due to API limitations (30 at a time) we call request such times as needed to load PHOTOS_NUMBER_TO_LOAD
     */
    @SuppressLint("CheckResult")
    private void loadPhotos() {
        if (photosLoaded < PHOTOS_NUMBER_TO_LOAD) {
            // calculate page number according to already loaded items
            int page = photosLoaded/UNSPLASH_LOAD_LIMIT + 1;

            PhotosQuery.loadPhotos(page, UNSPLASH_LOAD_LIMIT)
                    .subscribeOn(Schedulers.io())

                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> setLoading(true))
                    .doFinally(() -> setLoading(false))
                    .subscribe(
                            jsonResult -> {
                                // load array with PhotoItems from JsonArray
                                ArrayList<PhotoItem> tempItems = Application.GSON.fromJson(jsonResult.toString(),
                                        new TypeToken<ArrayList<PhotoItem>>() {}.getType());

                                if (BuildConfig.DEBUG) {
                                    Log.d(Application.LOG_TAG, "Data loaded from server: " + tempItems);
                                }

                                if (items == null) {
                                    items = new ArrayList<>();
                                }

                                photosLoaded += UNSPLASH_LOAD_LIMIT;

                                // if we're loading last page and loaded photos count is more hten needed,
                                // we need to calculate photos count to add
                                if (photosLoaded > PHOTOS_NUMBER_TO_LOAD) {
                                    int itemsAddCount = tempItems.size() - (photosLoaded - PHOTOS_NUMBER_TO_LOAD);
                                    for (int i = 0; i < itemsAddCount; i++) {
                                        items.add(tempItems.get(i));
                                    }
                                } else {
                                    // add all loaded items
                                    items.addAll(tempItems);

                                    if (photosLoaded < PHOTOS_NUMBER_TO_LOAD) {
                                        // load photos again
                                        loadPhotos();
                                    }
                                }
                                navigator.updateAdapterData(items);
                                notifyPropertyChanged(BR.items);
                            },
                            throwable -> {
                                if (BuildConfig.DEBUG) {
                                    Log.e(Application.LOG_TAG, throwable.getMessage());
                                }
                                onError(throwable.toString());
                            }
                    );
        }
    }

    /**
     * Resets loaded photos counter and loads photos again
     */
    public void reloadAllPhotos() {
        photosLoaded = 0;
        loadPhotos();
    }

    @Bindable
    public List<PhotoItem> getItems() {
        return items;
    }

}
