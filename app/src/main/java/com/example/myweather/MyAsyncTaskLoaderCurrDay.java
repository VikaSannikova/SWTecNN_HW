package com.example.myweather;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.myweather.api.RetrofitClient;
import com.example.myweather.api.model.CurrentWeather;
import com.example.myweather.api.model.DailyForecast;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class MyAsyncTaskLoaderCurrDay extends AsyncTaskLoader<CurrentWeather>{

    WeakReference<MainActivity> activityWeakReference;
    MainActivity activity;
    List<DailyForecast> days;

    public MyAsyncTaskLoaderCurrDay(Context context, Bundle bundle) {
        super(context);
    }

    @Override
    public CurrentWeather loadInBackground() {
        try {
            float temp = RetrofitClient.INSTANCE.getCurrentWeather().execute().body().component2().getTemp();
            int humidity = RetrofitClient.INSTANCE.getCurrentWeather().execute().body().component2().getHumidity();
            Log.d("___", "background suc");
            return new CurrentWeather(temp,humidity);
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

















