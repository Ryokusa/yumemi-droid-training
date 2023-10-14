package jp.co.yumemi.droidtraining.components

enum class Route {
    WeatherMain,
    WeatherFetchErrorDialog,
    ;

    enum class WeatherDetail {
        Main,
        ForecastWeatherFetchErrorDialog,
    }
}
