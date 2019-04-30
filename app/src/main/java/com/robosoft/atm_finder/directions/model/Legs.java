package com.robosoft.atm_finder.directions.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Legs implements Parcelable, Comparable<Legs> {

    @SerializedName("distance")
    public Distance distance;

    @SerializedName("duration")
    public Duration duration;

    @SerializedName("end_address")
    public String end_address;

    @SerializedName("start_address")
    public String start_address;

    @SerializedName("end_location")
    public EndLocation end_location;

    @SerializedName("start_location")
    public StartLocation start_location;

    @SerializedName("steps")
    public List<Steps> steps;


    protected Legs(Parcel in) {
        distance = in.readParcelable(Distance.class.getClassLoader());
        duration = in.readParcelable(Duration.class.getClassLoader());
        end_address = in.readString();
        start_address = in.readString();
        end_location = in.readParcelable(EndLocation.class.getClassLoader());
        start_location = in.readParcelable(StartLocation.class.getClassLoader());
        steps = in.createTypedArrayList(Steps.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(distance, flags);
        dest.writeParcelable(duration, flags);
        dest.writeString(end_address);
        dest.writeString(start_address);
        dest.writeParcelable(end_location, flags);
        dest.writeParcelable(start_location, flags);
        dest.writeTypedList(steps);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Legs> CREATOR = new Creator<Legs>() {
        @Override
        public Legs createFromParcel(Parcel in) {
            return new Legs(in);
        }

        @Override
        public Legs[] newArray(int size) {
            return new Legs[size];
        }
    };

    @Override
    public int compareTo(Legs o) {
        return this.steps.get(0).distance.value.compareTo(o.steps.get(0).distance.value);
    }
}
