package com.example.myweather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyWeatherAdapter extends RecyclerView.Adapter<MyWeatherAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<DayWeather> days;

    MyWeatherAdapter(Context context, List<DayWeather> states) {
        this.days = states;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.weather_row, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DayWeather day = days.get(position);
        holder.dayView.setText(day.getDateID());
        holder.dayView.setContentDescription(day.getDateID());
        holder.tempView.setText(String.valueOf(day.getTempID())+"°");
        Drawable icon = holder.itemView.getResources().getDrawable(day.getPicID());
        icon.setBounds(0,0,120,100);
        holder.tempView.setCompoundDrawables(icon,null,null, null);


    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView dayView, tempView;

        ViewHolder(View view) {
            super(view);
            dayView = (TextView) view.findViewById(R.id.item_weathercast_date);
            tempView = (TextView) view.findViewById(R.id.item_weathercast_weather);
        }
    }
}

