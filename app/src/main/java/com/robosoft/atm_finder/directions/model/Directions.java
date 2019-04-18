package com.robosoft.atm_finder.directions.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Directions implements Parcelable {

    @SerializedName("routes") public Routes routes;

    @SerializedName("status") public String status;

    public String directionIcon;

    protected Directions(Parcel in) {
        status = in.readString();
        directionIcon = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(directionIcon);
    }

    public class Routes{
        @SerializedName("legs") public Legs legs;

        public class Legs{

            @SerializedName("distance") public Distance distance;

            @SerializedName("duration") public Duration duration;

            @SerializedName("steps") public Steps steps;

            public class Distance {

                @SerializedName("text") public String text;

                @SerializedName("value") public String value;

            }

            public class Duration{

                @SerializedName("text") public String text;

                @SerializedName("value") public String value;

            }

            public class Steps{

                @SerializedName("html_instructions") public String html_instructions;

                @SerializedName("maneuver") public String maneuver;

                @SerializedName("distance") public Distance distance;

                public class Distance {

                    @SerializedName("text") public String text;

                    @SerializedName("value") public String value;

                }

            }

        }
    }
}
