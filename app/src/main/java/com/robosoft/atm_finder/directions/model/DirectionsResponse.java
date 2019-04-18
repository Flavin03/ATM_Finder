package com.robosoft.atm_finder.directions.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectionsResponse {

    @SerializedName("routes")
    private List<Directions> directionsList;

    public List<Directions> getDirectionsList() {
        return directionsList;
    }

}
