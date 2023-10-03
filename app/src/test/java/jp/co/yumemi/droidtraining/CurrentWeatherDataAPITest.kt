package jp.co.yumemi.droidtraining

import com.example.weatherapi.api.CurrentWeatherDataAPI
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CurrentWeatherDataAPITest {
    @Test
    fun canFetchCurrentWeatherData() = runTest {
        val api = CurrentWeatherDataAPI(BuildConfig.API_KEY)
        val cityId = CurrentWeatherDataAPI.CityId.NAGOYA
        val weatherData = api.fetchCurrentWeatherData(cityId = cityId)
        assert(weatherData.id == cityId.id.toLong())
    }

    @Test
    fun canFetch5day3hourForecastData() = runTest {
        val api = CurrentWeatherDataAPI(BuildConfig.API_KEY)
        val cityId = CurrentWeatherDataAPI.CityId.NAGOYA
        val weatherData = api.fetch5day3hourForecastData(cityId = cityId)
        assert(weatherData.city.id == cityId.id.toLong())
    }
}
