package com.example.tempo.repository

import com.example.tempo.constants.ConstantsCities
import com.example.tempo.security.SecurityPreferences

class RepositoryCities(private val securityPreferences: SecurityPreferences) {

    fun storeCity(id: String, city: String, state: String, latitude: String, longitude: String) {
        securityPreferences.run {
            storeString(ConstantsCities.CITY.ID, id)
            storeString(ConstantsCities.CITY.NOME, city)
            storeString(ConstantsCities.CITY.UF, state)
            storeString(ConstantsCities.CITY.LAT, latitude)
            storeString(ConstantsCities.CITY.LON, longitude)
        }
    }

    fun storeValueKey(key: String) {
        securityPreferences.run {
            storeString("key", key)
        }
    }
}