package com.robosoft.atm_finder;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
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
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, DirectionCardListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 200;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 100;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastKnownLocation = null;
    boolean doubleBackToExitPressedOnce = false;
    boolean mLocationPermissionGranted = false;
    private String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String TAG = "MapsActivity";
    private ArrayList<PlaceModel> placeModelList = null;
    private RecyclerView recyclerView = null;
    private RecyclerViewAdapter recyclerViewAdapter = null;
    private Polyline polyline = null;
    private ImageView filter_image = null;
    private String filterString = "bank";
    private boolean isMarkerClick = false;
    private String placeName = null;
    private String placeAdd = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        recyclerView = findViewById(R.id.recycler_view);

        filter_image = findViewById(R.id.filter_image);
        filter_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// custom dialog
                final Dialog dialog = new Dialog(MapsActivity.this);
                dialog.setContentView(R.layout.filter_dialog);
                TextView textview_bank = (TextView) dialog.findViewById(R.id.textview_bank);
                textview_bank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterString = "bank";
                        mMap.clear();
                        requestPlaces(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), filterString);
                        dialog.dismiss();
                    }
                });

                TextView textview_atm = (TextView) dialog.findViewById(R.id.textview_atm);
                textview_atm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterString = "atm";
                        mMap.clear();
                        requestPlaces(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), filterString);
                        dialog.dismiss();
                    }
                });

                TextView textview_cancel = (TextView) dialog.findViewById(R.id.textview_cancel);
                textview_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (!Places.isInitialized()) {
            Log.d(TAG, getResources().getString(R.string.google_maps_key));
            Places.initialize(this, getResources().getString(R.string.google_maps_key));
            PlacesClient placesClient = Places.createClient(this);
        }

        Log.d(TAG, "Places.initialize : " + Places.isInitialized());

// Initialize the AutocompleteSupportFragment.

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.TYPES));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                Log.i(TAG, "Place Types: " + place.getTypes());
                Log.i(TAG, "Place Types BANK: " + place.getTypes().contains("BANK"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()));

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


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
                            mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude())));
                            mMap.setOnMarkerClickListener(MapsActivity.this);
                            mMap.setMyLocationEnabled(true);
                            Log.d(TAG, "Current location LatLng: " + mLastKnownLocation);

                            if(checkConnection()) {
                                requestPlaces(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), filterString);
                            }else {
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

    private void requestPlaces(double lat, double lng, String type) {
        String apiKey = getResources().getString(R.string.google_maps_key);
        String requestUrl = baseUrl + "location=" + lat + "," + lng + "&rankby=distance&type=" + type + "&keyword=" + type + "&key=" + apiKey;
        Log.d(TAG, "requestUrl: " + requestUrl);
        NearbySearchAsync nearbySearchAsync = new NearbySearchAsync(requestUrl);
        nearbySearchAsync.execute();
    }

    private void showNearBanks_atms(String response) {
        Log.d(TAG, "response: " + response);
        placeModelList = new ArrayList<>();
        BitmapDescriptor icon = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.get("status").toString();
            if (status.equalsIgnoreCase("OK")) {
                JSONArray resultJsonObject = jsonObject.getJSONArray("results");

                if (resultJsonObject.length() != 0 && resultJsonObject.length() >= 2) {
                    for (int i = 0; i < 3; i++) {

                        Gson gson = new Gson();
                        PlaceModel placeModel = gson.fromJson(resultJsonObject.get(i).toString(), PlaceModel.class);
                        Location location = new Location("");
                        location.setLatitude(placeModel.getGeometry().getLocation().getLat());
                        location.setLongitude(placeModel.getGeometry().getLocation().getLng());
                        placeModel.setDistanceFrom((int) mLastKnownLocation.distanceTo(location));
                        placeModel.setType(filterString);
                        Log.d(TAG, "placeModel name: " + placeModel.getName());
                        Log.d(TAG, "placeModel name: " + placeModel.getDistanceFrom());
                        Log.d(TAG, "placeModel location.lat: " + placeModel.getGeometry().getLocation().getLat());
                        placeModelList.add(placeModel);

                        recyclerViewAdapter = new RecyclerViewAdapter(this, placeModelList, this);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(recyclerViewAdapter);

                        if (filterString.equalsIgnoreCase("bank")) {
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.bank_icn);
                        } else {
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.atm_icn);
                        }
                        mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude())));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(icon)
                                .title(placeModel.getName()));

                    }
                }
            } else {
                Toast.makeText(this, "" + status, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
        Log.d(TAG, "onMarkerClick : " + marker.getTitle());
        isMarkerClick = true;

        LatLng startLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
        LatLng endLatLng = marker.getPosition();

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(startLatLng, endLatLng);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        return false;
    }

    @Override
    public void onDirectionClick(int position) {
        Log.d(TAG, "position : " + position);
        placeModelList.get(position);
        LatLng startLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
        LatLng endLatLng = new LatLng(placeModelList.get(position).getGeometry().getLocation().getLat(),
                placeModelList.get(position).getGeometry().getLocation().getLng());

        placeName = placeModelList.get(position).getName();
        placeAdd = placeModelList.get(position).getVicinity();

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(startLatLng, endLatLng);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    private class NearbySearchAsync extends AsyncTask<Object, String, String> {

        String url = null;
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        public NearbySearchAsync(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Object... objects) {
            String inputLine;
            String result;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(url);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                result = null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String response) {
            showNearBanks_atms(response);
        }
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String apiKey = getResources().getString(R.string.google_maps_key);
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + apiKey;
        ;
        Log.d(TAG, "DirectionsUrl :  " + url);
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Background Task", "Download result: " + result);

            if (isMarkerClick) {
                ParserTask parserTask = new ParserTask();
                // Invokes the thread for parsing the JSON data
                parserTask.execute(result);
                isMarkerClick = false;
            } else {
                Intent intent = new Intent(MapsActivity.this, DirectionsActivity.class);
                intent.putExtra("DirectionResult",result);
                intent.putExtra("filterString",filterString);
                intent.putExtra("placeName",placeName);
                intent.putExtra("placeAdd",placeAdd);
                startActivity(intent);
            }
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions

                lineOptions.addAll(points);
                lineOptions.width(14);
                // lineOptions.startCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow), 10));
                lineOptions.startCap(new RoundCap());
                lineOptions.endCap(new RoundCap());
                lineOptions.color(Color.BLUE);
            }

            if (polyline != null) {
                polyline.remove();
            }

            polyline = mMap.addPolyline(lineOptions);


            // Drawing polyline in the Google Map for the i-th route
            // mMap.addPolyline(lineOptions);
        }
    }

}
