package com.example.tempo.di

import com.example.tempo.activity.main.WeatherViewModel
import com.example.tempo.remote.ApiServiceEmit
import com.example.tempo.repository.RepositoryCidades
import com.example.tempo.repository.WeatherRepository
import com.example.tempo.utils.SecurityPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val URL_BASE = "https://viacep.com.br/ws/"

val retrofitModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    single<ApiServiceEmit> { get<Retrofit>().create(ApiServiceEmit::class.java) }
}


//val repositoryCidadesModule = module { single { RepositoryCidades(get()) } }
val securityPreferencesModule = module { factory { SecurityPreferences(get()) } }

val service = module { single {
    single<ApiServiceEmit> { get<Retrofit>().create(ApiServiceEmit::class.java) }
} }
val weatherRepository = module { single { WeatherRepository(get()) } }
val cityRepository = module { single { RepositoryCidades(get()) } }
val securityPreferences = module { single { SecurityPreferences(get()) } }
val weatherViewModel = module { viewModel { WeatherViewModel(get()) } }

val appModules = listOf(
    retrofitModule, securityPreferencesModule, service, weatherRepository, weatherViewModel,
    cityRepository
)