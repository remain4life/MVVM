package org.remain4life.mvvm.helpers;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Application extends android.app.Application {
    public static final Gson GSON = new GsonBuilder()
            .create();

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
