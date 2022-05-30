package com.example.tempo.remote

import com.example.tempo.utils.Constants.API_KEY
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

interface ApiServiceEmit {
    @GET("https://api.openweathermap.org/data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") key: String = API_KEY,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pt_br"
    ): Response<WeatherDataClass>
}

