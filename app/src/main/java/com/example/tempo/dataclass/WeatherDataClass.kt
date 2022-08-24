
import com.google.gson.annotations.SerializedName

data class WeatherDataClass (
    val latitude: Double,
    val longitude: Double,

    @SerializedName("generationtime_ms")
    val generationtimeMS: Double,

    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Long,

    val timezone: String,

    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,

    val elevation: Long,

    @SerializedName("current_weather")
    val currentWeather: CurrentWeather,

    @SerializedName("hourly_units")
    val hourlyUnits: HourlyUnits,

    val hourly: Hourly,

    @SerializedName("daily_units")
    val dailyUnits: DailyUnits,

    val daily: Daily
)

data class CurrentWeather (
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Long,
    val weathercode: Long,
    val time: String
)

data class Daily (
    val time: List<String>,
    val weathercode: List<Long>,

    @SerializedName("temperature_2m_max")
    val temperature2MMax: List<Double>,

    @SerializedName("temperature_2m_min")
    val temperature2MMin: List<Double>,

    val sunrise: List<String>,
    val sunset: List<String>,

    @SerializedName("rain_sum")
    val rainSum: List<Double>,

    @SerializedName("windspeed_10m_max")
    val windspeed10MMax: List<Double>
)

data class DailyUnits (
    val time: String,
    val weathercode: String,

    @SerializedName("temperature_2m_max")
    val temperature2MMax: String,

    @SerializedName("temperature_2m_min")
    val temperature2MMin: String,

    val sunrise: String,
    val sunset: String,

    @SerializedName("rain_sum")
    val rainSum: String,

    @SerializedName("windspeed_10m_max")
    val windspeed10MMax: String
)

data class Hourly (
    val time: List<String>,

    @SerializedName("temperature_2m")
    val temperature2M: List<Double>,

    @SerializedName("relativehumidity_2m")
    val relativehumidity2M: List<Long>,

    @SerializedName("apparent_temperature")
    val apparentTemperature: List<Double>,

    val showers: List<Double>,
    val weathercode: List<Long>
)

data class HourlyUnits (
    val time: String,

    @SerializedName("temperature_2m")
    val temperature2M: String,

    @SerializedName("relativehumidity_2m")
    val relativehumidity2M: String,

    @SerializedName("apparent_temperature")
    val apparentTemperature: String,

    val showers: String,
    val weathercode: String
)
