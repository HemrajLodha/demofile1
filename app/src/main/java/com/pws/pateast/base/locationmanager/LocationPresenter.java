package com.pws.pateast.base.locationmanager;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.Route;
import com.pws.pateast.api.model.RouteMap;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.api.service.ParentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.base.locationmanager.animation.MarkerAnimation;
import com.pws.pateast.base.locationmanager.constants.FailType;
import com.pws.pateast.base.locationmanager.constants.ProcessType;
import com.pws.pateast.base.locationmanager.interpolator.LatLngInterpolator;
import com.pws.pateast.events.LocationEvent;
import com.pws.pateast.events.SocketEvent;
import com.pws.pateast.utils.Preference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pws.pateast.events.SocketEvent.EVENT_ON_CONNECT;
import static com.pws.pateast.events.SocketEvent.EVENT_ON_DISCONNECT;

public abstract class LocationPresenter<V extends LocationView> extends AppPresenter<V> {

    @Inject
    protected Preference preference;

    @Inject
    protected ServiceBuilder serviceBuilder;

    @Inject
    protected EventBus eventBus;

    @Inject
    protected GeoApiContext mGeoApiContext;

    protected User user;

    private APIService apiService;
    private V view;

    protected int markerCount;
    //protected Location mLastLocation;
    protected Marker mMarker;
    protected boolean connected;

    public abstract void onSocketEvent();

    public abstract void onLocationEvent(LocationEvent event);

    protected List<RouteMap> getRouteMaps() {
        return null;
    }

    protected void checkRouteMap(LatLng currentLatLng) {

    }

    protected void setWayPoints(String... wayPoints) {

    }

    // calculate distance matrix
    protected void calculateDistanceMatrix(DistanceMatrix matrix) {

    }

    @Override
    public V getView() {
        return view;
    }

    @Override
    public void attachView(V view) {
        this.view = view;
        getComponent().inject((LocationPresenter<LocationView>) this);
        user = preference.getUser();
        registerEvent(true);
        getView().getLocation();
    }

