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

private lateinit var retrofitClient: RetrofitClient

val retrofitSearchModule = module {

    val baseUrlSearch = "https://servicodados.ibge.gov.br/api/v1/"
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
    single<ApiServiceSearch> { get<Retrofit>().create(ApiServiceSearch::class.java) }
}

val test = module { single<ApiServiceMain> { get<Retrofit>().create(ApiServiceMain::class.java) } }
val repositorySearchModule = module { single { RepositorySearch(get()) } }
val repositoryMainModule = module { single { RepositoryMain(get()) } }
val repositoryCidadesModule = module { single { RepositoryCidades(get()) } }
val securityPreferencesModule = module { factory { SecurityPreferences(get()) } }
val viewModelModule = module { viewModel { MainViewModel(get()) } }
val searchViewModelModule = module { viewModel { SearchViewModel(get()) } }

val appModules = listOf(
    retrofitSearchModule, repositorySearchModule,
    searchViewModelModule, viewModelModule, repositoryMainModule,
    repositoryCidadesModule, securityPreferencesModule, test
)