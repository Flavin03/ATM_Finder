package com.robosoft.atm_finder;

import android.app.Application;
import android.content.Context;

import com.robosoft.atm_finder.network.ApiFactory;
import com.robosoft.atm_finder.network.ApiService;
import com.robosoft.atm_finder.utils.ConnectivityReceiver;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private ApiService apiService;
    private Scheduler scheduler;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    private static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public static MyApplication create(Context context) {
        return MyApplication.get(context);
    }

    public ApiService getPlaceService() {
        if (apiService == null) {
            apiService = ApiFactory.create();
        }

        return apiService;
    }

    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }

}