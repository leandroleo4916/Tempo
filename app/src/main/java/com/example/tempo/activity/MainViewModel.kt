package com.example.tempo.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tempo.remote.Tempo
import com.example.tempo.repository.RepositoryMain
import com.example.tempo.repository.ResultadoMain

class MainViewModel(private val repositoryMain: RepositoryMain) : ViewModel() {

    fun getTempo(id: String): LiveData<ResultadoMain<Tempo>> {
        return repositoryMain.getTempoMain(id)
    }

}