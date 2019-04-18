package com.robosoft.atm_finder.map.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robosoft.atm_finder.DirectionCardListener;
import com.robosoft.atm_finder.databinding.PlacesCardBinding;
import com.robosoft.atm_finder.map.model.PlaceModel;
import com.robosoft.atm_finder.R;
import com.robosoft.atm_finder.map.viewmodel.PlaceViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<PlaceModel> placeModelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        PlacesCardBinding placesCardBinding;

        public MyViewHolder(PlacesCardBinding placesCardBinding) {
            super(placesCardBinding.cardView);
            this.placesCardBinding = placesCardBinding;
        }

        void bindPlaces(PlaceModel placeModel) {
            if (placesCardBinding.getPlaceViewModel() == null) {
                placesCardBinding.setPlaceViewModel(new PlaceViewModel(placeModel, itemView.getContext()));
            } else {
                placesCardBinding.getPlaceViewModel().setPlace(placeModel);
            }
        }
    }


    public RecyclerViewAdapter() {
        this.placeModelList = Collections.emptyList();
    }

    public void setPlaceModelList(List<PlaceModel> placeModelList) {
        this.placeModelList = placeModelList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PlacesCardBinding placesCardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.places_card, parent, false);
        return new MyViewHolder(placesCardBinding);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.bindPlaces(placeModelList.get(position));

       /* if (placeModel.getType().equalsIgnoreCase("bank")) {
            holder.bank_atm_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bank_icn));
        } else {
            holder.bank_atm_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.atm_icn));
        }

        holder.direction_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionCardListener.onDirectionClick(position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return placeModelList.size();
    }


}


