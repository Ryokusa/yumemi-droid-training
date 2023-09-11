package jp.co.yumemi.droidtraining

import jp.co.yumemi.api.UnknownException

enum class WeatherType(val weather: String) {
    SUNNY("sunny"),
    CLOUDY("cloudy"),
    RAINY("rainy"),
    SNOW("snow");

    companion object {
        /**
         * weatherからWeatherTypeを取得
         *
         * @throws NoSuchElementException 要素がない場合
         */
        fun of(weather: String): WeatherType = values().first { it.weather == weather }

        fun fromCurrentWeatherDataId(weatherId: Long): WeatherType {
            if (weatherId.toInt() == 800) return SUNNY
            return when (weatherId.toString().substring(0, 1).toInt()) {
                2, 3, 5 -> RAINY
                6 -> SNOW
                7, 8 -> CLOUDY
                else -> throw UnknownException()
            }
        }
    }
}
