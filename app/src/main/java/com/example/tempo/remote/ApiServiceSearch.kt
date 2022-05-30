package com.example.tempo.remote

import retrofit2.Call
import retrofit2.http.GET

interface ApiServiceSearch {
    @GET("localidades/municipios")
    fun cities(): Call<ArrayList<Cidades>>
}