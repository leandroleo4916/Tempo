package com.example.tempo.di

import com.example.tempo.activity.MainViewModel
import com.example.tempo.activity.SearchViewModel
import com.example.tempo.activity.utils.SecurityPreferences
import com.example.tempo.remote.ApiServiceMain
import com.example.tempo.remote.ApiServiceSearch
import com.example.tempo.remote.RetrofitClient
import com.example.tempo.repository.RepositoryCidades
import com.example.tempo.repository.RepositoryMain
import com.example.tempo.repository.RepositorySearch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitSearchModule = module {

    val baseUrlSearch = "https://apiprevmet3.inmet.gov.br/previsao/"
    single<Retrofit> {
        Retrofit.Builder()
            .client(get())
            .baseUrl(baseUrlSearch)
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
    single<ApiServiceMain> { get<Retrofit>().create(ApiServiceMain::class.java) }
}

val repositoryMainModule = module { single { RepositoryMain(get()) } }
val repositoryCidadesModule = module { single { RepositoryCidades(get()) } }
val securityPreferencesModule = module { factory { SecurityPreferences(get()) } }
val viewModelModule = module { viewModel { MainViewModel(get()) } }
val searchViewModelModule = module { viewModel { SearchViewModel(get()) } }

val appModules = listOf(
    retrofitSearchModule, searchViewModelModule, viewModelModule, repositoryMainModule,
    repositoryCidadesModule, securityPreferencesModule
)