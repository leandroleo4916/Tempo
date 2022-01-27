package com.example.tempo.repository

import androidx.lifecycle.liveData
import com.example.tempo.remote.ApiServiceSearch
import java.net.ConnectException

sealed class Resultado<out R> {
    data class Sucesso<out T>(val dado: T?) : Resultado<T>()
    data class Erro(val exception: Exception) : Resultado<Nothing>()
    data class ErroConnection(val exception: Exception) : Resultado<Nothing>()
}

class RepositorySearch(private val serviceSearch: ApiServiceSearch) {

    fun getCity() = liveData {
        try {
            val response = serviceSearch.cidades()
            if (response.isSuccessful) {
                emit(Resultado.Sucesso(response.body()))
            } else {
                emit(Resultado.Erro(Exception()))
            }
        } catch (e: ConnectException) {
            emit(Resultado.ErroConnection(e))
        } catch (e: Exception) {
            emit(Resultado.Erro(e))
        }
    }
}