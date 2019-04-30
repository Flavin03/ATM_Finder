package com.robosoft.atm_finder.directions.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StartLocation implements Parcelable
{
    @SerializedName("lat") public double lat;
    @SerializedName("lng") public double lng;

    protected StartLocation(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StartLocation> CREATOR = new Creator<StartLocation>() {
        @Override
        public StartLocation createFromParcel(Parcel in) {
            return new StartLocation(in);
        }

        @Override
        public StartLocation[] newArray(int size) {
            return new StartLocation[size];
        }
    };
}
