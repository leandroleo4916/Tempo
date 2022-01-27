package com.example.tempo.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient{

    companion object{
        private lateinit var retrofit: Retrofit
        private const val baseUrl = "https://servicodados.ibge.gov.br/api/v1/"

        private fun retrofitInstance(): Retrofit{
            val httpClient = OkHttpClient.Builder()
            if(!::retrofit.isInitialized){
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }

    fun<T> createService(service: Class<T>): T {
        return retrofitInstance().create(service)
    }

}