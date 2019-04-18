package com.robosoft.atm_finder.directions.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.robosoft.atm_finder.R;
import com.robosoft.atm_finder.directions.model.Directions;

public class DirectionListViewModel extends BaseObservable {

    private Directions directions;

    private Context context;

    public DirectionListViewModel(Directions directions, Context context) {
        this.directions = directions;
        this.context = context;
    }

    public String getInstruction()
    {
        return directions.routes.legs.steps.html_instructions;
    }

    public String getDistance()
    {
        return directions.routes.legs.steps.distance.value;
    }

    public String getDirectionIcon() {
        // The URL will usually come from a model (i.e Profile)
        String direction = null;
        if(directions.routes.legs.steps.maneuver!=null) {
            if (directions.routes.legs.steps.maneuver.contains("left")) {
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

    @BindingAdapter({"android:src"})
    public static void setDirectionIcon(ImageView view, String direction) {

        if(direction!=null) {
            if (direction.equalsIgnoreCase("left")) {
                view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.arrow_left_icn));
            } else if (direction.equalsIgnoreCase("right")) {
                view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.arrow_right_icn));
            } else {
                view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.arrow1_icn));
            }
        }

    }

    public void setDirections(Directions directions)
    {
        this.directions = directions;
        notifyChange();
    }

}
