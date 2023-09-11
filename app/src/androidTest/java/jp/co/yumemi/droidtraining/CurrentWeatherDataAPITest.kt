package jp.co.yumemi.droidtraining

import jp.co.yumemi.droidtraining.api.CurrentWeatherDataAPI
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CurrentWeatherDataAPITest {
    @Test
    fun canFetchCurrentWeatherData() = runTest {
        val api = CurrentWeatherDataAPI()
        val cityId = CurrentWeatherDataAPI.Companion.CityId.NAGOYA
        val weatherData = api.fetchCurrentWeatherData(cityId = cityId)
        assert(weatherData.id == cityId.id.toLong())
    }
}
