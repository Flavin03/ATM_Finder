package com.robosoft.atm_finder.utils;

import android.location.Location;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.robosoft.atm_finder.R;
import com.robosoft.atm_finder.directions.model.DirectionModel;
import com.robosoft.atm_finder.directions.model.Directions;
import com.robosoft.atm_finder.map.adapter.RecyclerViewAdapter;
import com.robosoft.atm_finder.map.model.PlaceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Waleed Sarwar
 * @since September 28, 2015 9:04 AM
 */
public class JSONParser {

    /**
     * Receives a JSONObject and returns a list of lists containing latitude and longitude
     */
    private static final String TAG = "JSONParser";

    public  List<LatLng> parse(List<Directions> directions) {

        List<LatLng> latLngList = new ArrayList<>();
        List<Directions> directionsList = directions;

        try {
            for (int i = 0; i < directionsList.size(); i++) {

                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                for (int j = 0; j < directionsList.get(i).legs.size(); j++) {

                    for (int k = 0; k < directionsList.get(i).legs.get(j).steps.size(); k++) {
                        String polyline = "";

                        polyline = directionsList.get(0).legs.get(j).steps.get(k).polyline.points;

                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);

                            latLngList.add(new LatLng((((LatLng) list.get(l)).latitude),(((LatLng) list.get(l)).longitude)));
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return latLngList;
    }


    /**
     * Method to decode polyline points
     * Courtesy : jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


}