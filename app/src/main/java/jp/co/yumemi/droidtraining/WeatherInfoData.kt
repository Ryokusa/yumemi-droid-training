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
            "sunny" to R.drawable.sunny,
            "cloudy" to R.drawable.cloudy,
            "rainy" to R.drawable.rainy,
            "snow" to R.drawable.snow
        )
    }

    @IgnoredOnParcel
    @DrawableRes val icon = ICONS[weather] ?: throw NullPointerException("無効なweatherです： $weather")
}
