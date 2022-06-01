package com.example.tempo.repository

import com.example.tempo.utils.ConstantsCidades
import com.example.tempo.utils.SecurityPreferences

class RepositoryCidades(private val securityPreferences: SecurityPreferences) {

    fun storeCidade(id: String, cidade: String, state: String) {
        securityPreferences.run {
            storeString(ConstantsCidades.CIDADES.ID, id)
            storeString(ConstantsCidades.CIDADES.NOME, cidade)
            storeString(ConstantsCidades.CIDADES.UF, state)
        }
    }
}