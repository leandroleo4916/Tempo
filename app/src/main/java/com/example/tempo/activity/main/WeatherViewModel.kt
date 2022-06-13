package com.example.tempo.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tempo.remote.WeatherDataClass
import com.example.tempo.repository.ResultRequest
import com.example.tempo.repository.WeatherRepository

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    fun searchDataWeather(city: String):
            LiveData<ResultRequest<WeatherDataClass?>> = repository.weatherData(city)

}

