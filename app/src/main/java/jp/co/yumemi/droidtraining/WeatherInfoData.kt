package jp.co.yumemi.droidtraining

import androidx.annotation.DrawableRes

/** 仮天気情報
 * TODO:正式な形にする
 */
data class WeatherInfoData(
    @DrawableRes val icon: Int,
    val weather: String,
    val lowestTemperature: Short,
    val highestTemperature: Short
)
