package com.robosoft.atm_finder.directions.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.text.Html;
import android.widget.ImageView;

import com.robosoft.atm_finder.R;
import com.robosoft.atm_finder.directions.model.Directions;
import com.robosoft.atm_finder.directions.model.Steps;

public class DirectionListViewModel extends BaseObservable {

    private Steps steps;

    private Context context;

    public DirectionListViewModel(Steps steps, Context context) {
        this.steps = steps;
        this.context = context;
    }

    public String getInstruction()
    {
        return Html.fromHtml(steps.html_instructions).toString().replaceAll("\n", "");
        //return null;
    }

    public String getDistance()
    {
        return steps.distance.value+" m";
       // return null;
    }

    public String getDirectionIcon() {
        // The URL will usually come from a model (i.e Profile)
        String direction = null;
        if(steps.maneuver!=null) {
            if (steps.maneuver.contains("left")) {
                direction = "left";
            } else {
                direction = "right";
            }
        }
        else {
            direction = "straight";
        }

        return direction;
    }

    /*@BindingAdapter({"android:src"})
    //public static void setDirectionIcon(ImageView view, String imageUrl) {
    public static void setImageicon(ImageView view, String imageUrl) {

        if(imageUrl!=null) {
            if (imageUrl.equalsIgnoreCase("left")) {
                view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.arrow_left_icn));
            } else if (imageUrl.equalsIgnoreCase("right")) {
                view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.arrow_right_icn));
            } else {
                view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.arrow1_icn));
            }
        }

    }*/

    public void setDirections(Steps directions)
    {
        this.steps = directions;
        notifyChange();
    }

}
