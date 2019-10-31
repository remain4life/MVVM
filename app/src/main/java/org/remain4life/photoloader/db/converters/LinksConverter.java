package org.remain4life.photoloader.db.converters;

import android.arch.persistence.room.TypeConverter;

import org.remain4life.photoloader.helpers.Application;
import org.remain4life.photoloader.model.Links;

public class LinksConverter {
    @TypeConverter
    public static Links toLinks(String data) {
        return Application.GSON.fromJson(data, Links.class);
    }

    @TypeConverter
    public static String fromLinks(Links links) {
        return Application.GSON.toJson(links);
    }
}
