package org.remain4life.photoloader.helpers;

import android.content.Context;
import android.net.Uri;
import android.support.multidex.MultiDex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.remain4life.photoloader.db.AppDatabase;
import org.remain4life.photoloader.helpers.gson.UriGsonTypeAdapter;

public class Application extends android.app.Application {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Uri.class, new UriGsonTypeAdapter())
            .create();

    private static Application app;

    private AppDatabase database;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        // init DB
        database = AppDatabase.getInstance(this);
    }

    public static Application getApplication(){
        return app;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
