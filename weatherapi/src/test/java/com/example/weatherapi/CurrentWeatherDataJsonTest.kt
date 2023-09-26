package com.example.weatherapi

import com.example.weatherapi.api.CurrentWeatherData
import junit.framework.TestCase.assertEquals
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Test

class CurrentWeatherDataJsonTest {

    private val currentWeatherDataInit = CurrentWeatherData(
        name = "Tokyo",
        id = 1850147L,
        timezone = 32400L,
        cod = 200L,
        coord = CurrentWeatherData.Coord(
            lon = 139.6917,
            lat = 35.6895,
        ),
        weather = listOf(
            CurrentWeatherData.Weather(
                id = 803L,
                main = "Clouds",
                description = "broken clouds",
                icon = "04n",
            ),
        ),
        base = "stations",
        visibility = 10000L,
        main = CurrentWeatherData.Main(
            temp = 289.5,
            feelsLike = 289.52,
            tempMin = 288.82,
            tempMax = 290.15,
            pressure = 1013L,
            humidity = 89L,
            seaLevel = 1020L,
            grndLevel = 1013L,
        ),
        wind = CurrentWeatherData.Wind(
            speed = 0.45,
            deg = 0L,
            gust = 1.34,
        ),
        clouds = CurrentWeatherData.Clouds(
            all = 75L,
        ),
        rain = CurrentWeatherData.Rain(
            the1H = 0.12,
            the3H = 0.12,
        ),
        snow = CurrentWeatherData.Snow(
            the1H = 0.12,
            the3H = 0.12,
        ),
        dt = 1630548912L,
        sys = CurrentWeatherData.Sys(
            type = 1L,
            id = 8074L,
            country = "JP",
            sunrise = 1630499316L,
            sunset = 1630544869L,
        ),
    )

    private fun makeJson(currentWeatherData: CurrentWeatherData): String {
        // nullableな部分
        val seaLevelJson = if (currentWeatherData.main.seaLevel != null) {
            """"sea_level": ${currentWeatherData.main.seaLevel},"""
        } else {
            ""
        }
        val grndLevelJson = if (currentWeatherData.main.grndLevel != null) {
            """"grnd_level": ${currentWeatherData.main.grndLevel},"""
        } else {
            ""
        }
        val rainJson = if (currentWeatherData.rain != null) {
            """"rain": {
                "1h": ${currentWeatherData.rain?.the1H},
                "3h": ${currentWeatherData.rain?.the3H}
            },"""
        } else {
            ""
        }
        val snowJson = if (currentWeatherData.snow != null) {
            """"snow": {
                "1h": ${currentWeatherData.snow?.the1H},
                "3h": ${currentWeatherData.snow?.the3H}
            },"""
        } else {
            ""
        }

        return """
        {
            "coord": {
                "lon": ${currentWeatherData.coord.lon},
                "lat": ${currentWeatherData.coord.lat}
            },
            "weather": [
                {
                    "id": ${currentWeatherData.weather[0].id},
                    "main": "${currentWeatherData.weather[0].main}",
                    "description": "${currentWeatherData.weather[0].description}",
                    "icon": "${currentWeatherData.weather[0].icon}"
                }
            ],
            "base": "${currentWeatherData.base}",
            "main": {
                "temp": ${currentWeatherData.main.temp},
                "feels_like": ${currentWeatherData.main.feelsLike},
                "temp_min": ${currentWeatherData.main.tempMin},
                "temp_max": ${currentWeatherData.main.tempMax},
                "pressure": ${currentWeatherData.main.pressure},
                $seaLevelJson
                $grndLevelJson
                "humidity": ${currentWeatherData.main.humidity}
            },
            "visibility": ${currentWeatherData.visibility},
            "wind": {
                "speed": ${currentWeatherData.wind.speed}
                "deg": ${currentWeatherData.wind.deg},
                "gust": ${currentWeatherData.wind.gust}
            },
            "clouds": {
                "all": ${currentWeatherData.clouds.all}
            },
            $rainJson
            $snowJson
            "dt": ${currentWeatherData.dt}
            "sys": {
                "type": ${currentWeatherData.sys.type},
                "id": ${currentWeatherData.sys.id},
                "country": "${currentWeatherData.sys.country}",
                "sunrise": ${currentWeatherData.sys.sunrise},
                "sunset": ${currentWeatherData.sys.sunset}
            },
            "timezone": ${currentWeatherData.timezone},
            "id": ${currentWeatherData.id},
            "name": "${currentWeatherData.name}",
            "cod": ${currentWeatherData.cod}
        }
        """.trimIndent()
    }

