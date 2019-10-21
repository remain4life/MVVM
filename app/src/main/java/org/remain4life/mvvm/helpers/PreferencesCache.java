package org.remain4life.mvvm.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class to cache last user location data
 */
public class PreferencesCache {
    private static final String PREFERENCES_NAME = "cache";
    private static final String LAST_LATITUDE = "lastUserLatitude";
    private static final String LAST_LONGITUDE = "lastUserLongitude";

    private static SharedPreferences sharedPreferences() {
        return Application.getApplication().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor sharedPreferencesEditor() {
        return sharedPreferences().edit();
    }

    public static void setLastLatitude(double lastLatitude) {
        sharedPreferencesEditor().putString(LAST_LATITUDE, String.valueOf(lastLatitude)).apply();
    }

    public static void setLastLongitude(double lastLongitude) {
        sharedPreferencesEditor().putString(LAST_LONGITUDE, String.valueOf(lastLongitude)).apply();
    }

    public static double getLastLatitude() {
        String latitudeStr = sharedPreferences().getString(LAST_LATITUDE,null);
        double lastLatitude;
        if (latitudeStr == null) {
            lastLatitude = Constants.DEFAULT_LAT;
        } else {
            lastLatitude = Double.parseDouble(latitudeStr);
        }
        return lastLatitude;
    }

    public static double getLastLongitude() {
        String longitudeStr = sharedPreferences().getString(LAST_LONGITUDE,null);
        double lastLongitude;
        if (longitudeStr == null) {
            lastLongitude = Constants.DEFAULT_LNG;
        } else {
            lastLongitude = Double.parseDouble(longitudeStr);
        }
        return lastLongitude;
    }
}
