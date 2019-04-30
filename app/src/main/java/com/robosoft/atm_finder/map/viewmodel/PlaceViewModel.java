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

import com.robosoft.atm_finder.R;
import com.robosoft.atm_finder.directions.activities.DirectionsActivity;
import com.robosoft.atm_finder.map.activities.MapsActivity;
import com.robosoft.atm_finder.map.model.PlaceModel;

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

    public void onItemClick(View v){
        //context.startActivity(UserDetailActivity.fillDetail(v.getContext(), user));
        System.out.println("Directions clicked");

        Intent intent = new Intent(context, DirectionsActivity.class);
        intent.putExtra("placeModel",placeModel);
        intent.putExtra("mLastKnownLocation",MapsActivity.mLastKnownLocation);
        context.startActivity(intent);

    }

    public String getImageicon() {
        // The URL will usually come from a model (i.e Profile)
        return placeModel.filterType;
    }

   /* @BindingAdapter({"android:src"})
    public static void setImageicon(ImageView view, String imageUrl) {
       view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.bank_icn));
    }*/

    public void setPlace(PlaceModel placeModel) {
        this.placeModel = placeModel;
        notifyChange();
    }

}
