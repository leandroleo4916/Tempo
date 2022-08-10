
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

    val hourly: Hourly
)

data class CurrentWeather (
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Long,
    val weathercode: Long,
    val time: String
)

data class Hourly (
    val time: List<String>,

    @SerializedName("temperature_2m")
    val temperature2M: List<Double>
)

data class HourlyUnits (
    val time: String,

    @SerializedName("temperature_2m")
    val temperature2M: String
)
