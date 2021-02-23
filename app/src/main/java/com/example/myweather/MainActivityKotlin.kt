package com.example.myweather

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.format.Time
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Guideline
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.viewmodel.WeatherViewModelKotlin
import java.util.*

class MainActivityKotlin : AppCompatActivity() {
    var notification_button: TextView? = null
    var sprinkler_button: CheckBox? = null
    var weather_list: RecyclerView? = null
    var location_list: RecyclerView? = null
    var guideline1: Guideline? = null
    var guideline2: Guideline? = null
    var myview: WateringTimeView? = null
    var days_weather_from_server: MutableList<DayWeather>? = null
    var weatherAdapter: MyWeatherAdapter? = null
    var weatherViewModel: WeatherViewModelKotlin? = null
    var temp_num: TextView? = null
    var humidity_num: TextView? = null
    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.constraint_activity_main)
        val context: Context = this
        val days_weather = ArrayList<DayWeather>()
        days_weather_from_server = ArrayList()
        days_weather_from_server!!.clear()
        temp_num = findViewById(R.id.temp_num)
        humidity_num = findViewById(R.id.humidity_num)


//        days_weather.clear();
//        days_weather.add(new DayWeather("February 7, 2022", R.drawable.cloudy, 12));
//        days_weather.add(new DayWeather("February 8, 2020", R.drawable.rain, -10));
//        days_weather.add(new DayWeather("February 9, 2020", R.drawable.partly_cloudy, 22));
        val locations =
            ArrayList<Location>()
        locations.clear()
        locations.add(Location(false, "BACKYARD", true))
        locations.add(Location(false, "BACK PATIO", false))
        locations.add(Location(false, "FRONT YARD", false))
        locations.add(Location(false, "GARDEN", true))
        locations.add(Location(false, "PORCH", false))
        weather_list = findViewById<View>(R.id.weather_list) as RecyclerView
        val llm1 = LinearLayoutManager(this)
        weather_list!!.layoutManager = llm1
        weatherAdapter = MyWeatherAdapter(this, days_weather_from_server)
        weather_list!!.adapter = weatherAdapter
        weatherViewModel =
            ViewModelProviders.of(this).get(WeatherViewModelKotlin::class.java)
        weatherViewModel!!.mutable_daysList.observe(
            this,
            Observer { dayWeathers ->
                if (dayWeathers != null) {
                    days_weather_from_server = dayWeathers as MutableList<DayWeather>?
                    weatherAdapter!!.setDays(days_weather_from_server)
                } else {
                    Log.i("___", "NO RESULTS")
                }
            })
        weatherViewModel!!.currDay
            .observe(this,
                Observer { currentWeather ->
                    if (currentWeather != null) {
                        temp_num?.setText(currentWeather.temp.toInt().toString() + "°")
                        humidity_num?.setText(currentWeather.humidity.toString() + "%")
                    } else {
                        Log.i("___", "NO RESULTS")
                    }
                })
        weatherViewModel!!.makeApiCall()
        location_list = findViewById<View>(R.id.location_list) as RecyclerView
        val llm2 = LinearLayoutManager(this)
        location_list!!.layoutManager = llm2
        val locationAdapter = MyLocationAdapter(this, locations)
        location_list!!.adapter = locationAdapter
        sprinkler_button = findViewById<View>(R.id.sprinkler_cb) as CheckBox
        sprinkler_button!!.setOnClickListener {
            if (!sprinkler_button!!.isChecked) {
                for (loc in locations) {
                    loc.tomorrowID = false
                }
                val locationAdapter1 = MyLocationAdapter(context, locations)
                location_list!!.adapter = locationAdapter1
            }
        }
        notification_button = findViewById<View>(R.id.notification_button) as TextView
        val desc = notification_button!!.contentDescription as String
        notification_button!!.contentDescription = desc + notification_button!!.text
        guideline1 =
            findViewById<View>(R.id.guideline1) as Guideline
        guideline2 =
            findViewById<View>(R.id.guideline2) as Guideline
        myview = findViewById<View>(R.id.wateringtimeview) as WateringTimeView
        myview!!.setTemplate("already watering for: \r %s")
        val time = Time()
        time.setToNow()
        myview!!.setDuration(time.hour * 3600 + time.minute * 60 + time.second.toFloat())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            guideline1!!.setGuidelinePercent(0.30f)
            guideline2!!.setGuidelinePercent(0.60f)
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            guideline1!!.setGuidelinePercent(0.30f)
            guideline2!!.setGuidelinePercent(0.70f)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }
}