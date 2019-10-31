package org.remain4life.photoloader.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.yandex.mapkit.geometry.Point;

import org.remain4life.photoloader.helpers.PreferencesCache;
import org.remain4life.photoloader.viewmodels.base.BaseViewModel;
import org.remain4life.photoloader.views.MapActivity;
import org.remain4life.photoloader.views.base.IMapNavigator;

public class MapViewModel extends BaseViewModel<IMapNavigator> {

    // variables for Fused Location Provider
    private boolean isLocationListenerRegistered = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    public MapViewModel(@NonNull Context context, IMapNavigator navigator) {
        super(context, navigator);
        initLocationData();
    }

    /**
     * Inits google client and request for fused location provider
     */
    private void initLocationData() {

        // init google client and request for fused location provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        locationRequest = new LocationRequest()
                //.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) // GPS quality location points
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY) // optimized for battery
                .setInterval(20000) // at least once every 20 seconds
                .setFastestInterval(10000); // at most once every 10 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                Point userPoint;
                if (locationResult != null) {
                    // update user location
                    Location lastLocation = locationResult.getLastLocation();
                    userPoint = new Point(lastLocation.getLatitude(), lastLocation.getLongitude());
                    // cache location
                    PreferencesCache.setLastLatitude(lastLocation.getLatitude());
                    PreferencesCache.setLastLongitude(lastLocation.getLongitude());
                } else {
                    // get cached data
                    userPoint = new Point(PreferencesCache.getLastLatitude(), PreferencesCache.getLastLongitude());
                }

                navigator.updateUserLocation(userPoint);
            }
        };
    }

    @SuppressLint("MissingPermission")
    private void registerLocationListener() {
        if (!isLocationListenerRegistered) {
            // enable updates
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            isLocationListenerRegistered = true;
        }
    }

    private void unregisterLocationListener() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        isLocationListenerRegistered = false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MapActivity.REQUEST_PERMISSION_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                initLocationData();
                registerLocationListener();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStop() {
        super.onStop();

        unregisterLocationListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        // if we have permissions
        if (navigator.isLocationEnabled()) {
            // set last known location
            registerLocationListener();
        }

    }
}
