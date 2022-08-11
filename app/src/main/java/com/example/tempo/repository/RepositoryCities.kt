package com.example.tempo.repository

import com.example.tempo.constants.ConstantsCities
import com.example.tempo.dataclass.CityData
import com.example.tempo.security.SecurityPreferences

class RepositoryCities(private val securityPreferences: SecurityPreferences) {

    fun storeCity(item: CityData) {
        securityPreferences.run {
            storeString(ConstantsCities.CITY.ID, item.idCity)
            storeString(ConstantsCities.CITY.NOME, item.city)
            storeString(ConstantsCities.CITY.UF, item.state)
            storeString(ConstantsCities.CITY.LAT, item.latitude)
            storeString(ConstantsCities.CITY.LON, item.longitude)
        }
    }
}