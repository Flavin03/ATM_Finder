package com.robosoft.atm_finder.directions.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.robosoft.atm_finder.databinding.ActivityDirectionsBinding;
import com.robosoft.atm_finder.directions.viewmodel.DirectionViewModel;
import com.robosoft.atm_finder.map.model.PlaceModel;
import com.robosoft.atm_finder.directions.adapter.DirectionsRecyclerViewAdapter;
import com.robosoft.atm_finder.R;
import com.robosoft.atm_finder.utils.Utility;

import java.util.Observable;
import java.util.Observer;

public class DirectionsActivity extends AppCompatActivity implements Observer {

    DirectionsRecyclerViewAdapter directionsRecyclerViewAdapter = null;
    private PlaceModel placeModel = null;
    private Location location = null;
    private String directionURL = null;
    private ActivityDirectionsBinding activityDirectionsBinding;
    private DirectionViewModel directionViewModel;
    private static final String TAG = "DirectionsActivity";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();

        getIntentValue();
        setDirectionView(activityDirectionsBinding.recyclerView);
        setUpObserver(directionViewModel);
        directionViewModel.fetchDirectionsList(directionURL, placeModel);

    }

    private void initDataBinding() {
        activityDirectionsBinding = DataBindingUtil.setContentView(this, R.layout.activity_directions);
        directionViewModel = new DirectionViewModel(this);
        activityDirectionsBinding.setDirectionViewModel(directionViewModel);
        activityDirectionsBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        toolbar = activityDirectionsBinding.toolbar;
        setSupportActionBar(toolbar);
    }

    private void setDirectionView(RecyclerView directionView) {
        DirectionsRecyclerViewAdapter directionsRecyclerViewAdapter = new DirectionsRecyclerViewAdapter();
        directionView.setAdapter(directionsRecyclerViewAdapter);
        directionView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public void setUpObserver(Observable observable) {
        observable.addObserver(this);
    }


    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof  DirectionViewModel) {
            directionsRecyclerViewAdapter = (DirectionsRecyclerViewAdapter) activityDirectionsBinding.recyclerView.getAdapter();
            directionViewModel = (DirectionViewModel) o;
            directionsRecyclerViewAdapter.setDirectionList(directionViewModel.getDirectionsList());
            activityDirectionsBinding.textviewDistance.setText(directionViewModel.getDistance());
            activityDirectionsBinding.textviewDuration.setText(directionViewModel.getDuration());
        }
    }

    public void getIntentValue() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("placeModel")) {
            placeModel = intent.getExtras().getParcelable("placeModel");
        }
        if (intent != null && intent.hasExtra("mLastKnownLocation")) {
            location = intent.getParcelableExtra("mLastKnownLocation");
        }
        if(placeModel!= null && location!=null)
        {
            directionURL = Utility.requestDirections(location,placeModel,this);
            Log.d(TAG,"directionURL : "+directionURL);
        }

    }

   /* private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

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

        *//*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*//*
        return sortedMap;
    }*/

    @Override protected void onDestroy() {
        super.onDestroy();
        directionViewModel.reset();
    }

}
