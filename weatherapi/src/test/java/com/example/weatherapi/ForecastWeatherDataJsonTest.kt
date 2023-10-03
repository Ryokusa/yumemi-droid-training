package com.example.weatherapi

import com.example.weatherapi.api.CurrentWeatherData
import com.example.weatherapi.api.ForecastDataList
import junit.framework.TestCase.assertEquals
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Test

class ForecastWeatherDataJsonTest {

    private val fakeForecastDataList: ForecastDataList = ForecastDataList(
        cod = "200",
        message = 1,
        cnt = 40,
        list = listOf(
            ForecastDataList.ForecastData(
                dt = 1620034215,
                main = CurrentWeatherData.Main(
                    temp = 25.0,
                    feelsLike = 2.0,
                    tempMin = 10.0,
                    tempMax = 29.0,
                    pressure = 2,
                    humidity = 4,
                ),
                weather = listOf(
                    CurrentWeatherData.Weather(
                        id = 500,
                        main = "Rain",
                        description = "light rain",
                        icon = "10d",
                    )
                ),
                clouds = CurrentWeatherData.Clouds(
                    all = 100,
                ),
                wind = CurrentWeatherData.Wind(
                    speed = 0.62,
                    deg = 349,
                    gust = 1.18,
                ),
                visibility = 10000,
                pop = 0.32,
                rain = CurrentWeatherData.Rain(
                    the3H = 0.26,
                ),
                snow = CurrentWeatherData.Snow(
                    the3H = 0.0,
                ),
                sys = ForecastDataList.Sys(
                    pod = "d",
                ),
                dtText = "2022-08-30 15:00:00",
            ),
            ForecastDataList.ForecastData(
                dt = 1620000000,
                main = CurrentWeatherData.Main(
                    temp = 0.0,
                    feelsLike = 0.0,
                    tempMin = 0.0,
                    tempMax = 0.0,
                    pressure = 0,
                    humidity = 0,
                ),
                weather = listOf(
                    CurrentWeatherData.Weather(
                        id = 0,
                        main = "",
                        description = "",
                        icon = "",
                    )
                ),
                clouds = CurrentWeatherData.Clouds(
                    all = 0,
                ),
                wind = CurrentWeatherData.Wind(
                    speed = 0.0,
                    deg = 0,
                    gust = 0.0,
                ),
                visibility = 0,
                pop = 0.0,
                rain = CurrentWeatherData.Rain(
                    the3H = 0.0,
                ),
                snow = CurrentWeatherData.Snow(
                    the3H = 0.0,
                ),
                sys = ForecastDataList.Sys(
                    pod = "d",
                ),
                dtText = "",
            )
        ),
        city = ForecastDataList.City(
            id = 3163858,
            name = "3163858",
            coord = CurrentWeatherData.Coord(
                lon = 44.34,
                lat = 10.99,
            ),
            country = "IT",
            population = 4593,
            timezone = 7200,
            sunrise = 166183417,
            sunset = 166182248,
        ),
    )

    private fun makeJson(forecastDataList: ForecastDataList): String {
        var listJson: String = """"list": [
            """.trimIndent()
        for (forecastData in forecastDataList.list) {
            val seaLevelJson = if (forecastData.main.seaLevel == null) {
                ""
            } else {
                """
                    "sea_level": ${forecastData.main.seaLevel},
                """.trimIndent()
            }
            val grndLevelJson = if (forecastData.main.grndLevel == null) {
                ""
            } else {
                """
                    "grnd_level": ${forecastData.main.grndLevel},
                """.trimIndent()
            }
            val rain3HJson = if (forecastData.rain?.the3H == null) {
                ""
            } else {
                """"rain": {
                    "3h": ${forecastData.rain?.the3H}
                },""".trimIndent()
            }

            val snow3HJson = if (forecastData.snow?.the3H == null) {
                ""
            } else {
                """"snow": {
                    "3h": ${forecastData.snow?.the3H}
                },""".trimIndent()
            }

            listJson = """
                $listJson
                {
                    "dt": ${forecastData.dt},
                    "main": {
                        "temp": ${forecastData.main.temp},
                        "feels_like": ${forecastData.main.feelsLike},
                        "temp_min": ${forecastData.main.tempMin},
                        "temp_max": ${forecastData.main.tempMax},
                        "pressure": ${forecastData.main.pressure},
                        $seaLevelJson
                        $grndLevelJson
                        "humidity": ${forecastData.main.humidity}
                    },
                    "weather": [
                        {
                            "id": ${forecastData.weather[0].id},
                            "main": "${forecastData.weather[0].main}",
                            "description": "${forecastData.weather[0].description}",
                            "icon": "${forecastData.weather[0].icon}"
                        }
                    ],
                    "clouds": {
                        "all": ${forecastData.clouds.all}
                    },
                    "wind": {
                        "speed": ${forecastData.wind.speed},
                        "deg": ${forecastData.wind.deg},
                        "gust": ${forecastData.wind.gust}
                    },
                    "visibility": ${forecastData.visibility},
                    "pop": ${forecastData.pop},
                    $rain3HJson
                    $snow3HJson
                    "sys": {
                        "pod": "${forecastData.sys.pod}"
                    },
                    "dt_txt": "${forecastData.dtText}"
                },
            """.trimIndent()
        }
        listJson = """${listJson.substring(0, listJson.length - 1)}
            ],""".trimIndent()
        val json = """
            {
                "cod": "${forecastDataList.cod}",
                "message": ${forecastDataList.message},
                "cnt": ${forecastDataList.cnt},
                $listJson
                "city": {
                    "id": ${forecastDataList.city.id},
                    "name": "${forecastDataList.city.name}",
                    "coord": {
                        "lon": ${forecastDataList.city.coord.lon},
                        "lat": ${forecastDataList.city.coord.lat}
                    },
                    "country": "${forecastDataList.city.country}",
                    "population": ${forecastDataList.city.population},
                    "timezone": ${forecastDataList.city.timezone},
                    "sunrise": ${forecastDataList.city.sunrise},
                    "sunset": ${forecastDataList.city.sunset}
                }
            }
        """.trimIndent()
        return json
    }

