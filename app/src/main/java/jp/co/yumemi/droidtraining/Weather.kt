package jp.co.yumemi.droidtraining

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
        fun of(weather: String): WeatherType = values().first{ it.weather == weather }
    }
}
