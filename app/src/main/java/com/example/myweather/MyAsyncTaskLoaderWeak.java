package com.example.myweather;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myweather.api.RetrofitClient;
import com.example.myweather.api.model.WeatherForecast;

import java.io.IOException;


public class MyAsyncTaskLoaderWeak extends AsyncTaskLoader<WeatherForecast> {
    public MyAsyncTaskLoaderWeak(@NonNull Context context, Bundle bundle) {
        super(context);
    }

    @Nullable
    @Override
    public WeatherForecast loadInBackground() {
        try {
            WeatherForecast temp = RetrofitClient.INSTANCE.getWeatherForecast().execute().body();
            Log.d("___", "background suc");
            return temp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public void forceLoad() {
        Log.d("___", "forceLoad");
        super.forceLoad();
    }
}
