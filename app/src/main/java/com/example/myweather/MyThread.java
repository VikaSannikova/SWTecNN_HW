package com.example.myweather;

import android.util.Log;

import com.example.myweather.api.RetrofitClient;
import com.example.myweather.api.model.CurrentWeatherForecast;
import com.example.myweather.api.model.DailyForecast;
import com.example.myweather.api.model.WeatherForecast;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyThread extends Thread {
    String tag;
    WeakReference<MainActivity> activityWeakReference;
    MainActivity activity;

    public MyThread(String tag, MainActivity context
    ) {
        this.tag = tag;
        activityWeakReference = new WeakReference(context);
        activity = activityWeakReference.get();
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) { //пока наш поток не прерван
            RetrofitClient.INSTANCE.getCurrentWeather().enqueue(new Callback<CurrentWeatherForecast>() {
                @Override
                public void onResponse(Call<CurrentWeatherForecast> call, Response<CurrentWeatherForecast> response) {
                    activity.temp = (int) response.body().component2().component1();
                    activity.humidity = response.body().component2().component2();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.temp_num.setText(String.valueOf(activity.temp)+ "°");
                            activity.humidity_num.setText(String.valueOf(activity.humidity) + "%");
                        }
                    });
                }

                @Override
                public void onFailure(Call<CurrentWeatherForecast> call, Throwable t) {
                    Log.i(tag, "НЕ работает запрос текущей погоды");
                }
            });
            RetrofitClient.INSTANCE.getWeatherForecast().enqueue(new Callback<WeatherForecast>() {
                @Override
                public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                    List<DailyForecast> days_from_server = response.body().component1();
                    for (DailyForecast df : days_from_server) {
                        activity.days_weather_from_server.add(new DayWeather(df.getDate(), R.drawable.cloudy, (int) df.getTemp().component1()));
                    }
                    activity.runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   MyWeatherAdapter weatherAdapter = new MyWeatherAdapter(activity, activity.days_weather_from_server);
                                                   activity.weather_list.setAdapter(weatherAdapter);
                                               }
                                           }
                    );
                }

                @Override
                public void onFailure(Call<WeatherForecast> call, Throwable t) {
                    Log.i(tag, "НЕ работает запрос недельно1 погоды");
                }
            });
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
