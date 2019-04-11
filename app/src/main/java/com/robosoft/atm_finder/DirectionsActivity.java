package com.robosoft.atm_finder;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DirectionsActivity extends AppCompatActivity {

    private String directionResult;
    private String filterString;
    ArrayList<DirectionModel> directionModelArrayList = null;
    DirectionsRecyclerViewAdapter directionsRecyclerViewAdapter = null;
    RecyclerView recyclerView = null;
    private String placeName = null;
    private String placeAdd = null;
    private TextView textview_place_name = null;
    private TextView textview_place_address = null;
    private ImageView bank_atm_icon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        recyclerView = findViewById(R.id.recycler_view);
        textview_place_name = findViewById(R.id.textview_place_name);
        textview_place_address = findViewById(R.id.textview_place_address);
        bank_atm_icon = findViewById(R.id.bank_atm_icon);

        getIntentValue();

        textview_place_name.setText(placeName);
        textview_place_address.setText(placeAdd);
        if (filterString.equalsIgnoreCase("bank")) {
            bank_atm_icon.setImageDrawable(getResources().getDrawable(R.drawable.bank_icn));
        }else {
            bank_atm_icon.setImageDrawable(getResources().getDrawable(R.drawable.atm_icn));
        }

        JSONObject jObject;
        try {
            jObject = new JSONObject(directionResult);
            DirectionsJSONParser parser = new DirectionsJSONParser();
            // Starts parsing data
            directionModelArrayList = parser.parseDirection(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(directionModelArrayList!=null && directionModelArrayList.size()>0)
        {
            directionsRecyclerViewAdapter = new DirectionsRecyclerViewAdapter(this, directionModelArrayList, filterString);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(directionsRecyclerViewAdapter);
        }

    }

    public void getIntentValue() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("DirectionResult")) {
            directionResult = intent.getStringExtra("DirectionResult");
        }
        if (intent != null && intent.hasExtra("filterString")) {
            filterString = intent.getStringExtra("filterString");
        }
        if (intent != null && intent.hasExtra("placeName")) {
            placeName = intent.getStringExtra("placeName");
        }
        if (intent != null && intent.hasExtra("placeAdd")) {
            placeAdd = intent.getStringExtra("placeAdd");
        }
    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/


        return sortedMap;
    }

}
