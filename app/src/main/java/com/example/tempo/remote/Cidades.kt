package com.example.tempo.remote

import com.google.gson.annotations.SerializedName

data class Cidades(
    val id: String,
    val nome: String,
    val microrregiao: Microrregiao,

    @SerializedName("regiao-imediata")
    val regiaoImediata: RegiaoImediata
)

data class Microrregiao(
    val id: Long,
    val nome: String,
    val mesorregiao: Mesorregiao
)

data class Mesorregiao(
    val id: Long,
    val nome: String,

    @SerializedName("UF")
    val uf: Uf
)

data class Uf(
    val id: Long,
    val sigla: String,
    val nome: String,
    val regiao: Uf? = null
)

data class RegiaoImediata(
    val id: Long,
    val nome: String,

    @SerializedName("regiao-intermediaria")
    val regiaoIntermediaria: Mesorregiao
)
