package jp.co.yumemi.droidtraining

import com.example.weatherapi.api.OpenWeatherDataAPI
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OpenWeatherDataAPITest {
    @Test
    fun canFetchCurrentWeatherData() = runTest {
        val api = OpenWeatherDataAPI(BuildConfig.API_KEY)
        val cityId = OpenWeatherDataAPI.CityId.NAGOYA
        val weatherData = api.fetchCurrentWeatherData(cityId = cityId)
        assert(weatherData.id == cityId.id.toLong())
    }

    @Test
    fun canFetch5day3hourForecastData() = runTest {
        val api = OpenWeatherDataAPI(BuildConfig.API_KEY)
        val cityId = OpenWeatherDataAPI.CityId.NAGOYA
        val weatherData = api.fetch5day3hourForecastData(cityId = cityId)
        assert(weatherData.city.id == cityId.id.toLong())
    }
}
