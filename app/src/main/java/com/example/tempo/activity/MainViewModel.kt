package com.example.tempo.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tempo.remote.Welcome
import com.example.tempo.repository.RepositoryMain
import com.example.tempo.repository.ResultadoMain

class MainViewModel(private val repositoryMain: RepositoryMain) : ViewModel() {

    fun getTempo(id: String): LiveData<ResultadoMain<Welcome>> {
        return repositoryMain.getTempoMain(id)
    }

}