    @Test
    fun forecastDataListJson_decode_success() {
        val json = makeJson(fakeForecastDataList)
        val forecastDataList = Json.decodeFromString<ForecastDataList>(json)
        assertEquals(fakeForecastDataList, forecastDataList)
    }

    @Test
    fun forecastDataListJson_decode_success_without_rain() {
        val withoutRain = fakeForecastDataList.copy(list = fakeForecastDataList.list.map {
            it.copy(rain = null)
        })
        val json = makeJson(withoutRain)
        val forecastDataList = Json.decodeFromString<ForecastDataList>(json)
        assertEquals(withoutRain, forecastDataList)
    }

    @Test
    fun forecastDataListJson_decode_success_without_snow() {
        val withoutSnow = fakeForecastDataList.copy(list = fakeForecastDataList.list.map {
            it.copy(snow = null)
        })
        val json = makeJson(withoutSnow)
        val forecastDataList = Json.decodeFromString<ForecastDataList>(json)
        assertEquals(withoutSnow, forecastDataList)
    }

    @Test
    fun forecastDataListJson_decode_success_without_rain_and_snow() {
        val withoutRainAndSnow = fakeForecastDataList.copy(list = fakeForecastDataList.list.map {
            it.copy(rain = null, snow = null)
        })
        val json = makeJson(withoutRainAndSnow)
        val forecastDataList = Json.decodeFromString<ForecastDataList>(json)
        assertEquals(withoutRainAndSnow, forecastDataList)
    }

    @Test
    fun forecastDataListJson_decode_success_without_seaLevel() {
        val withoutSeaLevel = fakeForecastDataList.copy(list = fakeForecastDataList.list.map {
            it.copy(main = it.main.copy(seaLevel = null))
        })
        val json = makeJson(withoutSeaLevel)
        val forecastDataList = Json.decodeFromString<ForecastDataList>(json)
        assertEquals(withoutSeaLevel, forecastDataList)
    }

    @Test
    fun forecastDataListJson_decode_success_without_grndLevel() {
        val withoutGrndLevel = fakeForecastDataList.copy(list = fakeForecastDataList.list.map {
            it.copy(main = it.main.copy(grndLevel = null))
        })
        val json = makeJson(withoutGrndLevel)
        val forecastDataList = Json.decodeFromString<ForecastDataList>(json)
        assertEquals(withoutGrndLevel, forecastDataList)
    }

    @Test
    fun forecastDataListJson_decode_success_without_seaLevel_and_grndLevel() {
        val withoutSeaLevelAndGrndLevel =
            fakeForecastDataList.copy(list = fakeForecastDataList.list.map {
                it.copy(main = it.main.copy(seaLevel = null, grndLevel = null))
            })
        val json = makeJson(withoutSeaLevelAndGrndLevel)
        val forecastDataList = Json.decodeFromString<ForecastDataList>(json)
        assertEquals(withoutSeaLevelAndGrndLevel, forecastDataList)
    }

    @Test
    fun forecastDataListJson_decode_success_without_rain_and_snow_and_seaLevel_and_grndLevel() {
        val withoutRainAndSnowAndSeaLevelAndGrndLevel =
            fakeForecastDataList.copy(list = fakeForecastDataList.list.map {
                it.copy(
                    rain = null,
                    snow = null,
                    main = it.main.copy(seaLevel = null, grndLevel = null)
                )
            })
        val json = makeJson(withoutRainAndSnowAndSeaLevelAndGrndLevel)
        val forecastDataList = Json.decodeFromString<ForecastDataList>(json)
        assertEquals(withoutRainAndSnowAndSeaLevelAndGrndLevel, forecastDataList)
    }

    @Test(expected = SerializationException::class)
    fun forecastDataListJson_decode_failed_by_typeError() {
        val json = makeJson(fakeForecastDataList)
        val failJson = json.replace(""""Rain"""", "Rain")
        Json.decodeFromString<ForecastDataList>(failJson)
    }

    @Test(expected = SerializationException::class)
    fun forecastDataListJson_decode_failed_by_missingRequireField() {
        val json = makeJson(fakeForecastDataList)
        val failJson = json.replace(""""dt"""", "")
        Json.decodeFromString<ForecastDataList>(failJson)
    }
}
