package com.example.tempo.di

import com.example.tempo.activity.search.SearchViewModel
import com.example.tempo.utils.SecurityPreferences
import com.example.tempo.repository.RepositoryCidades
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module



val repositoryCidadesModule = module { single { RepositoryCidades(get()) } }
val securityPreferencesModule = module { factory { SecurityPreferences(get()) } }
val searchViewModelModule = module { viewModel { SearchViewModel(get()) } }

val appModules = listOf(
    searchViewModelModule, repositoryCidadesModule, securityPreferencesModule
)