package com.robosoft.atm_finder;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

public class DataBindingAdapters {

    private static final String TAG = "DataBindingAdapters";

    @BindingAdapter({"android:src"})
    //public static void setDirectionIcon(ImageView view, String imageUrl) {
    public static void setImageicon(ImageView view, String imageUrl) {

        Log.d(TAG,"imageUrl : "+imageUrl);

        if(imageUrl!=null) {
            if (imageUrl.equalsIgnoreCase("left")) {
                view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.arrow_left_icn));
            } else if (imageUrl.equalsIgnoreCase("right")) {
                view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.arrow_right_icn));
            } else {
                view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.arrow1_icn));
            }
        }else
        {
            view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.bank_icn));
        }

    }

}
