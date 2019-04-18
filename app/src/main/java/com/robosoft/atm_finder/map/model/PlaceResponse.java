package com.robosoft.atm_finder.map.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceResponse {

    @SerializedName("results")
    private List<PlaceModel> placeModelList;

    public List<PlaceModel> getPlaceList() {
        return placeModelList;
    }

}
