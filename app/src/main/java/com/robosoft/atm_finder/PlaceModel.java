package com.robosoft.atm_finder;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class PlaceModel implements Parcelable {

    public String id;

    public String name;

    public String place_id;

    public String reference;

    public String vicinity;

    public String type;

    public Geometry geometry;

    public int distanceFrom;

    protected PlaceModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        place_id = in.readString();
        reference = in.readString();
        vicinity = in.readString();
        type = in.readString();
        distanceFrom = in.readInt();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public int getDistanceFrom() {
        return distanceFrom;
    }

    public void setDistanceFrom(int distanceFrom) {
        this.distanceFrom = distanceFrom;
    }

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
    }

    public class Geometry
    {
        public Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public class Location
    {
        public double lat;

        public double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }

}
