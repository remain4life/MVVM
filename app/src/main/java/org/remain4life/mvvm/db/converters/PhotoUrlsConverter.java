package org.remain4life.mvvm.db.converters;

import android.arch.persistence.room.TypeConverter;

import org.remain4life.mvvm.helpers.Application;
import org.remain4life.mvvm.model.PhotoUrls;

public class PhotoUrlsConverter {
    @TypeConverter
    public static PhotoUrls toPhotoUrls(String data) {
        return Application.GSON.fromJson(data, PhotoUrls.class);
    }

    @TypeConverter
    public static String fromPhotoUrls(PhotoUrls photoUrls) {
        return Application.GSON.toJson(photoUrls);
    }
}
