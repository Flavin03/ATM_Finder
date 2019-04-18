package com.robosoft.atm_finder.directions.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robosoft.atm_finder.databinding.DirectionsRowBinding;
import com.robosoft.atm_finder.directions.model.DirectionModel;
import com.robosoft.atm_finder.R;
import com.robosoft.atm_finder.directions.model.Directions;
import com.robosoft.atm_finder.directions.viewmodel.DirectionListViewModel;
import com.robosoft.atm_finder.directions.viewmodel.DirectionViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectionsRecyclerViewAdapter extends RecyclerView.Adapter<DirectionsRecyclerViewAdapter.MyViewHolder> {
    private List<Directions> directionList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        DirectionsRowBinding directionsRowBinding;

        public MyViewHolder(DirectionsRowBinding directionsRowBinding) {
            super(directionsRowBinding.directionLinearlayout);
           this.directionsRowBinding = directionsRowBinding;
        }

        void bindDirections(Directions directions) {
            if (directionsRowBinding.getDirectionListViewModel() == null) {
                directionsRowBinding.setDirectionListViewModel(new DirectionListViewModel(directions, itemView.getContext()));
            } else {
                directionsRowBinding.getDirectionListViewModel().setDirections(directions);
            }
        }
    }

    public DirectionsRecyclerViewAdapter() {
        this.directionList = Collections.emptyList();
    }

    public void setDirectionList(List<Directions> directionList) {
        this.directionList = directionList;
        notifyDataSetChanged();
    }


    @Override
    public DirectionsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DirectionsRowBinding directionsRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.directions_row, parent, false);
        return new MyViewHolder(directionsRowBinding);
    }

    @Override
    public void onBindViewHolder(final DirectionsRecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.bindDirections(directionList.get(position));

       /* final DirectionModel directionModel = directionModelArrayList.get(position);
        holder.textview_instruction.setText(Html.fromHtml(directionModel.getInstruction()).toString().replaceAll("\n", ""));
        holder.textview_distance.setText(directionModel.getDistance()+" m");

        if(directionModel.getDirection()!=null) {
            if (directionModel.getDirection().contains("left")) {
                holder.direction_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_left_icn));
            } else {
                holder.direction_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_right_icn));
            }
        }
        else {
            holder.direction_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow1_icn));
        }*/

    }

    @Override
    public int getItemCount() {
        return directionList.size();
    }
}
