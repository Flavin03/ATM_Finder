package com.robosoft.atm_finder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<PlaceModel> placeModelList;
    private DirectionCardListener directionCardListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView place_name, place_address, place_distance;
        public ImageView bank_atm_icon, direction_icon;

        public MyViewHolder(View view) {
            super(view);
            place_name = (TextView) view.findViewById(R.id.place_name);
            place_address = (TextView) view.findViewById(R.id.place_address);
            place_distance = (TextView) view.findViewById(R.id.place_distance);
            bank_atm_icon = (ImageView) view.findViewById(R.id.bank_atm_icon);
            direction_icon = (ImageView) view.findViewById(R.id.direction_icon);
        }
    }


    public RecyclerViewAdapter(Context mContext, ArrayList<PlaceModel> placeModelList, DirectionCardListener directionCardListener) {
        this.mContext = mContext;
        this.placeModelList = placeModelList;
        this.directionCardListener = directionCardListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PlaceModel placeModel = placeModelList.get(position);
        holder.place_name.setText(placeModel.getName());
        holder.place_address.setText(placeModel.getVicinity());
        holder.place_distance.setText(placeModel.getDistanceFrom()+"m");

        if (placeModel.getType().equalsIgnoreCase("bank")) {
            holder.bank_atm_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bank_icn));
        } else {
            holder.bank_atm_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.atm_icn));
        }

        holder.direction_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionCardListener.onDirectionClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeModelList.size();
    }


}


