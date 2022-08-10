package com.example.tempo.remote

import WeatherDataClass
import com.example.tempo.dataclass.InfoCidade
import com.example.tempo.dataclass.Periodo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceMain {
    @GET("{id}")
    fun tempo( @Path("id") id: String): Call<Map<String, Map<String, Periodo>>>
}

interface ApiServiceRecycler {
    @GET("{id}")
    fun tempoRecycler(
        @Path("id") id: String): Call<Map<String, Map<String, InfoCidade>>>
}

interface ApiServiceTime {
    @GET("https://api.open-meteo.com/v1/forecast?")
    suspend fun getWeather(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("hourly") hourly: String = "temperature_2m",
        @Query("current_weather") current_weather: String = "true"
    ): Response<WeatherDataClass>
}

