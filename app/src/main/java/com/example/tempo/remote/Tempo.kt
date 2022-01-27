package com.example.tempo.remote

import com.google.gson.annotations.SerializedName

data class Tempo (
    val tempo: Date
)

data class Date (
    val hoje: Turno,
    val amanha: Turno,
    val data3: Info,
    val data4: Info,
    val data5: Info
)

data class Turno (
    val manha: Info,
    val tarde: Info,
    val noite: Info
)

data class Info (
    val uf: String,
    val entidade: String,
    val resumo: String,
    val tempo: String,

    @SerializedName("temp_max")
    val tempMax: Long,

    @SerializedName("temp_min")
    val tempMin: Long,

    @SerializedName("dir_vento")
    val dirVento: String,

    @SerializedName("int_vento")
    val intVento: String,

    @SerializedName("cod_icone")
    val codIcone: String,

    val icone: String,

    @SerializedName("dia_semana")
    val diaSemana: String,

    @SerializedName("umidade_max")
    val umidadeMax: Long,

    @SerializedName("umidade_min")
    val umidadeMin: Long,

    @SerializedName("temp_max_tende")
    val tempMaxTende: String,

    @SerializedName("cod_temp_max_tende_icone")
    val codTempMaxTendeIcone: String,

    @SerializedName("temp_max_tende_icone")
    val tempMaxTendeIcone: String,

    @SerializedName("temp_min_tende")
    val tempMinTende: String,

    @SerializedName("cod_temp_min_tende_icone")
    val codTempMinTendeIcone: String,

    @SerializedName("temp_min_tende_icone")
    val tempMinTendeIcone: String,

    val estacao: String,
    val hora: String,
    val nascer: String,
    val ocaso: String,
    val fonte: String
)