    @Test
    fun currentWeatherDataJson_decode_success() {
        val json = makeJson(currentWeatherDataInit)
        val currentWeatherData = Json.decodeFromString<CurrentWeatherData>(json)
        assertEquals(currentWeatherDataInit, currentWeatherData)
    }

    @Test
    fun currentWeatherDataJson_decode_success_without_rain() {
        // 雨要素を削除しておく
        val jsonWithoutRain = makeJson(currentWeatherDataInit.copy(rain = null))

        val currentWeatherData = Json.decodeFromString<CurrentWeatherData>(jsonWithoutRain)
        assert(currentWeatherData.rain == null)
    }

    @Test
    fun currentWeatherDataJson_decode_success_without_snow() {
        // 雪要素を削除しておく
        val jsonWithoutSnow = makeJson(currentWeatherDataInit.copy(snow = null))

        val currentWeatherData = Json.decodeFromString<CurrentWeatherData>(jsonWithoutSnow)
        assert(currentWeatherData.snow == null)
    }

    @Test
    fun currentWeatherDataJson_decode_success_without_rain_and_snow() {
        // 雨要素と雪要素を削除しておく
        val jsonWithoutRainAndSnow = makeJson(currentWeatherDataInit.copy(rain = null, snow = null))

        val currentWeatherData = Json.decodeFromString<CurrentWeatherData>(jsonWithoutRainAndSnow)
        assert(currentWeatherData.rain == null)
        assert(currentWeatherData.snow == null)
    }

    @Test
    fun currentWeatherDataJson_decode_success_without_seaLevel() {
        // 海面気圧要素を削除しておく
        val jsonWithoutSeaLevel =
            makeJson(currentWeatherDataInit.copy(main = currentWeatherDataInit.main.copy(seaLevel = null)))

        val currentWeatherData = Json.decodeFromString<CurrentWeatherData>(jsonWithoutSeaLevel)
        assert(currentWeatherData.main.seaLevel == null)
    }

    @Test
    fun currentWeatherDataJson_decode_success_without_grndLevel() {
        // 地上気圧要素を削除しておく
        val jsonWithoutGrndLevel =
            makeJson(currentWeatherDataInit.copy(main = currentWeatherDataInit.main.copy(grndLevel = null)))

        val currentWeatherData = Json.decodeFromString<CurrentWeatherData>(jsonWithoutGrndLevel)
        assert(currentWeatherData.main.grndLevel == null)
    }

    @Test
    fun currentWeatherDataJson_decode_success_without_seaLevel_and_grndLevel() {
        // 海面気圧要素と地上気圧要素を削除しておく
        val jsonWithoutSeaLevelAndGrndLevel = makeJson(
            currentWeatherDataInit.copy(
                main = currentWeatherDataInit.main.copy(
                    seaLevel = null,
                    grndLevel = null,
                ),
            ),
        )

        val currentWeatherData =
            Json.decodeFromString<CurrentWeatherData>(jsonWithoutSeaLevelAndGrndLevel)
        assert(currentWeatherData.main.seaLevel == null)
        assert(currentWeatherData.main.grndLevel == null)
    }

    @Test(expected = SerializationException::class)
    fun currentWeatherDataJson_decode_failed_by_typeError() {
        // 文字列型要素を数値型に変換しておく
        val json = makeJson(currentWeatherDataInit)
        val failJson = json.replace(""""Tokyo"""", "2")

        Json.decodeFromString<CurrentWeatherData>(failJson)
    }

    @Test(expected = SerializationException::class)
    fun currentWeatherDataJson_decode_failed_by_missingRequiredField() {
        // 必須要素を削除しておく
        val json = makeJson(currentWeatherDataInit)
        val failJson = json.replace("""name": "Tokyo"""", "")

        Json.decodeFromString<CurrentWeatherData>(failJson)
    }
}
