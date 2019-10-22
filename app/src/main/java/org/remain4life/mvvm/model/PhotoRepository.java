package org.remain4life.mvvm.model;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.remain4life.mvvm.BuildConfig;
import org.remain4life.mvvm.db.AppDatabase;
import org.remain4life.mvvm.db.PhotoItemEntity;
import org.remain4life.mvvm.helpers.Application;
import org.remain4life.mvvm.helpers.Constants;
import org.remain4life.mvvm.helpers.PhotosQuery;
import org.remain4life.mvvm.viewmodels.base.BasePhotoModel;
import org.remain4life.mvvm.viewmodels.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static org.remain4life.mvvm.helpers.Constants.APP_TAG;
import static org.remain4life.mvvm.helpers.Constants.DB_TAG;
import static org.remain4life.mvvm.viewmodels.base.BasePhotoModel.PHOTOS_NUMBER_TO_LOAD;
import static org.remain4life.mvvm.viewmodels.base.BasePhotoModel.UNSPLASH_LOAD_LIMIT;

public class PhotoRepository {

    private static PhotoRepository instance;

    private AppDatabase db;

    public PhotoRepository(AppDatabase db) {
        this.db = db;
    }

    public static PhotoRepository getInstance() {
        if (instance == null) {
            instance = new PhotoRepository(Application.getApplication().getDatabase());
        }
        return instance;
    }

