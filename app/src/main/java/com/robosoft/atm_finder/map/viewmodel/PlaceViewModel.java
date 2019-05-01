package com.robosoft.atm_finder.map.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.robosoft.atm_finder.R;
import com.robosoft.atm_finder.directions.activities.DirectionsActivity;
import com.robosoft.atm_finder.map.activities.MapsActivity;
import com.robosoft.atm_finder.map.model.PlaceModel;
import com.robosoft.atm_finder.utils.ConnectivityReceiver;
import com.robosoft.atm_finder.utils.Utility;

public class PlaceViewModel extends BaseObservable {

    private PlaceModel placeModel;
    private Context context;

    public PlaceViewModel(PlaceModel placeModel, Context context) {
        this.placeModel = placeModel;
        this.context = context;
    }

    public String getPlaceName() {
        return placeModel.name;
    }

    public String getPlaceDetail() {
        return placeModel.vicinity;
    }

    public int getPlaceDistance() {
        return placeModel.distanceFrom;
    }

    public void onItemClick(View v) {
        //context.startActivity(UserDetailActivity.fillDetail(v.getContext(), user));
        System.out.println("Directions clicked");
        if (ConnectivityReceiver.isConnected()) {
            Intent intent = new Intent(context, DirectionsActivity.class);
            intent.putExtra("placeModel", placeModel);
            intent.putExtra("mLastKnownLocation", MapsActivity.mLastKnownLocation);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public String getImageicon() {
        // The URL will usually come from a model (i.e Profile)
        return placeModel.filterType;
    }

    public void setPlace(PlaceModel placeModel) {
        this.placeModel = placeModel;
        notifyChange();
    }

}
