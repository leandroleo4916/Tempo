package com.example.tempo.repository

import androidx.lifecycle.liveData
import com.example.tempo.remote.ApiServiceTimeDay
import java.net.ConnectException

sealed class ResultRequest<out R> {
    data class Success<out T>(val dado: T?) : ResultRequest<T?>()
    data class Error(val exception: Exception) : ResultRequest<Nothing>()
    data class ErrorConnection(val exception: Exception) : ResultRequest<Nothing>()
}

class WeatherRepository(private val service: ApiServiceTimeDay) {

    fun weatherData(latitude: String, longitude: String) = liveData {
        try {
            val request = service.getWeatherDay(latitude, longitude)
            if(request.isSuccessful){
                emit(ResultRequest.Success(dado = request.body()))
            } else {
                emit(ResultRequest.Error(exception = Exception("Não foi possível conectar!")))
            }
        } catch (e: ConnectException) {
            emit(ResultRequest.ErrorConnection(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(ResultRequest.Error(exception = e))
        }
    }
}