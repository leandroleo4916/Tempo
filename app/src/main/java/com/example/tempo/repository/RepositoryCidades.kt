package com.example.tempo.repository

import com.example.tempo.activity.utils.ConstantsCidades
import com.example.tempo.activity.utils.SecurityPreferences

class RepositoryCidades(private val securityPreferences: SecurityPreferences) {

    fun setCidade(id: String, nome: String) {
        securityPreferences.run {
            storeString(ConstantsCidades.CIDADES.ID, id)
            storeString(ConstantsCidades.CIDADES.NOME, nome)
        }
    }

    fun storeCidade(id: String, cidade: String) {
        securityPreferences.run {
            storeString(ConstantsCidades.CIDADES.ID, id)
            storeString(ConstantsCidades.CIDADES.NOME, cidade)
        }
    }
}