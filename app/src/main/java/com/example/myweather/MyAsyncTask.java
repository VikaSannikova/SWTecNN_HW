package com.example.myweather;

import android.os.AsyncTask;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import com.example.myweather.api.RetrofitClient;
import com.example.myweather.api.model.CurrentWeatherForecast;
import com.example.myweather.api.model.DailyForecast;
import com.example.myweather.api.model.WeatherForecast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAsyncTask extends AsyncTask<Void, Void, ArrayList <DayWeather>> {
    String tag;
    WeakReference<MainActivity> activityWeakReference;
    MainActivity activity;

    public MyAsyncTask(String tag, MainActivity context) {
        this.tag = tag;
        activityWeakReference = new WeakReference(context);
        activity = activityWeakReference.get();
    }

    @Override
    protected ArrayList<DayWeather> doInBackground(Void... voids) {
        RetrofitClient.INSTANCE.getCurrentWeather().enqueue(new Callback<CurrentWeatherForecast>() {
            @Override
            public void onResponse(Call<CurrentWeatherForecast> call, Response<CurrentWeatherForecast> response) {
                int temp = (int) response.body().component2().component1();
                int humidity = response.body().component2().component2();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.temp_num.setText(String.valueOf(temp) + "°");
                        activity.humidity_num.setText(String.valueOf(humidity) + "%");
                    }
                });
            }

            @Override
            public void onFailure(Call<CurrentWeatherForecast> call, Throwable t) {
                //Log.i(tag, "НЕ работает запрос текущей погоды");
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
        return activity.days_weather_from_server;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("AT", "загружаем данные");
    }

}
