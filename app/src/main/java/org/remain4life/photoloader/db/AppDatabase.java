package org.remain4life.photoloader.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import org.remain4life.photoloader.BuildConfig;
import org.remain4life.photoloader.db.converters.LinksConverter;
import org.remain4life.photoloader.db.converters.PhotoUrlsConverter;
import org.remain4life.photoloader.db.converters.UriConverter;
import org.remain4life.photoloader.db.converters.UserConverter;

import static org.remain4life.photoloader.helpers.Constants.DB_TAG;

@Database(entities = PhotoItemEntity.class,
        version = 1,
        exportSchema = false)
@TypeConverters({UserConverter.class,
        UriConverter.class,
        LinksConverter.class,
        PhotoUrlsConverter.class
})
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "photoLoaderDb";
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (BuildConfig.DEBUG) {
                    Log.d(DB_TAG, "Creating new database instance");
                }
                instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        //only for TEST
                        //.allowMainThreadQueries()
                        .build();
            }
        }
        if (BuildConfig.DEBUG) {
            Log.d(DB_TAG, "Getting the database instance");
        }
        return instance;
    }

    //return all methods from DAO interface
    public abstract EntitiesDao entitiesDao();
}
