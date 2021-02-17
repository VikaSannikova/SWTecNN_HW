package com.example.myweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyLocationAdapter extends RecyclerView.Adapter<MyLocationAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Location> locations;


    MyLocationAdapter(Context context, List<Location> states) {
        this.locations = states;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.location_row, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Location day = locations.get(position);
        holder.tomorrowView.setChecked(day.getTomorrowID());
        holder.tomorrowView.setTag(day);


        holder.nameView.setText(day.getNameID());
        holder.nameView.setContentDescription(day.getNameID());

        holder.todayView.setChecked(day.getTodayID());
        holder.todayView.setTag(day);

        if(holder.todayView.isChecked()){
            holder.nameView.setTextColor(holder.nameView.getHighlightColor());
        }

        holder.tomorrowView.setContentDescription("tomorrow watering" + holder.nameView.getText());
        holder.todayView.setContentDescription("today watering" + holder.nameView.getText());
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final CheckBox tomorrowView, todayView;
        final TextView nameView;

        ViewHolder(View view) {
            super(view);
            tomorrowView = (CheckBox) view.findViewById(R.id.tomorrow_cb_id);
            nameView = (TextView) view.findViewById(R.id.location_name_id);
            todayView = (CheckBox) view.findViewById(R.id.today_cb_id);
        }
    }
}
