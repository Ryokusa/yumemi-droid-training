package com.example.weatherapi.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherData(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Long,
    val wind: Wind,
    val rain: Rain? = null,
    val clouds: Clouds,
    val snow: Snow? = null,
    val dt: Long,
    val sys: Sys,
    val timezone: Long,
    val id: Long,
    val name: String,
    val cod: Long,
) {

    @Serializable
    data class Rain(
        @SerialName("1h")
        val the1H: Double? = null,

        @SerialName("3h")
        val the3H: Double? = null,
    )

    @Serializable
    data class Snow(
        @SerialName("1h")
        val the1H: Double? = null,

        @SerialName("3h")
        val the3H: Double? = null,
    )

    @Serializable
    data class Sys(
        val type: Long,
        val id: Long,
        val country: String,
        val sunrise: Long,
        val sunset: Long,
    )

    @Serializable
    data class Weather(
        val id: Long,
        val main: String,
        val description: String,
        val icon: String,
    )

    @Serializable
    data class Wind(
        val speed: Double,
        val deg: Long,
        val gust: Double,
    )

    @Serializable
    data class Clouds(
        val all: Long,
    )

    @Serializable
    data class Coord(
        val lon: Double,
        val lat: Double,
    )

    @Serializable
    data class Main(
        val temp: Double,

        @SerialName("feels_like")
        val feelsLike: Double,

        @SerialName("temp_min")
        val tempMin: Double,

        @SerialName("temp_max")
        val tempMax: Double,

        val pressure: Long,
        val humidity: Long,

        @SerialName("sea_level")
        val seaLevel: Long? = null,

        @SerialName("grnd_level")
        val grndLevel: Long? = null,
    )
}
