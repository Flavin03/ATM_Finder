package com.robosoft.atm_finder.directions.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Steps implements Parcelable , Comparable<Steps>{

    @SerializedName("distance")
    public Distance distance;

    @SerializedName("duration")
    public Duration duration;

    @SerializedName("html_instructions")
    public String html_instructions;

    @SerializedName("maneuver")
    public String maneuver;

    @SerializedName("travel_mode")
    public String travel_mode;

    @SerializedName("end_location")
    public EndLocation end_location;

    @SerializedName("start_location")
    public StartLocation start_location;

    @SerializedName("polyline")
    public Polyline polyline;

    protected Steps(Parcel in) {
        distance = in.readParcelable(Distance.class.getClassLoader());
        duration = in.readParcelable(Duration.class.getClassLoader());
        html_instructions = in.readString();
        maneuver = in.readString();
        travel_mode = in.readString();
        end_location = in.readParcelable(EndLocation.class.getClassLoader());
        start_location = in.readParcelable(StartLocation.class.getClassLoader());
        polyline = in.readParcelable(Polyline.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(distance, flags);
        dest.writeParcelable(duration, flags);
        dest.writeString(html_instructions);
        dest.writeString(maneuver);
        dest.writeString(travel_mode);
        dest.writeParcelable(end_location, flags);
        dest.writeParcelable(start_location, flags);
        dest.writeParcelable(polyline, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    @Override
    public int compareTo(Steps o) {
        return this.distance.value.compareTo(o.distance.value);
    }

    public static class Polyline implements Parcelable {
        @SerializedName("points")
        public String points;

        protected Polyline(Parcel in) {
            points = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(points);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Polyline> CREATOR = new Creator<Polyline>() {
            @Override
            public Polyline createFromParcel(Parcel in) {
                return new Polyline(in);
            }

            @Override
            public Polyline[] newArray(int size) {
                return new Polyline[size];
            }
        };
    }

}