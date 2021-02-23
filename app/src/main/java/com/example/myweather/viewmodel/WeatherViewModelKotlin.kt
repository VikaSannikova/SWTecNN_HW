package com.example.myweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.DayWeather
import com.example.myweather.R
import com.example.myweather.api.RetrofitClient.getCurrentWeather
import com.example.myweather.api.RetrofitClient.getWeatherForecast
import com.example.myweather.api.model.CurrentWeather
import kotlinx.coroutines.launch
import java.util.*

class WeatherViewModelKotlin : ViewModel() {
    private val daysList: LiveData<List<DayWeather>?>
    val mutable_daysList: MutableLiveData<List<DayWeather>?>
    val currDay: MutableLiveData<CurrentWeather>

    fun makeApiCall() {
        viewModelScope.launch {
            val resp = getCurrentWeather()
            if (resp.isSuccessful){
                currDay.postValue(resp.body()?.weather)
            }
        }
        viewModelScope.launch {
            val resp = getWeatherForecast()
            if (resp.isSuccessful){
                val dayWeathers: MutableList<DayWeather> = ArrayList()
                    for (df in resp.body()!!.daily) {
                        dayWeathers.add(
                            DayWeather(
                                df.getDate(),
                                R.drawable.cloudy,
                                df.temp.day.toInt()
                            )
                        )
                    }
                mutable_daysList.postValue(dayWeathers)
            }
        }
    }

    init {
        mutable_daysList = MutableLiveData()
        daysList = mutable_daysList
        currDay = MutableLiveData()
    }
}