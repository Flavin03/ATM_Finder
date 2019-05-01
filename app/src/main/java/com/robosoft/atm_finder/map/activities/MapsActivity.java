package com.robosoft.atm_finder.map.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.robosoft.atm_finder.databinding.ActivityMapsBinding;
import com.robosoft.atm_finder.directions.adapter.DirectionsRecyclerViewAdapter;
import com.robosoft.atm_finder.directions.model.Directions;
import com.robosoft.atm_finder.directions.model.Steps;
import com.robosoft.atm_finder.directions.viewmodel.DirectionViewModel;
import com.robosoft.atm_finder.map.viewmodel.DialogViewModel;
import com.robosoft.atm_finder.map.viewmodel.MapViewModel;
import com.robosoft.atm_finder.utils.ConnectivityReceiver;
import com.robosoft.atm_finder.DirectionCardListener;
import com.robosoft.atm_finder.directions.activities.DirectionsActivity;
import com.robosoft.atm_finder.utils.JSONParser;
import com.robosoft.atm_finder.map.model.PlaceModel;
import com.robosoft.atm_finder.R;
import com.robosoft.atm_finder.map.adapter.RecyclerViewAdapter;
import com.robosoft.atm_finder.utils.Utility;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Observer {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 200;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 100;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    public static Location mLastKnownLocation = null;
    boolean doubleBackToExitPressedOnce = false;
    boolean mLocationPermissionGranted = false;
    private static final String TAG = "MapsActivity";
    private ArrayList<PlaceModel> placeModelList = null;
    private RecyclerViewAdapter recyclerViewAdapter = null;
    private Polyline polyline = null;
    private String filterType = "bank";
    private boolean isMarkerClick = false;
    private boolean isBank = true;
    private String placeName = null;
    private String placeAdd = null;

    private ActivityMapsBinding mapsActivityBinding;
    private MapViewModel mapViewModel;
    private DialogViewModel dialogViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataBinding();
        setPlacesView(mapsActivityBinding.recyclerView);
        setUpObserver(mapViewModel);
        setDialogViewModel();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (!Places.isInitialized()) {
            Places.initialize(this, getResources().getString(R.string.google_maps_key));
        }

// Initialize the AutocompleteSupportFragment.

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.TYPES));
        autocompleteFragment.setCountry("IN");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                Log.i(TAG, "Place Types: " + place.getTypes());
                Log.i(TAG, "Place Types BANK: " + place.getTypes().contains("BANK"));
                mMap.clear();
                if (isAlreadySearched(place.getId())) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));
                    mMap.addMarker(new MarkerOptions().position(place.getLatLng()));
                    showMarkers(mapViewModel.getPlaceCacheMap().get(place.getId()));
                } else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));
                    mMap.addMarker(new MarkerOptions().position(place.getLatLng()));
                    if (checkConnection()) {
                        mapViewModel.fetchPlacesList(Utility.requestPlaces(place.getLatLng().latitude, place.getLatLng().longitude, filterType, MapsActivity.this), place.getLatLng(), place.getId());
                    } else {
                        Toast.makeText(MapsActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void initDataBinding() {
        mapsActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        mapViewModel = new MapViewModel(this);
        mapsActivityBinding.setMapViewModel(mapViewModel);
    }

    private void setPlacesView(RecyclerView placesView) {
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter();
        placesView.setAdapter(recyclerViewAdapter);
        placesView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    public void setUpObserver(Observable observable) {
        observable.addObserver(this);
    }


    @Override
    public void update(Observable o, Object arg) {
        Log.d(TAG, " update: " + o.toString());
        if (o instanceof MapViewModel) {
            recyclerViewAdapter = (RecyclerViewAdapter) mapsActivityBinding.recyclerView.getAdapter();
            mapViewModel = (MapViewModel) o;
            recyclerViewAdapter.setPlaceModelList(mapViewModel.getPlaceModelList());
            showMarkers(mapViewModel.getPlaceModelList());

            if (mapViewModel.getDirectionsList() != null) {
                JSONParser parser = new JSONParser();
                showDirections(parser.parse(mapViewModel.getDirectionsList()));
            }

        }
    }

    private void setDialogViewModel() {
        //dialogViewModel = new DialogViewModel(null,this);
        dialogViewModel = ViewModelProviders.of(this).get(DialogViewModel.class);
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        dialogViewModel.getFilter().observe(this, new android.arch.lifecycle.Observer<String>() {
            @Override
            public void onChanged(@Nullable final String filter) {
                // Update the UI, in this case, a TextView.
                Log.d(TAG, "onChanged filter : " + filter);
            }
        });
    }

    private boolean isAlreadySearched(String placeId) {
        for (String place : mapViewModel.getPlaceCacheMap().keySet()) {
            if (place.equalsIgnoreCase(placeId)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        final LatLng sydney = new LatLng(-33.852, 151.211);

        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 15.0f));
                            // mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude())));
                            mMap.setOnMarkerClickListener(MapsActivity.this);
                            mMap.setMyLocationEnabled(true);
                            Log.d(TAG, "Current location LatLng: " + mLastKnownLocation);
                            if (checkConnection()) {
                                mapViewModel.fetchPlacesList(Utility.requestPlaces(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), filterType, MapsActivity.this), new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), null);
                            } else {
                                Toast.makeText(MapsActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((sydney), 15.0f));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
    }

    private void showMarkers(List<PlaceModel> placeModelList) {

        for (PlaceModel placeModel : placeModelList) {
           /* mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(placeModel.geometry.location.lat,
                            placeModel.geometry.location.lng), 15.0f));*/
            mMap.addMarker(new MarkerOptions().position(new LatLng(placeModel.geometry.location.lat,
                    placeModel.geometry.location.lng)));
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClick : " + marker.getPosition());
        isMarkerClick = true;

        LatLng startLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
        LatLng endLatLng = marker.getPosition();

        // Getting URL to the Google Directions API
        ///String url = getDirectionsUrl(startLatLng, endLatLng);
        String url = Utility.getDirectionUrl(startLatLng, endLatLng, this);

        if (checkConnection()) {
            mapViewModel.fetchDirectionsList(url);
        } else {
            Toast.makeText(MapsActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void showDirections(List<LatLng> latLngList) {
        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.addAll(latLngList);
        lineOptions.width(14);
        // lineOptions.startCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow), 10));
        lineOptions.startCap(new RoundCap());
        lineOptions.endCap(new RoundCap());
        lineOptions.color(Color.BLUE);

        if (polyline != null) {
            polyline.remove();
        }
        polyline = mMap.addPolyline(lineOptions);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewModel.reset();
    }

}
