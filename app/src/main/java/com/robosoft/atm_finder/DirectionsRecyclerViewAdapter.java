package com.robosoft.atm_finder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DirectionsRecyclerViewAdapter extends RecyclerView.Adapter<DirectionsRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private String filterString;
    private ArrayList<DirectionModel> directionModelArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textview_instruction, textview_distance;
        public ImageView direction_icon;

        public MyViewHolder(View view) {
            super(view);
            textview_instruction = (TextView) view.findViewById(R.id.textview_instruction);
            textview_distance = (TextView) view.findViewById(R.id.textview_distance);
            direction_icon = (ImageView) view.findViewById(R.id.direction_icon);
        }
    }


    public DirectionsRecyclerViewAdapter(Context mContext, ArrayList<DirectionModel> directionModelArrayList, String filterString) {
        this.mContext = mContext;
        this.directionModelArrayList = directionModelArrayList;
        this.filterString = filterString;
    }

    @Override
    public DirectionsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.directions_row, parent, false);

        return new DirectionsRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DirectionsRecyclerViewAdapter.MyViewHolder holder, int position) {
        final DirectionModel directionModel = directionModelArrayList.get(position);
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
        }

    }

    @Override
    public int getItemCount() {
        return directionModelArrayList.size();
    }
}
