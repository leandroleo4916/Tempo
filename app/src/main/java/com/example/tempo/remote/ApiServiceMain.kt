package com.example.tempo.remote

import WeatherDataClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceTimeDay {
    @GET("https://api.open-meteo.com/v1/forecast?")
    suspend fun getWeatherDay(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("hourly") hourly: String = "temperature_2m",
        @Query("hourly") hourly2: String = "relativehumidity_2m",
        @Query("hourly") hourly3: String = "apparent_temperature",
        @Query("hourly") hourly4: String = "rain",
        @Query("daily") daily: String = "temperature_2m_max",
        @Query("daily") daily2: String = "temperature_2m_min",
        @Query("daily") daily3: String = "sunrise",
        @Query("daily") daily4: String = "sunset",
        @Query("current_weather") current_weather: String = "true",
        @Query("timezone") timezone: String = "America/Sao_Paulo"
    ): Response<WeatherDataClass>
}

