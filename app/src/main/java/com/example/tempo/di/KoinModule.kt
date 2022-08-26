package com.example.tempo.di

import com.example.tempo.activity.ShowToast
import com.example.tempo.activity.main.WeatherViewModel
import com.example.tempo.activity.search.SearchViewModel
import com.example.tempo.dbhistory.DataBaseHistory
import com.example.tempo.remote.ApiServiceTimeDay
import com.example.tempo.repository.RepositoryCities
import com.example.tempo.repository.RepositoryHistory
import com.example.tempo.repository.WeatherRepository
import com.example.tempo.security.SecurityPreferences
import com.example.tempo.utils.CaptureDateCurrent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    single<ApiServiceTimeDay> {
        get<Retrofit>().create(ApiServiceTimeDay::class.java)
    }
}

val weatherRepository = module { single { WeatherRepository(get()) } }
val cityRepository = module { single { RepositoryCities(get()) } }
val repositoryHistory = module { single { RepositoryHistory(get()) } }
val securityPreferences = module { single { SecurityPreferences(get()) } }
val weatherViewModel = module { viewModel { WeatherViewModel(get()) } }
val historyViewModel = module { viewModel { SearchViewModel(get()) } }
val dataBase = module { single { DataBaseHistory(get()) } }
val showToast = module { factory { ShowToast() } }
val captureDate = module { factory { CaptureDateCurrent() } }

val appModules = listOf(
    retrofitModule, securityPreferences, weatherRepository, showToast, captureDate,
    weatherViewModel, cityRepository, dataBase, repositoryHistory, historyViewModel
)