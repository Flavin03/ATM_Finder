package com.robosoft.atm_finder.map.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PlaceModel implements Parcelable {

    @SerializedName("id") public String id;

    @SerializedName("name") public String name;

    @SerializedName("place_id") public String place_id;

    @SerializedName("reference") public String reference;

    @SerializedName("vicinity") public String vicinity;

    @SerializedName("type") public String type;

    @SerializedName("geometry") public Geometry geometry;

    public int distanceFrom;

    public String filterType;

    public String imageicon;

    protected PlaceModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        place_id = in.readString();
        reference = in.readString();
        vicinity = in.readString();
        type = in.readString();
        distanceFrom = in.readInt();
        imageicon = in.readString();
        filterType = in.readString();
        geometry = in.readParcelable(Geometry.class.getClassLoader());
    }

    public static final Creator<PlaceModel> CREATOR = new Creator<PlaceModel>() {
        @Override
        public PlaceModel createFromParcel(Parcel in) {
            return new PlaceModel(in);
        }

        @Override
        public PlaceModel[] newArray(int size) {
            return new PlaceModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(place_id);
        dest.writeString(reference);
        dest.writeString(vicinity);
        dest.writeString(type);
        dest.writeInt(distanceFrom);
        dest.writeString(imageicon);
        dest.writeString(filterType);
        dest.writeParcelable(geometry,flags);
    }

    public static class Geometry implements Parcelable
    {
        @SerializedName("location") public Location location;

        protected Geometry(Parcel in) {
            location = in.readParcelable(Location.class.getClassLoader());
        }

        public static final Creator<Geometry> CREATOR = new Creator<Geometry>() {
            @Override
            public Geometry createFromParcel(Parcel in) {
                return new Geometry(in);
            }

            @Override
            public Geometry[] newArray(int size) {
                return new Geometry[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(location, flags);
        }
    }

    public static class Location implements Parcelable
    {
        @SerializedName("lat") public double lat;

        @SerializedName("lng") public double lng;

        protected Location(Parcel in) {
            lat = in.readDouble();
            lng = in.readDouble();
        }

        public static final Creator<Location> CREATOR = new Creator<Location>() {
            @Override
            public Location createFromParcel(Parcel in) {
                return new Location(in);
            }

            @Override
            public Location[] newArray(int size) {
                return new Location[size];
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

}
