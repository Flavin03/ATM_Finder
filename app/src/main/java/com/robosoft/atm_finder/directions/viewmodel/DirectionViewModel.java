package com.robosoft.atm_finder.directions.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.view.View;

import com.robosoft.atm_finder.MyApplication;
import com.robosoft.atm_finder.directions.model.Directions;
import com.robosoft.atm_finder.directions.model.DirectionsResponse;
import com.robosoft.atm_finder.map.model.PlaceModel;
import com.robosoft.atm_finder.map.model.PlaceResponse;
import com.robosoft.atm_finder.network.ApiService;
import com.robosoft.atm_finder.utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DirectionViewModel extends Observable {

    public ObservableInt progressBar;
    public ObservableInt directionsRecycler;

    private List<Directions> directionsList;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public DirectionViewModel(@NonNull Context context) {
        this.context = context;
        this.directionsList = new ArrayList<>();
        progressBar = new ObservableInt(View.GONE);
        directionsRecycler = new ObservableInt(View.GONE);
    }

    public void fetchDirectionsList(String directionURL) {

        MyApplication appController = MyApplication.create(context);
        ApiService apiService = appController.getPlaceService();

        Disposable disposable = apiService.fetchDirections(Constant.BASE_URL_DIRECTION)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DirectionsResponse>() {
                    @Override
                    public void accept(DirectionsResponse directionsResponse) throws Exception {
                        updateDirectionsList(directionsResponse.getDirectionsList());
                        progressBar.set(View.GONE);
                        directionsRecycler.set(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        progressBar.set(View.GONE);
                        directionsRecycler.set(View.GONE);
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void updateDirectionsList(List<Directions> DirectionsList) {
        directionsList.addAll(directionsList);
        setChanged();
        notifyObservers();
    }

    public List<Directions> getDirectionsList()
    {
        return directionsList;
    }
}
