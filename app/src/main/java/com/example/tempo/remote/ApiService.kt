package com.example.tempo.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceSearch {
    @GET("localidades/municipios")
    suspend fun cidades(): Response<ArrayList<Cidades>>
}

interface ApiServiceMain {
    @GET("{id}")
    suspend fun tempo(@Path("id") id: String): Response<Tempo>
}

