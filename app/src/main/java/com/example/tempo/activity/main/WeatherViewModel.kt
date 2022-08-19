package com.example.tempo.activity.main
import WeatherDataClass
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tempo.repository.ResultRequest
import com.example.tempo.repository.WeatherRepository

class WeatherViewModel(private val repository: WeatherRepository): ViewModel() {

    fun searchDataWeather(latitude: String, longitude: String):
            LiveData<ResultRequest<WeatherDataClass?>> = repository.weatherData(latitude, longitude)

}

