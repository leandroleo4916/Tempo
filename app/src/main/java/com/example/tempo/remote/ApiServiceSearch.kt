package com.example.tempo.remote

import com.example.tempo.dataclass.Cidades
import retrofit2.Call
import retrofit2.http.GET

interface ApiServiceSearch {
    @GET("localidades/municipios")
    fun cities(): Call<ArrayList<Cidades>>
}