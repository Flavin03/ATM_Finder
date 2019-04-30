package com.robosoft.atm_finder.directions.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Geocoded_waypoints implements Parcelable
{
    @SerializedName("geocoder_status") public String geocoder_status;
    @SerializedName("place_id") public String place_id;
    @SerializedName("types") public List<String> types;

    protected Geocoded_waypoints(Parcel in) {
        geocoder_status = in.readString();
        place_id = in.readString();
        types = in.createStringArrayList();
    }

    public static final Creator<Geocoded_waypoints> CREATOR = new Creator<Geocoded_waypoints>() {
        @Override
        public Geocoded_waypoints createFromParcel(Parcel in) {
            return new Geocoded_waypoints(in);
        }

        @Override
        public Geocoded_waypoints[] newArray(int size) {
            return new Geocoded_waypoints[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(geocoder_status);
        dest.writeString(place_id);
        dest.writeStringList(types);
    }
}
