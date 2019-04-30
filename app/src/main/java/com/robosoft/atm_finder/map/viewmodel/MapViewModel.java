package com.robosoft.atm_finder.map.viewmodel;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.robosoft.atm_finder.MyApplication;
import com.robosoft.atm_finder.R;
import com.robosoft.atm_finder.databinding.FilterDialogBinding;
import com.robosoft.atm_finder.directions.model.Directions;
import com.robosoft.atm_finder.directions.model.DirectionsResponse;
import com.robosoft.atm_finder.map.model.PlaceModel;
import com.robosoft.atm_finder.map.model.PlaceResponse;
import com.robosoft.atm_finder.network.ApiService;
import com.robosoft.atm_finder.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MapViewModel extends Observable {

    private static final String TAG = "MapViewModel";
    // public ObservableInt progressBar;
    public ObservableInt placesRecycler;
    public ObservableInt progressBar;
    private DialogViewModel dialogViewModel;

    private List<PlaceModel> placeModelList;
    private List<Directions> directionsList;
    public HashMap<String, List<PlaceModel>> placeCacheMap;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MapViewModel(@NonNull Context context) {
        this.context = context;
        this.placeModelList = new ArrayList<>();
        this.directionsList = new ArrayList<>();
        progressBar = new ObservableInt(View.GONE);
        placesRecycler = new ObservableInt(View.GONE);
        this.placeCacheMap = new HashMap<>();
        initializeViews();
    }

    public void initializeViews() {
        placesRecycler.set(View.GONE);
        progressBar.set(View.VISIBLE);
        //fetchPlacesList();
    }

    public void onFilterClick(View view) {
        Log.d(TAG, "onFilterClick");

        Dialog dialog = new Dialog(context);
        dialogViewModel = new DialogViewModel(dialog, context);
        FilterDialogBinding filterDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.filter_dialog, null, false);
        filterDialogBinding.setDialogViewModel(dialogViewModel);
        dialog.setContentView(filterDialogBinding.getRoot());
        dialog.show();
    }

    public void fetchPlacesList(String url, final LatLng latLng, String placeId) {

        MyApplication appController = MyApplication.create(context);
        ApiService apiService = appController.getPlaceService();

        Disposable disposable = apiService.fetchBankAtm(url)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlaceResponse>() {
                    @Override
                    public void accept(PlaceResponse placeResponse) throws Exception {
                        updatePlaceDataList(placeResponse.getPlaceList(), latLng, placeId);
                        progressBar.set(View.GONE);
                        placesRecycler.set(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        progressBar.set(View.GONE);
                        placesRecycler.set(View.GONE);
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void updatePlaceDataList(List<PlaceModel> placeList, LatLng latLng, String placeId) {
        if (placeId != null) {
            placeCacheMap.put(placeId, Utility.calculateDistance(latLng, placeList));
        }
        // placeModelList.addAll(placeList);
        placeModelList.clear();
        placeModelList.addAll(Utility.calculateDistance(latLng, placeList));
        setChanged();
        notifyObservers();
    }

    public void fetchDirectionsList(String directionURL) {
        MyApplication appController = MyApplication.create(context);
        ApiService apiService = appController.getDirectionService();

        Disposable disposable = apiService.fetchDirections(directionURL)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DirectionsResponse>() {
                    @Override
                    public void accept(DirectionsResponse directionsResponse) throws Exception {
                        updateDirectionsList(directionsResponse.getDirectionsList());
                        progressBar.set(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        progressBar.set(View.GONE);
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void updateDirectionsList(List<Directions> DirectionsList) {
        directionsList.clear();
        directionsList.addAll(DirectionsList);
        setChanged();
        notifyObservers();
    }

    public List<Directions> getDirectionsList() {
        return directionsList;
    }

    public List<PlaceModel> getPlaceModelList() {
        return placeModelList;
    }

    public HashMap<String, List<PlaceModel>> getPlaceCacheMap() {
        return placeCacheMap;
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }
}
