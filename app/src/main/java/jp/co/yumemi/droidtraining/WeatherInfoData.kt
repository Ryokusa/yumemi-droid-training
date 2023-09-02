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
    val weather: String,
    val lowestTemperature: Short,
    val highestTemperature: Short
) : Parcelable {

    companion object {
        private val ICONS: Map<String, Int> = mapOf(
            WeatherType.SUNNY.name to R.drawable.sunny,
            WeatherType.CLOUDY.name to R.drawable.cloudy,
            WeatherType.RAINY.name to R.drawable.rainy,
            WeatherType.SNOW.name to R.drawable.snow
        )
    }

    @IgnoredOnParcel
    @DrawableRes val icon: Int = ICONS[weather] ?: throw NullPointerException("無効なweatherです： $weather")
}
