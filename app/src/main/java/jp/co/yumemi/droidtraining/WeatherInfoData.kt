package jp.co.yumemi.droidtraining

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/** 仮天気情報
 * TODO:正式な形にする
 */
@Parcelize
data class WeatherInfoData(
    val weather: WeatherType,
    val lowestTemperature: Short,
    val highestTemperature: Short
) : Parcelable {

    companion object {
        private val ICONS: Map<WeatherType, Int> = mapOf(
            WeatherType.SUNNY to R.drawable.sunny,
            WeatherType.CLOUDY to R.drawable.cloudy,
            WeatherType.RAINY to R.drawable.rainy,
            WeatherType.SNOW to R.drawable.snow
        )
    }

    @IgnoredOnParcel
    @DrawableRes val icon: Int = ICONS[weather] ?: throw NullPointerException("無効なweatherです： ${weather.weather}")
}
