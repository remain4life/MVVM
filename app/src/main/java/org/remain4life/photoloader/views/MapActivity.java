package org.remain4life.photoloader.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.geometry.BoundingBox;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.geometry.SubpolylineHelper;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.transport.TransportFactory;
import com.yandex.mapkit.transport.masstransit.MasstransitOptions;
import com.yandex.mapkit.transport.masstransit.MasstransitRouter;
import com.yandex.mapkit.transport.masstransit.Route;
import com.yandex.mapkit.transport.masstransit.Section;
import com.yandex.mapkit.transport.masstransit.SectionMetadata;
import com.yandex.mapkit.transport.masstransit.Session;
import com.yandex.mapkit.transport.masstransit.TimeOptions;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

import org.remain4life.photoloader.BR;
import org.remain4life.photoloader.R;
import org.remain4life.photoloader.databinding.ActivityMapBinding;
import org.remain4life.photoloader.helpers.Helper;
import org.remain4life.photoloader.viewmodels.MapViewModel;
import org.remain4life.photoloader.views.base.BaseActivity;
import org.remain4life.photoloader.views.base.IMapNavigator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends BaseActivity<ActivityMapBinding, MapViewModel>
        implements IMapNavigator, Session.RouteListener {

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
            creatorPlacemark.addTapListener((mapObject, point) -> {
                Toast.makeText(getApplicationContext(), R.string.map_creator_location, Toast.LENGTH_LONG).show();
                return true;
            });
            markers.add(creatorPlacemark);
            if (userPoint != null) {
                PlacemarkMapObject userPlacemark = binding.mapView.getMap().getMapObjects().addPlacemark(userPoint, userMarker);
                userPlacemark.addTapListener((mapObject, point) -> {
                    Toast.makeText(getApplicationContext(), R.string.map_user_location, Toast.LENGTH_LONG).show();
                    return true;
                });
                markers.add(userPlacemark);
            }

            // create route
            MasstransitOptions options = new MasstransitOptions(
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new TimeOptions());
            List<RequestPoint> points = new ArrayList<RequestPoint>();
            points.add(new RequestPoint(userPoint, RequestPointType.WAYPOINT, null));
            points.add(new RequestPoint(CREATOR_POINT, RequestPointType.WAYPOINT, null));
            MasstransitRouter mtRouter = TransportFactory.getInstance().createMasstransitRouter();
            mtRouter.requestRoutes(points, options, this);

            updateCamera();
        }
    }

    @Override
    public void bind(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(getString(R.string.map_key));
        MapKitFactory.initialize(this);
        TransportFactory.initialize(this);
        super.bind(savedInstanceState);
    }

    @Override
    public MapViewModel onCreateViewModel(@Nullable Bundle savedInstanceState) {
        return new MapViewModel(this, this);
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

    @Override
    public void onMasstransitRoutes(@NonNull List<Route> routes) {
        // we consider first alternative route only
        if (routes.size() > 0) {
            // draw route
            for (Section section : routes.get(0).getSections()) {
                drawSection(
                        section.getMetadata().getData(),
                        SubpolylineHelper.subpolyline(
                                routes.get(0).getGeometry(), section.getGeometry()));
            }
        }
    }

    @Override
    public void onMasstransitRoutesError(@NonNull Error error) {
        onError(error.toString());
    }

    private void drawSection(SectionMetadata.SectionData data,
                             Polyline geometry) {
        // Draw a section polyline on a map
        // Set its color depending on the information which the section contains
        PolylineMapObject polylineMapObject = binding.mapView.getMap().getMapObjects().addCollection().addPolyline(geometry);
        // draw transport lines in primary color
        polylineMapObject.setStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRoute));
    }
}