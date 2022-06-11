package com.example.tempo.repository

import com.example.tempo.utils.ConstantsCidades
import com.example.tempo.utils.SecurityPreferences

class RepositoryCities(private val securityPreferences: SecurityPreferences) {

    fun storeCity(id: String, city: String, state: String) {
        securityPreferences.run {
            storeString(ConstantsCidades.CIDADES.ID, id)
            storeString(ConstantsCidades.CIDADES.NOME, city)
            storeString(ConstantsCidades.CIDADES.UF, state)
        }
    }
}