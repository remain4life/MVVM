package org.remain4life.mvvm.db.converters;

import android.arch.persistence.room.TypeConverter;

import org.remain4life.mvvm.helpers.Application;
import org.remain4life.mvvm.model.User;

public class UserConverter {
    @TypeConverter
    public static User toUser(String data) {
        return Application.GSON.fromJson(data, User.class);
    }

    @TypeConverter
    public static String fromUser(User user) {
        return Application.GSON.toJson(user);
    }
}
