package org.remain4life.mvvm.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface EntitiesDao {
    // get all new photos from DB for Photos screen
    @Query("SELECT * FROM photos WHERE isNew = 1 ORDER BY id")
    Single<List<PhotoItemEntity>> loadNewPhotos();

    // get photos from DB for Favourites screen
    @Query("SELECT * FROM photos WHERE isFavourite = 1 ORDER BY id")
    Single<List<PhotoItemEntity>> loadFavourites();

    // get one photo from DB by id
    @Query("SELECT * FROM photos WHERE serverId = :serverId LIMIT 1")
    Single<PhotoItemEntity> loadPhotoById(String serverId);

    // insert new PhotoItems if there are no such entities
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long[] insert(List<PhotoItemEntity> photoItems);

    // update existing photos as old
    @Query("UPDATE photos SET isNew = 0")
    int setPhotosOld();

    // update photo as favourite or not by id
    @Query("UPDATE photos SET isFavourite = :isFavourite WHERE serverId = :serverId")
    int setPhotoFavourite(String serverId, boolean isFavourite);

    // clear DB from all photos except favourites
    @Query("DELETE FROM photos WHERE isFavourite = 0")
    int removePhotos();
}
