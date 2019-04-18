package com.robosoft.atm_finder.map.viewmodel;

import android.content.Context;
import android.databinding.ObservableInt;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.View;

import com.robosoft.atm_finder.MyApplication;
import com.robosoft.atm_finder.map.model.PlaceModel;
import com.robosoft.atm_finder.map.model.PlaceResponse;
import com.robosoft.atm_finder.network.ApiService;
import com.robosoft.atm_finder.utils.Constant;
import com.robosoft.atm_finder.utils.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MapViewModel extends Observable {

   // public ObservableInt progressBar;
    public ObservableInt placesRecycler;

    private List<PlaceModel> placeModelList;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MapViewModel(@NonNull Context context) {
        this.context = context;
        this.placeModelList = new ArrayList<>();
      //  progressBar = new ObservableInt(View.GONE);
        placesRecycler = new ObservableInt(View.GONE);
        initializeViews();
    }

    public void initializeViews() {
        placesRecycler.set(View.GONE);
       // progressBar.set(View.VISIBLE);
        //fetchPlacesList();
    }

    public void onFilterClick(View view) {

    }

    public void fetchPlacesList(String url, final Location location) {

        MyApplication appController = MyApplication.create(context);
        ApiService apiService = appController.getPlaceService();

        Disposable disposable = apiService.fetchBankAtm(url)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlaceResponse>() {
                    @Override
                    public void accept(PlaceResponse placeResponse) throws Exception {
                        updatePlaceDataList(placeResponse.getPlaceList(), location);
                       // progressBar.set(View.GONE);
                        placesRecycler.set(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                     //   progressBar.set(View.GONE);
                        placesRecycler.set(View.GONE);
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void updatePlaceDataList(List<PlaceModel> placeList, Location location) {
       // placeModelList.addAll(placeList);
        placeModelList.addAll(Utility.calculateDistance(location, placeList));
        setChanged();
        notifyObservers();
    }

    public List<PlaceModel> getPlaceModelList()
    {
        return placeModelList;
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
