package com.example.tempo.repository

import androidx.lifecycle.liveData
import com.example.tempo.remote.ApiServiceMain
import java.net.ConnectException

sealed class ResultadoMain<out R> {
    data class Success<out T>(val dado: T?) : ResultadoMain<T>()
    data class Error(val exception: Exception) : ResultadoMain<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultadoMain<Nothing>()
}

class RepositoryMain(private val serviceMain: ApiServiceMain) {

    fun getTempoMain(id: String) = liveData {
        try {
            val response = serviceMain.tempo(id)
            if (response.isSuccessful) {
                emit(ResultadoMain.Success(response.body()))
            } else {
                emit(ResultadoMain.Error(Exception()))
            }
        } catch (e: ConnectException) {
            emit(ResultadoMain.ErrorConnection(e))
        } catch (e: Exception) {
            emit(ResultadoMain.Error(e))
        }
    }
}