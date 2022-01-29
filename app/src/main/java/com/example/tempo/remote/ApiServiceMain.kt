package com.example.tempo.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceMain {
    @GET("{id}")
    fun tempo(@Path("id") id: String): Call<Map<String, Map<String, Periodo>>>
}

