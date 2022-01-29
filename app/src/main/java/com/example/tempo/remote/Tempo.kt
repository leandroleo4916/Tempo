package com.example.tempo.remote

import com.google.gson.annotations.SerializedName

data class Tempo (
    val data1: Map<String, Periodo>,
    val data2: Map<String, Periodo>,
    val data3: Map<String, Map<String, InfoCidade>>,
    val data4: Map<String, Map<String, InfoCidade>>,
    val data5: Map<String, Map<String, InfoCidade>>
)

data class Periodo (
    val manha: InfoCidade,
    val tarde: InfoCidade,
    val noite: InfoCidade
)

data class InfoCidade (
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
