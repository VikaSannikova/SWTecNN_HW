package com.example.myweather;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweather.api.RetrofitClient;
import com.example.myweather.api.model.CurrentWeatherForecast;
import com.example.myweather.api.model.DailyForecast;
import com.example.myweather.api.model.WeatherForecast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    TextView notification_button;
    CheckBox sprinkler_button;
    RecyclerView weather_list;
    RecyclerView location_list;
    Guideline guideline1;
    Guideline guideline2;
    WateringTimeView myview;
    ArrayList<DayWeather> days_weather_from_server;
    ThreadPoolExecutor mThreadPoolExecutor;
    TextView temp_num, humidity_num;
    Handler mainHandler = new Handler();
    LoaderManager mLoaderManager;

    Thread mThread;
    String tag = "mThread";

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.constraint_activity_main);
        Context context = this;
        ArrayList<DayWeather> days_weather = new ArrayList<>();
        days_weather_from_server = new ArrayList<>();
        days_weather_from_server.clear();
        temp_num = (TextView) findViewById(R.id.temp_num);
        humidity_num = (TextView) findViewById(R.id.humidity_num);
        mThreadPoolExecutor = new ThreadPoolExecutor(
                4,
                10,
                1000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        mThreadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                RetrofitClient.INSTANCE.getCurrentWeather().enqueue(new Callback<CurrentWeatherForecast>() {
                    @Override
                    public void onResponse(Call<CurrentWeatherForecast> call, Response<CurrentWeatherForecast> response) {
                        int temp = (int) response.body().component2().component1();
                        int humidity = response.body().component2().component2();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                temp_num.setText(temp + "°");
                                humidity_num.setText(humidity + "%");
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
                            days_weather_from_server.add(new DayWeather(df.getDate(), R.drawable.cloudy, (int) df.getTemp().component1()));
                        }
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              MyWeatherAdapter weatherAdapter = new MyWeatherAdapter(getLayoutInflater().getContext(), days_weather_from_server);
                                              weather_list.setAdapter(weatherAdapter);
                                          }
                                      }
                        );
                    }

                    @Override
                    public void onFailure(Call<WeatherForecast> call, Throwable t) {
                        Log.i(tag, "НЕ работает запрос недельно1 погоды");
                    }
                });
            }
        });

        days_weather.clear();
        days_weather.add(new DayWeather("February 7, 2022", R.drawable.cloudy, 12));
        days_weather.add(new DayWeather("February 8, 2020", R.drawable.rain, -10));
        days_weather.add(new DayWeather("February 9, 2020", R.drawable.partly_cloudy, 22));

        ArrayList<Location> locations = new ArrayList<>();
        locations.clear();
        locations.add(new Location(false, "BACKYARD", true));
        locations.add(new Location(false, "BACK PATIO", false));
        locations.add(new Location(false, "FRONT YARD", false));
        locations.add(new Location(false, "GARDEN", true));
        locations.add(new Location(false, "PORCH", false));

        weather_list = (RecyclerView) findViewById(R.id.weather_list);
        LinearLayoutManager llm1 = new LinearLayoutManager(this);
        weather_list.setLayoutManager(llm1);
        // MyWeatherAdapter weatherAdapter = new MyWeatherAdapter(this, days_weather);
        // MyWeatherAdapter weatherAdapter = new MyWeatherAdapter(this, days_weather1);
        // weather_list.setAdapter(weatherAdapter);

        location_list = (RecyclerView) findViewById(R.id.location_list);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        location_list.setLayoutManager(llm2);
        MyLocationAdapter locationAdapter = new MyLocationAdapter(this, locations);
        location_list.setAdapter(locationAdapter);

        sprinkler_button = (CheckBox) findViewById(R.id.sprinkler_cb);
        sprinkler_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sprinkler_button.isChecked()) {
                    for (Location loc : locations) {
                        loc.setTomorrowID(false);
                    }
                    MyLocationAdapter locationAdapter1 = new MyLocationAdapter(context, locations);
                    location_list.setAdapter(locationAdapter1);
                }
            }
        });

        notification_button = (TextView) findViewById(R.id.notification_button);
        String desc = (String) notification_button.getContentDescription();
        notification_button.setContentDescription(desc + notification_button.getText());

        guideline1 = (Guideline) findViewById(R.id.guideline1);
        guideline2 = (Guideline) findViewById(R.id.guideline2);

        myview = (WateringTimeView) findViewById(R.id.wateringtimeview);
        myview.setTemplate("already watering for: \r %s");
        Time time = new Time();
        time.setToNow();
        myview.setDuration(time.hour * 3600 + time.minute * 60 + time.second);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            guideline1.setGuidelinePercent(0.30f);
            guideline2.setGuidelinePercent(0.60f);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            guideline1.setGuidelinePercent(0.30f);
            guideline2.setGuidelinePercent(0.70f);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mThreadPoolExecutor.shutdown();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mThreadPoolExecutor.shutdown();
    }
}
