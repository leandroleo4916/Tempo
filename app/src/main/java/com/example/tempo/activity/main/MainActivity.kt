package com.example.tempo.activity.main

import WeatherDataClass
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tempo.activity.ShowToast
import com.example.tempo.activity.search.SearchActivity
import com.example.tempo.adapter.MainAdapter
import com.example.tempo.adapter.SevenDaysAdapter
import com.example.tempo.constants.ConstantsCities
import com.example.tempo.databinding.ActivityMainBinding
import com.example.tempo.dataclass.SevenDaysDataClass
import com.example.tempo.dataclass.TimeDataClass
import com.example.tempo.repository.ResultRequest
import com.example.tempo.repository.WeatherType
import com.example.tempo.security.SecurityPreferences
import com.example.tempo.utils.CaptureDateCurrent
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val securityPreferences: SecurityPreferences by inject()
    private val weatherViewModel: WeatherViewModel by viewModel()
    private val showToast: ShowToast by inject()
    private val captureDateCurrent: CaptureDateCurrent by inject()
    private lateinit var adapter: MainAdapter
    private lateinit var adapterSevenDays: SevenDaysAdapter
    private lateinit var id: String
    private lateinit var city: String
    private lateinit var uf: String
    private lateinit var latitude: String
    private lateinit var longitude: String
    private val dataOfDay = ArrayList<TimeDataClass>()
    private val dataSevenDays = ArrayList<SevenDaysDataClass>()
    private val dateDay = captureDateCurrent.captureDateDay()
    private val hourDay = captureDateCurrent.captureHoraCurrent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        searchCity()

        if (city.isEmpty()) openActivity()
        else {
            recycler()
            observer()
            listener()
        }
    }

    override fun onRestart() {
        super.onRestart()
        binding.progressMain.visibility = View.VISIBLE
        dataOfDay.clear()
        dataSevenDays.clear()
        searchCity()
        observer()
    }

    private fun searchCity(){
        id = securityPreferences.getStoredString(ConstantsCities.CITY.ID)
        city = securityPreferences.getStoredString(ConstantsCities.CITY.NOME)
        uf = securityPreferences.getStoredString(ConstantsCities.CITY.UF)
        latitude = securityPreferences.getStoredString(ConstantsCities.CITY.LAT)
        longitude = securityPreferences.getStoredString(ConstantsCities.CITY.LON)
    }

    private fun recycler() {
        val recycler = binding.recyclerViewMain
        adapter = MainAdapter()
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recycler.adapter = adapter

        val recyclerSevenDays = binding.recyclerSevenDays
        adapterSevenDays = SevenDaysAdapter()
        recyclerSevenDays.layoutManager = LinearLayoutManager(this)
        recyclerSevenDays.adapter = adapterSevenDays
    }

    private fun observer() {
        weatherViewModel.searchDataWeather(latitude, longitude).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultRequest.Success -> {
                        result.dado?.let { weather ->
                            addElementViewMain(weather)
                            addElementRecycler(weather)
                            addElementTimeSevenDays(weather)
                            addElementMore(weather)
                        }
                    }
                    is ResultRequest.Error -> {
                        result.exception.message?.let { it -> showToast.message(it, this) }
                    }
                    is ResultRequest.ErrorConnection -> {
                        result.exception.message?.let { it -> showToast.message(it, this) }
                    }
                }
            }
        }
    }

    private fun addElementViewMain(weather: WeatherDataClass) {

        binding.run {
            progressMain.visibility = View.GONE
            val position = divHour(hourDay)
            val fell = weather.hourly.apparentTemperature[position]
            var fellUnit = fell.toInt()
            val time = weather.currentWeather.temperature
            var timeUnit = time.toInt()
            val max = weather.daily.temperature2MMax[0]
            var maxUnit = max.toInt()
            val min = weather.daily.temperature2MMin[0]
            var minUnit = min.toInt()
            val code = WeatherType.weatherCode(weather.currentWeather.weathercode.toInt())

            if (fell > fellUnit+0.5) fellUnit += 1
            if (time > timeUnit+0.5) timeUnit += 1
            if (max > maxUnit+0.5) maxUnit += 1
            if (min > minUnit+0.5) minUnit += 1

            "$city - $uf".also { textviewCity.text = it }
            code.weatherDesc.also { textviewCeu.text = it }
            "$dateDay - $hourDay".also { textviewDate.text = it }
            "Humidade ${weather.hourly.relativehumidity2M[position]}%".also { textviewHumidity.text = it }
            ("Sessação térmica de ${fellUnit}º").also { textviewTermica.text = it }
            "$timeUnit".also { textViewTemperatura.text = it }
            ("$maxUnit"+"º/"+"${minUnit}º").also { textviewMaxmin.text = it }

            progressMain.visibility = View.GONE
            textviewCity.visibility = View.VISIBLE
            imageLocationGps.visibility = View.VISIBLE
            textviewDate.visibility = View.VISIBLE
            textViewTemperatura.visibility = View.VISIBLE
            textCelsius.visibility = View.VISIBLE
            textviewCeu.visibility = View.VISIBLE
            textviewMaxmin.visibility = View.VISIBLE
            textviewTermica.visibility = View.VISIBLE
            textviewHumidity.visibility = View.VISIBLE
            textAfterDays.visibility = View.VISIBLE
        }
    }

    private fun addElementTimeSevenDays(weather: WeatherDataClass) {

        var date = captureDateCurrent.captureDateCurrent()
        var dateValue = "hoje"
        var position = 10
        for (i in 0..6){
            if (i != 0){
                dateValue = captureDateCurrent.getDayOfWeek(date)
                date = captureDateCurrent.captureNextDate(date)
            }
            else {
                date = captureDateCurrent.captureNextDate(date)
            }
            val humidity = weather.hourly.relativehumidity2M[position].toString()+"%"
            val codeDay = WeatherType.weatherCode(weather.hourly.weathercode[position].toInt())
            val codeNight = WeatherType.weatherCode(weather.hourly.weathercode[position+10].toInt())
            val maxAndMin = weather.daily.temperature2MMax[i].toInt().toString()+"º/"+
                            weather.daily.temperature2MMin[i].toInt().toString()+"º"
            dataSevenDays.add(SevenDaysDataClass(dateValue, humidity, codeDay.iconRes, codeNight.iconRes, maxAndMin))
            position += 24
        }
        adapterSevenDays.updateWeatherSevenDays(dataSevenDays)
    }

    private fun listener(){
        binding.run {
            imageAddLocalization.setOnClickListener { openActivity() }
            textOpenMeteo.setOnClickListener {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://open-meteo.com/"))
                startActivity(browserIntent)
            }
        }
    }

    private fun openActivity(){
        startActivity(Intent(this, SearchActivity::class.java))
    }

    private fun addElementRecycler(weather: WeatherDataClass){

        val divHourInit = divHour(hourDay)
        var position = divHourInit
        for (i in 1..24){
            val temp = weather.hourly.temperature2M[position]
            var temperature = temp.toInt()
            if (temp > temperature+0.5) temperature += 1
            val time = weather.hourly.time[position].split("T")
            val rain = weather.hourly.relativehumidity2M[position].toString()
            val code = WeatherType.weatherCode(weather.currentWeather.weathercode.toInt())
            position += 1
            dataOfDay.add(TimeDataClass(time[1], temperature.toString(), code.iconRes, rain))
        }
        adapter.updateWeatherPerHour(dataOfDay)
    }

    private fun addElementMore(weather: WeatherDataClass){
        binding.run {
            val sunrise = weather.daily.sunrise[0].split("T")
            textNascerRight.text = sunrise[1]
            val sunset = weather.daily.sunset[0].split("T")
            textPorRight.text = sunset[1]
            textWindRight.text = weather.daily.windspeed10MMax[0].toString()+" Km/h"
            textVolumeRight.text = weather.daily.rainSum[0].toString()+" mm"
        }
    }

    private fun divHour(hour: String): Int{
        val res = hour.split(":")
        return res[0].toInt()
    }
}