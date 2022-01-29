package com.example.tempo.repository

sealed class ResultadoMain<out R> {
    data class Success<out T>(val dado: T?) : ResultadoMain<T>()
    data class Error(val exception: Exception) : ResultadoMain<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultadoMain<Nothing>()
}
