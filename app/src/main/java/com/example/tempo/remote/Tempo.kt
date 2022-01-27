package com.example.tempo.remote

import com.google.gson.annotations.SerializedName

data class Welcome (
    @SerializedName("2702108")
    val the2702108: The2702108
)

data class The2702108 (
    @SerializedName("27/01/2022")
    val the27012022: The012022,

    @SerializedName("28/01/2022")
    val the28012022: The012022,

    @SerializedName("29/01/2022")
    val the29012022: The29012022,

    @SerializedName("30/01/2022")
    val the30012022: The29012022,

    @SerializedName("31/01/2022")
    val the31012022: The29012022
)

data class The012022 (
    val manha: The29012022,
    val tarde: The29012022,
    val noite: The29012022
)

data class The29012022 (
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
