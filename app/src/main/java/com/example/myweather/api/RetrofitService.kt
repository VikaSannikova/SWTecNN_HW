package com.example.myweather.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import com.example.myweather.api.model.CurrentWeatherForecast
import com.example.myweather.api.model.WeatherForecast
import io.reactivex.rxjava3.core.Single

private const val API_KEY = "92dd28f1dc9baf629b40f09a07ff87ac"
//private const val API_KEY = "8665b3a61f87e64bce15c39b6bb36802"


interface RetrofitService {

    @GET("data/2.5/onecall")
    fun getWeatherForecast(
        @Query("lat") latitude: Double = 56.302947,
        @Query("lon") longitude: Double = 44.021527,
        @Query("exclude") exclude: String = arrayOf(
            "current",
            "minutely",
            "hourly",
            "alerts"
        ).joinToString(separator = ","),
        @Query("units") units: String = "metric",
        @Query("appId") apiKey: String = API_KEY
    ): Single<WeatherForecast>

    @GET("data/2.5/weather")
    fun getCurrentWeatherForecast(
            @Query("q") place: String = arrayOf(
            "Nizhniy Novgorod", "RUS"
        ).joinToString(separator = ","),
            @Query("appId") apiKey: String = API_KEY,
            @Query("units") units: String = "metric"
    ): Single<CurrentWeatherForecast>

    @GET
    fun getWeatherImage(@Url imageUrl: String): Single<ResponseBody>
}