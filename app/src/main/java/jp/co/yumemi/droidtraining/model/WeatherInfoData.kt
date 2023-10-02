package jp.co.yumemi.droidtraining.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.example.weatherapi.api.CurrentWeatherData
import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.droidtraining.R
import jp.co.yumemi.droidtraining.WeatherType
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import kotlin.math.roundToInt

/** 仮天気情報
 * TODO:正式な形にする
 */
@Parcelize
data class WeatherInfoData(
    val weather: WeatherType,
    val lowestTemperature: Short,
    val highestTemperature: Short,
    val place: String,
    val temperature: Short,
    val dateTime: LocalDateTime,
) : Parcelable {

    companion object {
        private val ICONS: Map<WeatherType, Int> = mapOf(
            WeatherType.SUNNY to R.drawable.sunny,
            WeatherType.CLOUDY to R.drawable.cloudy,
            WeatherType.RAINY to R.drawable.rainy,
            WeatherType.SNOW to R.drawable.snow,
        )

        /**
         * Json形式の天気情報からWeatherInfoDataを生成
         * @arg jsonWeatherInfoData Json形式の天気情報
         * @throws UnknownException 不明な天気だった場合
         */
        operator fun invoke(jsonWeatherInfoData: JsonWeatherInfoData): WeatherInfoData {
            try {
                val newWeather = WeatherType.of(jsonWeatherInfoData.weather)
                return WeatherInfoData(
                    weather = newWeather,
                    highestTemperature = jsonWeatherInfoData.maxTemp.toShort(),
                    lowestTemperature = jsonWeatherInfoData.minTemp.toShort(),
                    place = jsonWeatherInfoData.area,
                    temperature = 255,
                    dateTime = LocalDateTime.now(),
                )
            } catch (e: NoSuchElementException) {
                // 該当するWeatherTypeがない場合
                throw UnknownException()
            }
        }

        /**
         * CurrentWeatherDataからの変換
         * @throws UnknownException 不明な天気の場合
         */
        operator fun invoke(currentWeatherData: CurrentWeatherData): WeatherInfoData {
            val weatherType = WeatherType.fromCurrentWeatherDataId(currentWeatherData.weather[0].id)
            val highestTemperature = currentWeatherData.main.tempMax.roundToInt().toShort()
            val lowestTemperature = currentWeatherData.main.tempMin.roundToInt().toShort()
            val place = currentWeatherData.name
            val temp = currentWeatherData.main.temp.roundToInt().toShort()
            val dateTime = LocalDateTime.parse(currentWeatherData.dt.toString())
            return WeatherInfoData(
                weather = weatherType,
                highestTemperature = highestTemperature,
                lowestTemperature = lowestTemperature,
                place = place,
                temperature = temp,
                dateTime = dateTime,
            )
        }
    }

    @IgnoredOnParcel
    @DrawableRes
    val icon: Int =
        ICONS[weather] ?: throw NullPointerException("無効なweatherです： ${weather.id}")
}
