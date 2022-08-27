package com.example.tempo.repository

import androidx.annotation.DrawableRes
import com.example.tempo.R

sealed class WeatherType(val weatherDesc: String, @DrawableRes val iconRes: Int,
                         @DrawableRes val iconTime: Int) {
    object ClearSky : WeatherType(
        weatherDesc = "Céu limpo",
        iconRes = R.drawable.ic_sunny,
        iconTime = R.drawable.ceu_limpo
    )
    object MainlyClear : WeatherType(
        weatherDesc = "Principalmente nublado",
        iconRes = R.drawable.ic_cloudy,
        iconTime = R.drawable.ceu_parcial_nublado
    )
    object PartlyCloudy : WeatherType(
        weatherDesc = "Parcialmente nublado",
        iconRes = R.drawable.ic_cloudy,
        iconTime = R.drawable.ceu_parcial_nublado
    )
    object Overcast : WeatherType(
        weatherDesc = "Nublado",
        iconRes = R.drawable.ic_cloudy,
        iconTime = R.drawable.ceu_nublado
    )
    object Foggy : WeatherType(
        weatherDesc = "Neblina",
        iconRes = R.drawable.ic_very_cloudy,
        iconTime = R.drawable.ceu_parcial_nublado
    )
    object DepositingRimeFog : WeatherType(
        weatherDesc = "Nevoeiro",
        iconRes = R.drawable.ic_very_cloudy,
        iconTime = R.drawable.ceu_parcial_nublado
    )
    object LightDrizzle : WeatherType(
        weatherDesc = "Chuvisco leve",
        iconRes = R.drawable.ic_rainshower,
        iconTime = R.drawable.ceu_chuvoso
    )
    object ModerateDrizzle : WeatherType(
        weatherDesc = "Chuvisco moderado",
        iconRes = R.drawable.ic_rainshower,
        iconTime = R.drawable.ceu_chuvoso
    )
    object DenseDrizzle : WeatherType(
        weatherDesc = "Chuvisco denso",
        iconRes = R.drawable.ic_rainshower,
        iconTime = R.drawable.ceu_chuvoso
    )
    object LightFreezingDrizzle : WeatherType(
        weatherDesc = "Garoa leve",
        iconRes = R.drawable.ic_snowyrainy,
        iconTime = R.drawable.ceu_chuvoso
    )
    object DenseFreezingDrizzle : WeatherType(
        weatherDesc = "Garoa densa",
        iconRes = R.drawable.ic_snowyrainy,
        iconTime = R.drawable.ceu_chuvoso
    )
    object SlightRain : WeatherType(
        weatherDesc = "Chuva leve",
        iconRes = R.drawable.ic_rainy,
        iconTime = R.drawable.ceu_chuvoso
    )
    object ModerateRain : WeatherType(
        weatherDesc = "Chuva moderada",
        iconRes = R.drawable.ic_rainy,
        iconTime = R.drawable.ceu_chuvoso
    )
    object HeavyRain : WeatherType(
        weatherDesc = "Chuva forte",
        iconRes = R.drawable.ic_rainy,
        iconTime = R.drawable.ceu_chuvoso
    )
    object HeavyFreezingRain: WeatherType(
        weatherDesc = "Chuva congelante",
        iconRes = R.drawable.ic_snowyrainy,
        iconTime = R.drawable.ceu_chuvoso
    )
    object SlightSnowFall: WeatherType(
        weatherDesc = "Ligeira queda de neve",
        iconRes = R.drawable.ic_snowy,
        iconTime = R.drawable.ceu_chuvoso
    )
    object ModerateSnowFall: WeatherType(
        weatherDesc = "Neve moderada",
        iconRes = R.drawable.ic_heavysnow,
        iconTime = R.drawable.ceu_chuvoso
    )
    object HeavySnowFall: WeatherType(
        weatherDesc = "Forte nevasca",
        iconRes = R.drawable.ic_heavysnow,
        iconTime = R.drawable.ceu_chuvoso
    )
    object SnowGrains: WeatherType(
        weatherDesc = "Grãos de neve",
        iconRes = R.drawable.ic_heavysnow,
        iconTime = R.drawable.ceu_chuvoso
    )
    object SlightRainShowers: WeatherType(
        weatherDesc = "Chuva leve",
        iconRes = R.drawable.ic_rainshower,
        iconTime = R.drawable.ceu_chuvoso
    )
    object ModerateRainShowers: WeatherType(
        weatherDesc = "Pacadas moderadas de chuva",
        iconRes = R.drawable.ic_rainshower,
        iconTime = R.drawable.ceu_chuvoso
    )
    object ViolentRainShowers: WeatherType(
        weatherDesc = "Pancadas fortes de chuva",
        iconRes = R.drawable.ic_rainshower,
        iconTime = R.drawable.ceu_chuvoso
    )
    object SlightSnowShowers: WeatherType(
        weatherDesc = "Nevasca leve",
        iconRes = R.drawable.ic_snowy,
        iconTime = R.drawable.ceu_chuvoso
    )
    object HeavySnowShowers: WeatherType(
        weatherDesc = "Neve pesada",
        iconRes = R.drawable.ic_snowy,
        iconTime = R.drawable.ceu_chuvoso
    )
    object ModerateThunderstorm: WeatherType(
        weatherDesc = "Tempestade moderada",
        iconRes = R.drawable.ic_thunder,
        iconTime = R.drawable.ceu_chuvoso
    )
    object SlightHailThunderstorm: WeatherType(
        weatherDesc = "Trovoada com granizo",
        iconRes = R.drawable.ic_rainythunder,
        iconTime = R.drawable.ceu_chuvoso
    )
    object HeavyHailThunderstorm: WeatherType(
        weatherDesc = "Trovoada forte com granizo",
        iconRes = R.drawable.ic_rainythunder,
        iconTime = R.drawable.ceu_chuvoso
    )

    companion object {
        fun weatherCode(code: Int): WeatherType {
            return when(code) {
                0 -> ClearSky
                1 -> MainlyClear
                2 -> PartlyCloudy
                3 -> Overcast
                45 -> Foggy
                48 -> DepositingRimeFog
                51 -> LightDrizzle
                53 -> ModerateDrizzle
                55 -> DenseDrizzle
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> SlightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingDrizzle
                67 -> HeavyFreezingRain
                71 -> SlightSnowFall
                73 -> ModerateSnowFall
                75 -> HeavySnowFall
                77 -> SnowGrains
                80 -> SlightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> SlightSnowShowers
                86 -> HeavySnowShowers
                95 -> ModerateThunderstorm
                96 -> SlightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> ClearSky
            }
        }
    }
}