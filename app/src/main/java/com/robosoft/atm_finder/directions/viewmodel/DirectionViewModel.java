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
import com.robosoft.atm_finder.network.ApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private PlaceModel placeModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public DirectionViewModel(@NonNull Context context) {
        this.context = context;
        this.directionsList = new ArrayList<>();
        //progressBar = new ObservableInt(View.GONE);
        progressBar = new ObservableInt(View.VISIBLE);
        directionsRecycler = new ObservableInt(View.GONE);
    }

    public void fetchDirectionsList(String directionURL, PlaceModel placeModel) {
        this.placeModel = placeModel;

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
        Collections.sort(DirectionsList);

        Comparator<Directions> compareById = (Directions o1, Directions o2) -> o1.legs.get(0).steps.get(0).distance.value.compareTo( o2.legs.get(0).steps.get(0).distance.value );

        Collections.sort(DirectionsList, compareById);

        directionsList.addAll(DirectionsList);
        setChanged();
        notifyObservers();
    }

    public String getPlaceName() {
        return placeModel.name;
    }

    public String getPlaceDetail() {
        return placeModel.vicinity;
    }

    public String getDistance() {
        if (directionsList.size()!=0) {
            return directionsList.get(0).legs.get(0).distance.text;
        }else{
            return null;
        }
    }

    public String getDuration(){
        if (directionsList.size()!=0) {
        return directionsList.get(0).legs.get(0).duration.text;
        }else{
            return null;
        }
    }

    public String getImageicon() {
        // The URL will usually come from a model (i.e Profile)
        return placeModel.filterType;
    }

    public List<Directions> getDirectionsList() {
        return directionsList;
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
