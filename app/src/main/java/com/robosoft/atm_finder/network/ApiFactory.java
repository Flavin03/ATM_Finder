package com.robosoft.atm_finder.network;

import com.robosoft.atm_finder.utils.Constant;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Ahmad Shubita on 8/1/17.
 */

public class ApiFactory {

    public static ApiService create() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.BASE_URL_PLACE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

}
