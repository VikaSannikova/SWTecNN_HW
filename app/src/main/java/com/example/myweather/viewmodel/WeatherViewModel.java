package com.example.myweather.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myweather.DayWeather;
import com.example.myweather.R;
import com.example.myweather.api.RetrofitClient;
import com.example.myweather.api.model.DailyForecast;
import com.example.myweather.api.model.WeatherForecast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeatherViewModel extends ViewModel {

    private LiveData<List<DayWeather>> daysList;
    private MutableLiveData<List<DayWeather>> mutable_daysList;
    private CompositeDisposable compositeDisposable;

    public WeatherViewModel() {
        mutable_daysList = new MutableLiveData<>();
        daysList = mutable_daysList;
        compositeDisposable = new CompositeDisposable();
    }

    public MutableLiveData<List<DayWeather>> getMutable_daysList() {
        return mutable_daysList;
    }

    public void makeApiCall() {
        //Call<WeatherForecast> weatherForecastCall= RetrofitClient.INSTANCE.getWeatherForecast();

        RetrofitClient.INSTANCE.getWeatherForecast()
                .subscribeOn(Schedulers.io()).subscribe(new DisposableSingleObserver<WeatherForecast>() {
            @Override
            public void onSuccess(@NonNull WeatherForecast weatherForecast) {
                List<DayWeather> dayWeathers = new ArrayList<>();
                for (DailyForecast df : weatherForecast.component1()) {
                    dayWeathers.add(new DayWeather(df.getDate(), R.drawable.cloudy, (int) df.getTemp().component1()));
                }
                mutable_daysList.postValue(dayWeathers);
                Log.i("___", "RX onSuccess");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mutable_daysList.postValue(null);
                Log.i("___", "RX onError");
            }
        });

//        weatherForecastCall.enqueue(new Callback<WeatherForecast>() {
//            @Override
//            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
//                List<DailyForecast> days_from_server = response.body().component1();
//                List<DayWeather> dayWeathers = new ArrayList<>();
//                for (DailyForecast df : days_from_server) {
//                    dayWeathers.add(new DayWeather(df.getDate(), R.drawable.cloudy, (int) df.getTemp().component1()));
//                }
//                mutable_daysList.postValue(dayWeathers);
//            }
//
//            @Override
//            public void onFailure(Call<WeatherForecast> call, Throwable t) {
//                mutable_daysList.postValue(null);
//            }
//        });
    }
}























