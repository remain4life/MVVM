package org.remain4life.mvvm.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.BoundingBox;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.runtime.image.ImageProvider;

import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.R;
import org.remain4life.mvvm.databinding.ActivityMapBinding;
import org.remain4life.mvvm.helpers.Helper;
import org.remain4life.mvvm.viewmodels.MapViewModel;
import org.remain4life.mvvm.views.base.BaseActivity;
import org.remain4life.mvvm.views.base.IMapNavigator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends BaseActivity<ActivityMapBinding, MapViewModel>
        implements IMapNavigator {

    public static final int REQUEST_PERMISSION_LOCATION = 0;
    // Simferopol
    private static final Point CREATOR_POINT = new Point(44.952116, 34.102411);

    // marker icons
    ImageProvider creatorMarker;
    ImageProvider userMarker;
    List<PlacemarkMapObject> markers = new ArrayList<>();

    private Point userPoint;

    public static Intent createIntent(Context context){
        return new Intent(context, MapActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.title_map));

        // ask permissions
        if (!isLocationEnabled()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
        }
    }

    @Override
    public void updateUserLocation(Point newPoint) {
        userPoint = newPoint;
        initMarkers();
    }

    private void updateCamera() {
        if (userPoint == null)
            return;
        // getting BoundingBox between two points
        BoundingBox boundingBox = new BoundingBox(userPoint, CREATOR_POINT);
        // getting CameraPosition
        CameraPosition cameraPosition = binding.mapView.getMap().cameraPosition(boundingBox);
        // correct default zoom to show both markers
        cameraPosition = new CameraPosition(cameraPosition.getTarget(), cameraPosition.getZoom() - 0.8f, cameraPosition.getAzimuth(), cameraPosition.getTilt());

        // move camera
        binding.mapView.getMap()
                .move(cameraPosition,
                        new Animation(Animation.Type.SMOOTH, 0f),
                        null);
    }

    /**
     * Inits markers for user and creator
     */
    private void initMarkers() {
        if (creatorMarker == null) {
            creatorMarker = ImageProvider.fromBitmap(
                    Helper.drawableToBitmap(R.drawable.ic_marker_creator)
            );
        }

        if (userMarker == null) {
            userMarker = ImageProvider.fromBitmap(
                    Helper.drawableToBitmap(R.drawable.ic_marker_user)
            );
        }

        // if we have 1 or 0 markers - re-add them, otherwise do nothing
        if (markers.size() < 2) {
            // clear markers
            binding.mapView.getMap().getMapObjects().clear();
            markers.clear();

            // add markers
            PlacemarkMapObject creatorPlacemark = binding.mapView.getMap().getMapObjects().addPlacemark(CREATOR_POINT, creatorMarker);
            markers.add(creatorPlacemark);
            if (userPoint != null) {
                PlacemarkMapObject userPlacemark = binding.mapView.getMap().getMapObjects().addPlacemark(userPoint, userMarker);
                markers.add(userPlacemark);
            }

            updateCamera();
        }
    }

    @Override
    public void bind(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(getString(R.string.map_key));
        MapKitFactory.initialize(this);
        super.bind(savedInstanceState);
    }

    @Override
    public MapViewModel onCreateViewModel(@Nullable Bundle savedInstanceState) {
        return new MapViewModel(getApplicationContext(), this);
    }

    @Override
    public int getVariable() {
        return BR.mapViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.backToMain) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    public void setTitle(String title) {
        Objects.requireNonNull(
                getSupportActionBar()
        ).setTitle(getString(R.string.app_name) + " " + title);
    }

    @Override
    public boolean isLocationEnabled() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}

