package com.robosoft.atm_finder.utils;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.robosoft.atm_finder.R;
import com.robosoft.atm_finder.map.model.PlaceModel;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List<PlaceModel> calculateDistance(LatLng latLng, List<PlaceModel> placesArrayList) {

        List<PlaceModel> placeModelArrayList = new ArrayList<>();
        if (placesArrayList != null && placesArrayList.size() > 0)
            placeModelArrayList = reduceSize(placesArrayList);

        for (PlaceModel placeModel : placeModelArrayList) {
            Location loc = new Location("");
            loc.setLatitude(placeModel.geometry.location.lat);
            loc.setLongitude(placeModel.geometry.location.lng);
            placeModel.distanceFrom = (int) SphericalUtil.computeDistanceBetween(latLng, new LatLng(loc.getLatitude(),loc.getLongitude()));
        }
        return placeModelArrayList;
    }

    public static List<PlaceModel> reduceSize(List<PlaceModel> placesArrayList) {
        List<PlaceModel> placeModelArrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            PlaceModel placeModel = placesArrayList.get(i);
            placeModelArrayList.add(placeModel);
        }
        return placeModelArrayList;
    }

    public static String requestPlaces(double lat, double lng, String type, Context context) {
        String apiKey = context.getResources().getString(R.string.google_maps_key);
        String requestUrl = "json?location=" + lat + "," + lng + "&rankby=distance&type=" + type + "&keyword=" + type + "&key=" + apiKey;
        Log.d("Utility", "requestUrl: " + requestUrl);
       /* NearbySearchAsync nearbySearchAsync = new NearbySearchAsync(requestUrl);
        nearbySearchAsync.execute();*/
        return requestUrl;
    }

    public static String requestDirections(Location location, PlaceModel placeModel, Context context) {

        LatLng startLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng endLatLng = new LatLng(placeModel.geometry.location.lat,
                placeModel.geometry.location.lng);

        return getDirectionUrl(startLatLng, endLatLng, context);
    }

    public static String getDirectionUrl(LatLng startLatLng, LatLng endLatLng, Context context){

        String apiKey = context.getResources().getString(R.string.google_maps_key);
        // Origin of route
        String str_origin = "origin=" + startLatLng.latitude + "," + startLatLng.longitude;

        // Destination of route
        String str_dest = "destination=" + endLatLng.latitude + "," + endLatLng.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        String travelMode = "&mode=walking";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + travelMode;

        // Building the url to the web service
        String url = Constant.BASE_URL_DIRECTION+"json?" + parameters + "&key=" + apiKey;

        return url;
    }

}