    /**
     * Loads photos data from Unsplash and fills the items of BasePhotoModel.
     * Due to API limitations (30 at a time) we call request such times as needed to load PHOTOS_NUMBER_TO_LOAD
     * @param model BasePhotoModel to load photos to
     * @param clearDB flag to clear DB after successful photo receiving from server
     */
    @SuppressLint("CheckResult")
    public void loadPhotos(BasePhotoModel model, boolean clearDB) {
        if (model.getPhotosLoaded() < PHOTOS_NUMBER_TO_LOAD) {
            // calculate page number according to already loaded items
            int page = model.getPhotosLoaded() / UNSPLASH_LOAD_LIMIT + 1;

            PhotosQuery.loadPhotos(page, UNSPLASH_LOAD_LIMIT)
                    .subscribeOn(Schedulers.io())

                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> model.setLoading(true))
                    .doFinally(() -> model.setLoading(false))
                    .subscribe(
                            jsonResult -> {
                                // load array with PhotoItems from JsonArray
                                ArrayList<PhotoItem> tempItems = Application.GSON.fromJson(jsonResult.toString(),
                                        new TypeToken<ArrayList<PhotoItem>>() {
                                        }.getType());

                                if (BuildConfig.DEBUG) {
                                    Log.d(APP_TAG, "Data loaded from server: " + tempItems.size());
                                }

                                // clear items on reload
                                if (model.getPhotoItems() == null) {
                                    model.setPhotoItems(new ArrayList<>());
                                }

                                model.setPhotosLoaded(model.getPhotosLoaded() + UNSPLASH_LOAD_LIMIT);

                                // if we're loading last page and loaded photos count is more then needed,
                                // we need to calculate photos count to remove from loaded results
                                if (model.getPhotosLoaded() > PHOTOS_NUMBER_TO_LOAD) {
                                    int itemsRemove = model.getPhotosLoaded() - PHOTOS_NUMBER_TO_LOAD;

                                    for (int i = 0; i < itemsRemove; i++) {
                                        tempItems.remove(tempItems.size() - 1);
                                    }
                                }
                                // add loaded items
                                model.addPhotoItems(tempItems);

                                if (clearDB) {
                                    // clear DB and load items after that
                                    clearDbAndLoad(model, tempItems);
                                } else {
                                    // simply cache items to DB
                                    loadPhotoItemsToDB(model, tempItems);
                                }

                                if (model.getPhotosLoaded() < PHOTOS_NUMBER_TO_LOAD) {
                                    // load photos again without cleaning
                                    loadPhotos(model, false);
                                }
                            },
                            throwable -> {
                                if (BuildConfig.DEBUG) {
                                    Log.e(APP_TAG, throwable.getMessage());
                                }
                                model.onError(throwable.toString());
                                // load photos from DB in error case
                                model.showToast("Loading photos from DB...");
                                model.setPhotosLoaded(0);
                                loadPhotoItemsFromDB(model, false);
                            }
                    );
        }
    }

    /**
     * Saves loaded PhotoItems to DB
     *
     * @param model BaseViewModel to update
     * @param photoItems PhotoItem's list to convert to entities' list
     */
    @SuppressLint("CheckResult")
    private void loadPhotoItemsToDB(BaseViewModel model, List<PhotoItem> photoItems) {
        // list fpr converted entities
        ArrayList<PhotoItemEntity> itemEntities = new ArrayList<>();

        for (int i = 0; i < photoItems.size(); i++) {
            PhotoItem item = photoItems.get(i);
            itemEntities.add(
                    // create new non-favourite and new entity
                    new PhotoItemEntity(
                            item.getId(),
                            item.getDescription(),
                            item.getAltDescription(),
                            false,
                            true,
                            item.getPhotoUrls(),
                            item.getLinks(),
                            item.getAuthor()
                    )
            );
        }

        // insert created entities to DB
        Observable.just(db)
                .subscribeOn(Schedulers.io())
                .map(db -> db.entitiesDao().insert(itemEntities))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> model.setLoading(true))
                .doFinally(() -> model.setLoading(false))
                .subscribe(insertedEntites -> {
                            if (BuildConfig.DEBUG) {
                                Log.d(DB_TAG, "-> Entities successfully inserted: " + insertedEntites.length + " items");
                            }
                        },
                        throwable -> model.onError(throwable.toString())
                );
    }

    /**
     * Clears DB from all photos except favourites (set them old) and loads new items
     * @param model BaseViewModel to update
     * @param photoItems PhotoItems to cache after cleaning
     */
    @SuppressLint("CheckResult")
    public void clearDbAndLoad(BaseViewModel model, List<PhotoItem> photoItems) {
        // variable to cache results
        final int[] results = new int[2];
        Observable.just(db)
                .subscribeOn(Schedulers.io())
                .map(db -> {
                    results[0] = db.entitiesDao().removePhotos();
                    return db;
                })
                .map(db -> {
                    results[1] = db.entitiesDao().setPhotosOld();
                    return db;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> model.setLoading(true))
                .doFinally(() -> model.setLoading(false))
                .subscribe(db -> {
                            if (BuildConfig.DEBUG) {
                                Log.d(DB_TAG, "-> DB successfully cleared: " + results[0] + " items, set old " + results[1]);
                            }

                            if (photoItems != null && !photoItems.isEmpty()) {
                                loadPhotoItemsToDB(model, photoItems);
                            }

                            // set existing favourites as old

                        },
                        throwable -> model.onError(throwable.toString())
                );
    }

    /**
     * Loads cached photos data from DB
     * @param model BasePhotoModel to load photos to
     * @param loadFavourites load all photos if false, otherwise - only favourites
     */
    @SuppressLint("CheckResult")
    public void loadPhotoItemsFromDB(BasePhotoModel model, boolean loadFavourites) {
        List<PhotoItem> items = new ArrayList<>();

        Single<List<PhotoItemEntity>> load;
        if (loadFavourites) {
            load = db.entitiesDao().loadFavourites();
        } else {
            load = db.entitiesDao().loadNewPhotos();
        }

        load
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> model.setLoading(true))
                .doFinally(() -> model.setLoading(false))
                .subscribe(photoEntities -> {

                            for (int i = 0; i < photoEntities.size(); i++) {
                                PhotoItemEntity entity = photoEntities.get(i);
                                items.add(new PhotoItem(
                                        entity.serverId,
                                        entity.description,
                                        entity.altDescription,
                                        entity.isFavourite,
                                        entity.photoUrls,
                                        entity.links,
                                        entity.author
                                ));
                            }

                            model.setPhotoItems(items);

                            if (BuildConfig.DEBUG) {
                                Log.d(DB_TAG, "-> " + items.size() + " entities successfully loaded from DB");
                            }
                        },
                        throwable -> model.onError(throwable.toString())
                );
    }

    /**
     * Loads PhotoItemEntity by PhotoItem from DB
     *
     * @param photoItem PhotoItem prototype of entity to load
     * @return Single representation of PhotoItemEntity
     */
    public Single<PhotoItemEntity> getPhotoFromDB(PhotoItem photoItem) {
        return db.entitiesDao().loadPhotoById(photoItem.getId());
    }

    /**
     * Updates favourites flag in DB
     * @param photoItem PhotoItem with changed favourites flag
     */
    @SuppressLint("CheckResult")
    public void cacheFavourite(PhotoItem photoItem) {
        Observable.just(db)
                .subscribeOn(Schedulers.io())
                .map(db -> db.entitiesDao()
                        .setPhotoFavourite(photoItem.getId(), photoItem.isFavourite()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (BuildConfig.DEBUG) {
                                Log.d(DB_TAG, "-> Photo " + photoItem.getId() + ", "
                                        + photoItem.getAuthor().getName() + " favourite cached: " + photoItem.isFavourite());
                            }
                        },
                        throwable -> Log.e(Constants.ERROR_TAG, throwable.toString())
                );
    }
}