    public void registerEvent(boolean register) {
        if (register && !eventBus.isRegistered(this)) {
            eventBus.register(this);
        } else if (!register && eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
    }

    @Subscribe
    public void onEvent(final SocketEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                switch (event.getEventType()) {
                    case EVENT_ON_CONNECT:
                        connected = true;
                        onSocketEvent();
                        getView().hideDialog();
                        break;
                    case EVENT_ON_DISCONNECT:
                        connected = false;
                        onSocketEvent();
                        showMessage(R.string.unable_to_connect);
                        break;
                }
            }
        });

    }


    @Subscribe
    public void onEvent(final LocationEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                onLocationEvent(event);
            }
        });

    }

    protected void showMessage(@StringRes int message) {
        getView().showDialog(getString(R.string.app_name),
                getString(message),
                null,
                R.string.ok);
    }

    protected void addMarker(LatLng latLng) {
        //String appendValue = latLng.latitude + ", " + latLng.longitude;
        //Utils.showToast(getContext(), appendValue);

        addMarker(getView().getGoogleMap(), latLng);
    }

    public void onLocationChanged(Location location) {
        getView().hideProgressDialog();
    }


    public void onLocationFailed(@FailType int failType) {
        getView().hideProgressDialog();

        switch (failType) {
            case FailType.TIMEOUT: {
                getView().setError("Couldn't get location, and timeout!");
                break;
            }
            case FailType.PERMISSION_DENIED: {
                getView().setError("Couldn't get location, because user didn't give permission!");
                break;
            }
            case FailType.NETWORK_NOT_AVAILABLE: {
                getView().setError("Couldn't get location, because network is not accessible!");
                break;
            }
            case FailType.GOOGLE_PLAY_SERVICES_NOT_AVAILABLE: {
                getView().setError("Couldn't get location, because Google Play Services not available!");
                break;
            }
            case FailType.GOOGLE_PLAY_SERVICES_CONNECTION_FAIL: {
                getView().setError("Couldn't get location, because Google Play Services connection failed!");
                break;
            }
            case FailType.GOOGLE_PLAY_SERVICES_SETTINGS_DIALOG: {
                getView().setError("Couldn't display settingsApi dialog!");
                break;
            }
            case FailType.GOOGLE_PLAY_SERVICES_SETTINGS_DENIED: {
                getView().setError("Couldn't get location, because user didn't activate providers via settingsApi!");
                break;
            }
            case FailType.VIEW_DETACHED: {
                getView().setError("Couldn't get location, because in the process view was detached!");
                break;
            }
            case FailType.VIEW_NOT_REQUIRED_TYPE: {
                getView().setError("Couldn't get location, "
                        + "because view wasn't sufficient enough to fulfill given configuration!");
                break;
            }
            case FailType.UNKNOWN: {
                getView().setError("Ops! Something went wrong!");
                break;
            }
        }
    }

    public void onProcessTypeChanged(@ProcessType int newProcess) {
        switch (newProcess) {
            case ProcessType.GETTING_LOCATION_FROM_GOOGLE_PLAY_SERVICES: {
                getView().showProgressDialog("Getting Location from Google Play Services...");
                break;
            }
            case ProcessType.GETTING_LOCATION_FROM_GPS_PROVIDER: {
                getView().showProgressDialog("Getting Location from GPS...");
                break;
            }
            case ProcessType.GETTING_LOCATION_FROM_NETWORK_PROVIDER: {
                getView().showProgressDialog("Getting Location from Network...");
                break;
            }
            case ProcessType.ASKING_PERMISSIONS:
            case ProcessType.GETTING_LOCATION_FROM_CUSTOM_PROVIDER:
                // Ignored
                break;
        }
    }


    public void addMarker(GoogleMap mMap, LatLng latLng) {
        if (mMap != null && markerCount == 1) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder()
                            .target(latLng)
                            .zoom(19f)
                            .bearing((float) SphericalUtil.computeHeading(mMarker.getPosition(), latLng))
                            .build()));
            MarkerAnimation.animateMarkerToHC(mMarker, latLng, new LatLngInterpolator.SphericalInterpolator());
            //checkRouteMap(latLng);
        } else if (mMap != null && markerCount == 0) {
            //Set Custom BitMap for Pointer
            int height = 50;
            int width = 30;
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.ic_track_bus);
            Bitmap b = bitmapDrawable.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromBitmap((smallMarker))));
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder()
                            .target(latLng)
                            .zoom(16f)
                            .build()));

            //Set Marker Count to 1 after first marker is created
            markerCount = 1;
            addRouteMap(latLng);
        }
    }

    protected void getDistanceMatrix(LatLng currentLatLng) {
        String[] wayPoints = getWayPoints(currentLatLng, true);
        if (wayPoints != null) {
            getDistances(wayPoints);
        }
    }

    private void addRouteMap(LatLng currentLatLng) {
        String[] wayPoints = getWayPoints(currentLatLng, false);
        if (wayPoints != null) {
            getDirections(wayPoints);
        }
    }

    private String[] getWayPoints(LatLng currentLatLng, boolean forDistance) {
        List<RouteMap> routeMaps = getRouteMaps();
        if (routeMaps == null)
            return null;
        String[] wayPoints = new String[routeMaps.size() + 1];
        int flag = 0;
        wayPoints[flag] = String.format(Locale.ENGLISH, "%.8f,%.8f", currentLatLng.latitude, currentLatLng.longitude);
        flag++;
        for (RouteMap routeMap : routeMaps) {
            Route route = routeMap.getRouteaddress();
            LatLng position = new LatLng(route.getLat(), route.getLang());
            if (!forDistance) {
                addIcon(route.getAddress(), position);
            }
            wayPoints[flag] = String.format(Locale.ENGLISH, "%.8f,%.8f", position.latitude, position.longitude);
            flag++;
        }
        return wayPoints;
    }

    private void getDirections(String... waypoint) {
        Single<Response<DirectionsResult>> directions = Single.fromCallable(() -> getDirectionsResult(waypoint));
        directions.subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response<DirectionsResult>>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response<DirectionsResult> response) {
                        if (response.getData() != null) {
                            addPolyline(response.getData());
                            setWayPoints(waypoint);
                        } else {
                            onError(new RetrofitException(getString(R.string.no_routes)));
                        }
                    }

                    @Override
                    public LocationPresenter getPresenter() {
                        return LocationPresenter.this;
                    }
                });
    }

    private void getDistances(String... waypoint) {
        Single<Response<DistanceMatrix>> directions = Single.fromCallable(() -> getDistanceResult(waypoint));
        directions.subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response<DistanceMatrix>>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response<DistanceMatrix> response) {
                        if (response.getData() != null) {
                            calculateDistanceMatrix(response.getData());
                        } else {
                            onError(new RetrofitException(getString(R.string.no_routes)));
                        }
                    }

                    @Override
                    public LocationPresenter getPresenter() {
                        return LocationPresenter.this;
                    }
                });
    }

    private Response<DistanceMatrix> getDistanceResult(String... waypoints) {
        String origin = waypoints[0];
        String[] destinations = new String[0];
        if (waypoints.length > 1) {
            destinations = Arrays.copyOfRange(waypoints, 1, waypoints.length);
        } else {
            destinations[0] = waypoints[waypoints.length - 1];
        }

        DistanceMatrix result = null;
        Response<DistanceMatrix> response = new Response();
        try {
            result = DistanceMatrixApi.newRequest(mGeoApiContext)
                    .mode(TravelMode.DRIVING)
                    .units(Unit.METRIC)
                    .origins(origin)
                    .destinations(destinations)
                    .departureTime(new DateTime())
                    .await();
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            e.printStackTrace();
        }
        response.setStatus(result != null);
        response.setData(result);
        return response;
    }

    private Response<DirectionsResult> getDirectionsResult(String... waypoints) {
        String origin = waypoints[0];
        String destination = waypoints[waypoints.length - 1];
        DirectionsResult result = null;
        Response<DirectionsResult> response = new Response();
        try {
            result = DirectionsApi.newRequest(mGeoApiContext)
                    .mode(TravelMode.DRIVING)
                    .units(Unit.METRIC)
                    .origin(origin)
                    .destination(destination)
                    .waypoints(waypoints)
                    .departureTime(new DateTime())
                    .await();
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            e.printStackTrace();
        }
        response.setStatus(result != null);
        response.setData(result);
        return response;
    }


    private void addPolyline(DirectionsResult result) {
        List<LatLng> decodedPath = PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath());
        getView().getGoogleMap().addPolyline(new PolylineOptions().addAll(decodedPath).geodesic(true));
    }

    public void addIcon(CharSequence text, LatLng position) {
        IconGenerator iconFactory = new IconGenerator(getContext());
        iconFactory.setColor(ContextCompat.getColor(getContext(), R.color.colorAccentDriver));
        iconFactory.setColor(ContextCompat.getColor(getContext(), R.color.colorAccentDriver));
        iconFactory.setTextAppearance(getContext(), R.style.MarkerText);
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text)))
                .position(position)
                .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        getView().getGoogleMap().addMarker(markerOptions);
    }


    @Override
    public void detachView() {
        registerEvent(false);
        super.detachView();
    }

}
