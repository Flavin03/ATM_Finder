package com.robosoft.atm_finder.directions.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Distance implements Parcelable, Comparable<Distance> {

        @SerializedName("text")
        public String text;

        @SerializedName("value")
        public String value;

        protected Distance(Parcel in) {
            text = in.readString();
            value = in.readString();
        }

        public static final Creator<Distance> CREATOR = new Creator<Distance>() {
            @Override
            public Distance createFromParcel(Parcel in) {
                return new Distance(in);
            }

            @Override
            public Distance[] newArray(int size) {
                return new Distance[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(text);
            dest.writeString(value);
        }

    @Override
    public int compareTo(Distance o) {
        return this.value.compareTo(o.value);
    }
}
