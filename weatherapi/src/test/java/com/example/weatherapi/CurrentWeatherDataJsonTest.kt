package com.example.weatherapi

import com.example.weatherapi.api.CurrentWeatherData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Test

class CurrentWeatherDataJsonTest {

    private val json = """
            {
                "coord": {
                    "lon": 139.6917,
                    "lat": 35.6895
                },
                "weather": [
                    {
                        "id": 803,
                        "main": "Clouds",
                        "description": "broken clouds",
                        "icon": "04n"
                    }
                ],
                "base": "stations",
                "main": {
                    "temp": 289.5,
                    "feels_like": 289.52,
                    "temp_min": 288.82,
                    "temp_max": 290.15,
                    "pressure": 1013,
                    "humidity": 89
                },
                "visibility": 10000,
                "wind": {
                    "speed": 0.45,
                    "deg": 0,
                    "gust": 1.34
                },
                "clouds": {
                    "all": 75
                },
                "rain": {
                    "1h": 0.12,
                    "3h": 0.12
                },
                "snow": {
                    "1h": 0.12,
                    "3h": 0.12
                },
                "dt": 1630548912,
                "sys": {
                    "type": 1,
                    "id": 8074,
                    "country": "JP",
                    "sunrise": 1630499316,
                    "sunset": 1630544869
                },
                "timezone": 32400,
                "id": 1850147,
                "name": "Tokyo",
                "cod": 200
            }
        """.trimIndent().replace(Regex("""\r\n|\n|\r"""), "")

    @Test
    fun currentWeatherDataJson_decode_success() {
        val currentWeatherData = Json.decodeFromString<CurrentWeatherData>(json)
        assert(currentWeatherData.name == "Tokyo")
        assert(currentWeatherData.id == 1850147L)
        assert(currentWeatherData.timezone == 32400L)
        assert(currentWeatherData.cod == 200L)
        assert(currentWeatherData.coord.lon == 139.6917)
        assert(currentWeatherData.coord.lat == 35.6895)
        assert(currentWeatherData.weather[0].description == "broken clouds")
        assert(currentWeatherData.weather[0].id == 803L)
        assert(currentWeatherData.weather[0].main == "Clouds")
        assert(currentWeatherData.weather[0].icon == "04n")
        assert(currentWeatherData.base == "stations")
        assert(currentWeatherData.visibility == 10000L)
        assert(currentWeatherData.main.temp == 289.5)
        assert(currentWeatherData.main.feelsLike == 289.52)
        assert(currentWeatherData.main.tempMin == 288.82)
        assert(currentWeatherData.main.tempMax == 290.15)
        assert(currentWeatherData.main.pressure == 1013L)
        assert(currentWeatherData.main.humidity == 89L)
        assert(currentWeatherData.wind.speed == 0.45)
        assert(currentWeatherData.wind.deg == 0L)
        assert(currentWeatherData.clouds.all == 75L)
        assert(currentWeatherData.dt == 1630548912L)
        assert(currentWeatherData.sys.type == 1L)
        assert(currentWeatherData.sys.id == 8074L)
        assert(currentWeatherData.sys.country == "JP")
        assert(currentWeatherData.sys.sunrise == 1630499316L)
        assert(currentWeatherData.sys.sunset == 1630544869L)
    }

    @Test
    fun currentWeatherDataJson_decode_success_without_rain() {
        // 雨要素を削除しておく
        val jsonWithoutRain = json.replace(Regex(""""rain": .*?},""".trimMargin()), "")

        val currentWeatherData = Json.decodeFromString<CurrentWeatherData>(jsonWithoutRain)
        assert(currentWeatherData.rain == null)
    }

    @Test
    fun currentWeatherDataJson_decode_success_without_snow() {
        // 雪要素を削除しておく
        val jsonWithoutSnow = json.replace(Regex(""""snow": .*?},""".trimMargin()), "")

        val currentWeatherData = Json.decodeFromString<CurrentWeatherData>(jsonWithoutSnow)
        assert(currentWeatherData.snow == null)
    }

    @Test
    fun currentWeatherDataJson_decode_success_without_rain_and_snow() {
        // 雨要素と雪要素を削除しておく
        val jsonWithoutRainAndSnow = json
            .replace(Regex(""""rain": .*?},""".trimMargin()), "")
            .replace(Regex(""""snow": .*?},""".trimMargin()), "")

        val currentWeatherData = Json.decodeFromString<CurrentWeatherData>(jsonWithoutRainAndSnow)
        assert(currentWeatherData.rain == null)
        assert(currentWeatherData.snow == null)
    }

    @Test
    fun currentWeatherDataJson_decode_success_without_seaLevel() {
        // 海面気圧要素を削除しておく
        val jsonWithoutSeaLevel = json.replace("""sea_level": .*,""", "")

        val currentWeatherData = Json.decodeFromString<CurrentWeatherData>(jsonWithoutSeaLevel)
        assert(currentWeatherData.main.seaLevel == null)
    }

    @Test
    fun currentWeatherDataJson_decode_success_without_grndLevel() {
        // 地上気圧要素を削除しておく
        val jsonWithoutGrndLevel = json.replace("""grnd_level": .*,""", "")

        val currentWeatherData = Json.decodeFromString<CurrentWeatherData>(jsonWithoutGrndLevel)
        assert(currentWeatherData.main.grndLevel == null)
    }

    @Test
    fun currentWeatherDataJson_decode_success_without_seaLevel_and_grndLevel() {
        // 海面気圧要素と地上気圧要素を削除しておく
        val jsonWithoutSeaLevelAndGrndLevel = json
            .replace("""sea_level": .*,""", "")
            .replace("""grnd_level": .*,""", "")

        val currentWeatherData =
            Json.decodeFromString<CurrentWeatherData>(jsonWithoutSeaLevelAndGrndLevel)
        assert(currentWeatherData.main.seaLevel == null)
        assert(currentWeatherData.main.grndLevel == null)
    }


    @Test(expected = SerializationException::class)
    fun currentWeatherDataJson_decode_failed_by_typeError() {
        // 文字列型要素を数値型に変換しておく
        val failJson = json.replace(""""Tokyo"""", "2")

        Json.decodeFromString<CurrentWeatherData>(failJson)
    }

    @Test(expected = SerializationException::class)
    fun currentWeatherDataJson_decode_failed_by_missingRequiredField() {
        // 必須要素を削除しておく
        val failJson = json.replace("""name": "Tokyo"""", "")

        Json.decodeFromString<CurrentWeatherData>(failJson)
    }


}
