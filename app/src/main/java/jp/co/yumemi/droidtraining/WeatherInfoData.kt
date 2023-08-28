package jp.co.yumemi.droidtraining

import androidx.annotation.DrawableRes

/** 仮天気情報
 * TODO:正式な形にする
 */
data class WeatherInfoData(
    val weather: String,
    val lowestTemperature: Short,
    val highestTemperature: Short
){
    companion object {
        private val ICONS: Map<String, Int> = mapOf(
            "sunny" to R.drawable.sunny,
            "cloudy" to R.drawable.cloudy,
            "rainy" to R.drawable.rainy,
            "snow" to R.drawable.snow
        )
    }

    @DrawableRes val icon: Int
    init {
        icon = ICONS[weather] ?: throw NullPointerException("無効なweatherです： $weather")
    }
}
