package jp.co.yumemi.droidtraining.model

import kotlinx.serialization.Serializable

@Serializable
data class JsonWeatherInfoData(
    val weather: String,
    val maxTemp: Int,
    val minTemp: Int,
    val date: String,
    val area: String,
)
