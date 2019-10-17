package org.remain4life.mvvm.helpers;

import android.content.Context;
import android.net.Uri;
import android.support.multidex.MultiDex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.remain4life.mvvm.helpers.gson.UriGsonTypeAdapter;

public class Application extends android.app.Application {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Uri.class, new UriGsonTypeAdapter())
            .create();

    public static final String LOG_TAG = "MVVM_tag";

    private static Application app;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static Application getApplication(){
        return app;
    }
}
