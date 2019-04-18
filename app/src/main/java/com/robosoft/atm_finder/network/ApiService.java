package com.robosoft.atm_finder.network;

import com.robosoft.atm_finder.directions.model.DirectionsResponse;
import com.robosoft.atm_finder.map.model.PlaceResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {

    @GET
    Observable<PlaceResponse> fetchBankAtm(@Url String url);

    @GET
    Observable<DirectionsResponse> fetchDirections(@Url String url);

}
