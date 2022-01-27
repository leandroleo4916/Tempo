package com.example.tempo.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tempo.remote.Cidades
import com.example.tempo.repository.RepositorySearch
import com.example.tempo.repository.Resultado

class SearchViewModel(private val repositorySearch: RepositorySearch) : ViewModel() {

    fun searchCidades(): LiveData<Resultado<ArrayList<Cidades>>> {
        return repositorySearch.getCity()
    }

}