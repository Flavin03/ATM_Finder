package com.robosoft.atm_finder.directions.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class EndLocation implements Parcelable
{
    @SerializedName("lat") public double lat;
    @SerializedName("lng") public double lng;

    protected EndLocation(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<EndLocation> CREATOR = new Creator<EndLocation>() {
        @Override
        public EndLocation createFromParcel(Parcel in) {
            return new EndLocation(in);
        }

        @Override
        public EndLocation[] newArray(int size) {
            return new EndLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }
}