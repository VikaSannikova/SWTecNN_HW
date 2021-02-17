package com.example.myweather.api.model

import com.example.myweather.api.model.DailyForecast

data class WeatherForecast(
    val daily: List<DailyForecast>
)
