package jp.co.yumemi.droidtraining.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.droidtraining.R
import jp.co.yumemi.droidtraining.WeatherType
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/** 仮天気情報
 * TODO:正式な形にする
 */
@Parcelize
data class WeatherInfoData(
    val weather: WeatherType,
    val lowestTemperature: Short,
    val highestTemperature: Short,
    val place: String,
) : Parcelable {

    companion object {
        private val ICONS: Map<WeatherType, Int> = mapOf(
            WeatherType.SUNNY to R.drawable.sunny,
            WeatherType.CLOUDY to R.drawable.cloudy,
            WeatherType.RAINY to R.drawable.rainy,
            WeatherType.SNOW to R.drawable.snow
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
                    place = jsonWeatherInfoData.area
                )
            } catch (e: NoSuchElementException) {
                // 該当するWeatherTypeがない場合
                throw UnknownException()
            }
        }
    }

    @IgnoredOnParcel
    @DrawableRes
    val icon: Int =
        ICONS[weather] ?: throw NullPointerException("無効なweatherです： ${weather.weather}")
}
