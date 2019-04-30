package com.robosoft.atm_finder.directions.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Directions implements Parcelable, Comparable<Directions> {

    @SerializedName("copyrights")
    public String copyrights;

    @SerializedName("summary")
    public String summary;

    @SerializedName("bounds")
    public Bounds bounds;

    @SerializedName("legs")
    public List<Legs> legs;

    @SerializedName("overview_polyline")
    public Routes.OverviewPolyline overview_polyline;

    @SerializedName("warnings")
    public List<String> warnings;

    @SerializedName("waypoint_order")
    public List<String> waypoint_order;

    @Override
    public int compareTo(Directions o) {
        return this.legs.get(0).steps.get(0).distance.value.compareTo(o.legs.get(0).steps.get(0).distance.value);
    }

    public static class OverviewPolyline implements Parcelable {
        @SerializedName("points")
        public String points;

        protected OverviewPolyline(Parcel in) {
            points = in.readString();
        }

        public static final Creator<Routes.OverviewPolyline> CREATOR = new Creator<Routes.OverviewPolyline>() {
            @Override
            public Routes.OverviewPolyline createFromParcel(Parcel in) {
                return new Routes.OverviewPolyline(in);
            }

            @Override
            public Routes.OverviewPolyline[] newArray(int size) {
                return new Routes.OverviewPolyline[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(points);
        }
    }

    protected Directions(Parcel in) {
        copyrights = in.readString();
        summary = in.readString();
        bounds = in.readParcelable(Bounds.class.getClassLoader());
        legs = in.createTypedArrayList(Legs.CREATOR);
        overview_polyline = in.readParcelable(Routes.OverviewPolyline.class.getClassLoader());
        warnings = in.createStringArrayList();
        waypoint_order = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(copyrights);
        dest.writeString(summary);
        dest.writeParcelable(bounds, flags);
        dest.writeTypedList(legs);
        dest.writeParcelable(overview_polyline, flags);
        dest.writeStringList(warnings);
        dest.writeStringList(waypoint_order);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Directions> CREATOR = new Creator<Directions>() {
        @Override
        public Directions createFromParcel(Parcel in) {
            return new Directions(in);
        }

        @Override
        public Directions[] newArray(int size) {
            return new Directions[size];
        }
    };
}
