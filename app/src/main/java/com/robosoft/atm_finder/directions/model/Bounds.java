package com.robosoft.atm_finder.directions.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Bounds implements Parcelable
{
    @SerializedName("northeast") public Northeast northeast;
    @SerializedName("southwest") public Southwest southwest;

    protected Bounds(Parcel in) {
        northeast = in.readParcelable(Northeast.class.getClassLoader());
        southwest = in.readParcelable(Southwest.class.getClassLoader());
    }

    public static final Creator<Bounds> CREATOR = new Creator<Bounds>() {
        @Override
        public Bounds createFromParcel(Parcel in) {
            return new Bounds(in);
        }

        @Override
        public Bounds[] newArray(int size) {
            return new Bounds[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(northeast, flags);
        dest.writeParcelable(southwest, flags);
    }

    public static class Northeast implements Parcelable
    {
        @SerializedName("lat") public double lat;
        @SerializedName("lng") public double lng;

        protected Northeast(Parcel in) {
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

        public static final Creator<Northeast> CREATOR = new Creator<Northeast>() {
            @Override
            public Northeast createFromParcel(Parcel in) {
                return new Northeast(in);
            }

            @Override
            public Northeast[] newArray(int size) {
                return new Northeast[size];
            }
        };
    }

    public static class Southwest implements Parcelable
    {
        @SerializedName("lat") public double lat;
        @SerializedName("lng") public double lng;

        protected Southwest(Parcel in) {
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

        public static final Creator<Southwest> CREATOR = new Creator<Southwest>() {
            @Override
            public Southwest createFromParcel(Parcel in) {
                return new Southwest(in);
            }

            @Override
            public Southwest[] newArray(int size) {
                return new Southwest[size];
            }
        };
    }
}
