package com.example.tempo.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceMain {
    @GET("{id}")
    suspend fun tempo(@Path("id") id: String): Response<Welcome>
}

