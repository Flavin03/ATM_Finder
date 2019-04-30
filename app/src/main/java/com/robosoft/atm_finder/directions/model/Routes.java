package com.robosoft.atm_finder.directions.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Routes implements Parcelable {

    @SerializedName("copyrights")
    public String copyrights;

    @SerializedName("summary")
    public String summary;

    @SerializedName("bounds")
    public Bounds bounds;

    @SerializedName("legs")
    public List<Legs> legs;

    @SerializedName("overview_polyline")
    public OverviewPolyline overview_polyline;

    @SerializedName("warnings")
    public List<String> warnings;

    @SerializedName("waypoint_order")
    public List<String> waypoint_order;

    protected Routes(Parcel in) {
        copyrights = in.readString();
        summary = in.readString();
        bounds = in.readParcelable(Bounds.class.getClassLoader());
        legs = in.createTypedArrayList(Legs.CREATOR);
        overview_polyline = in.readParcelable(OverviewPolyline.class.getClassLoader());
        warnings = in.createStringArrayList();
        waypoint_order = in.createStringArrayList();
    }

    public static final Creator<Routes> CREATOR = new Creator<Routes>() {
        @Override
        public Routes createFromParcel(Parcel in) {
            return new Routes(in);
        }

        @Override
        public Routes[] newArray(int size) {
            return new Routes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
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

    public static class OverviewPolyline implements Parcelable {
        @SerializedName("points")
        public String points;

        protected OverviewPolyline(Parcel in) {
            points = in.readString();
        }

        public static final Creator<OverviewPolyline> CREATOR = new Creator<OverviewPolyline>() {
            @Override
            public OverviewPolyline createFromParcel(Parcel in) {
                return new OverviewPolyline(in);
            }

            @Override
            public OverviewPolyline[] newArray(int size) {
                return new OverviewPolyline[size];
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
}