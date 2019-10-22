package org.remain4life.mvvm.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.BuildConfig;
import org.remain4life.mvvm.db.AppDatabase;
import org.remain4life.mvvm.db.PhotoItemEntity;
import org.remain4life.mvvm.helpers.Application;
import org.remain4life.mvvm.helpers.ConnectivityStatus;
import org.remain4life.mvvm.helpers.PhotosQuery;
import org.remain4life.mvvm.model.PhotoItem;
import org.remain4life.mvvm.model.PhotoRepository;
import org.remain4life.mvvm.viewmodels.base.BasePhotoModel;
import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.base.IPhotosNavigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static org.remain4life.mvvm.helpers.Constants.APP_TAG;
import static org.remain4life.mvvm.helpers.Constants.DB_TAG;

public class PhotosViewModel extends BasePhotoModel<IPhotosNavigator> {

    private PhotoRepository photoRepo;

    public PhotosViewModel(@NonNull Context context, IPhotosNavigator navigator) {
        super(context, navigator);
        photoRepo = PhotoRepository.getInstance();

        reloadAllPhotos();
    }

    /**
     * Resets loaded photos counter and loads photos again accordig to connectivity status
     */
    public void reloadAllPhotos() {
        photosLoaded = 0;
        photoItems = new ArrayList<>();
        if (ConnectivityStatus.isConnected(context)) {
            photoRepo.loadPhotos(this, true);
        } else {
            photoRepo.loadPhotoItemsFromDB(this, false);
        }
    }
}
