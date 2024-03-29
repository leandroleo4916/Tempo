package com.example.tempo.remote

import WeatherDataClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceTimeDay {
    @GET("/v1/forecast?")
    suspend fun getWeatherDay(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("hourly") hourly: String = "temperature_2m",
        @Query("hourly") hourly1: String = "relativehumidity_2m",
        @Query("hourly") hourly2: String = "apparent_temperature",
        @Query("hourly") hourly3: String = "showers",
        @Query("hourly") hourly4: String = "weathercode",
        @Query("daily") daily: String = "weathercode",
        @Query("daily") daily1: String = "temperature_2m_max",
        @Query("daily") daily2: String = "temperature_2m_min",
        @Query("daily") daily3: String = "sunrise",
        @Query("daily") daily4: String = "sunset",
        @Query("daily") daily5: String = "rain_sum",
        @Query("daily") daily6: String = "windspeed_10m_max",
        @Query("current_weather") current_weather: String = "true",
        @Query("timezone") timezone: String = "America/Sao_Paulo"
    ): Response<WeatherDataClass>
}

