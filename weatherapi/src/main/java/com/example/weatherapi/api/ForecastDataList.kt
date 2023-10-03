package com.example.weatherapi.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastDataList(
    val cod: String,
    val message: Long,
    val cnt: Long,
    val list: List<ForecastData>,
    val city: City,
) {
    @Serializable
    data class ForecastData(
        val dt: Long,
        val main: CurrentWeatherData.Main,
        val weather: List<CurrentWeatherData.Weather>,
        val clouds: CurrentWeatherData.Clouds,
        val wind: CurrentWeatherData.Wind,
        val visibility: Long,
        val pop: Double,
        val rain: CurrentWeatherData.Rain? = null,
        val snow: CurrentWeatherData.Snow? = null,
        val sys: Sys,
        @SerialName("dt_txt")
        val dtText: String,
    )

    @Serializable
    data class City(
        val id: Long,
        val name: String,
        val coord: CurrentWeatherData.Coord,
        val country: String,
        val population: Long,
        val timezone: Long,
        val sunrise: Long,
        val sunset: Long,
    )

    @Serializable
    data class Sys(
        val pod: String,
    )
}
