package jp.co.yumemi.droidtraining

import com.example.weatherapi.api.CurrentWeatherData
import com.example.weatherapi.api.CurrentWeatherDataAPI
import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDateTime

class WeatherInfoDataRepositoryTest {
    class FakeCurrentWeatherDataAPI(private val _fetchCurrentWeatherData: (cityId: CurrentWeatherDataAPI.CityId) -> CurrentWeatherData) :
        CurrentWeatherDataAPI {
        override suspend fun fetchCurrentWeatherData(cityId: CurrentWeatherDataAPI.CityId): CurrentWeatherData {
            return _fetchCurrentWeatherData(cityId)
        }
    }

    // 加工して使う用
    private val currentWeatherData = CurrentWeatherData(
        coord = CurrentWeatherData.Coord(
            lon = 139.6917,
            lat = 35.6895,
        ),
        weather = listOf(
            CurrentWeatherData.Weather(
                id = 803,
                main = "Clouds",
                description = "broken clouds",
                icon = "04n",
            ),
        ),
        base = "stations",
        main = CurrentWeatherData.Main(
            temp = 289.5,
            feelsLike = 289.52,
            tempMin = 288.82,
            tempMax = 290.15,
            pressure = 1013,
            humidity = 89,
        ),
        visibility = 10000,
        wind = CurrentWeatherData.Wind(
            speed = 0.45,
            deg = 0,
            gust = 1.34,
        ),
        clouds = CurrentWeatherData.Clouds(
            all = 75,
        ),
        dt = 1630548912,
        sys = CurrentWeatherData.Sys(
            type = 1,
            id = 8074,
            country = "JP",
            sunrise = 1630499316,
            sunset = 1630544869,
        ),
        timezone = 32400,
        id = 1850147,
        name = "Tokyo",
        cod = 200,
    )

    @Test
    fun isSameInitialWeatherInfoData() = runTest {
        val initialWeatherInfoData = WeatherInfoData(
            weather = WeatherType.SUNNY,
            lowestTemperature = 5,
            highestTemperature = 40,
            place = "岐阜",
            temperature = 10,
            dateTime = LocalDateTime.now(),
        )
        val repository = WeatherInfoDataRepositoryImpl(
            initialWeatherInfoData = initialWeatherInfoData,
            currentWeatherDataAPI = FakeCurrentWeatherDataAPI { currentWeatherData },
        )
        assert(repository.weatherInfoData.value == initialWeatherInfoData)
    }

    @Test
    fun canUpdateWeatherInfoData() = runTest {
        val initialWeatherInfoData = WeatherInfoData(
            weather = WeatherType.RAINY,
            lowestTemperature = 5,
            highestTemperature = 40,
            place = "岐阜",
            temperature = 10,
            dateTime = LocalDateTime.now(),
        )

        // フェッチされる天気情報
        val newWeatherInfoData = WeatherInfoData(
            weather = WeatherType.SUNNY,
            lowestTemperature = 20,
            highestTemperature = 50,
            place = "東京",
            temperature = 30,
            dateTime = LocalDateTime.of(2021, 1, 1, 12, 0),
        )
        val newWeather = currentWeatherData.weather[0].copy(id = 800) // SUNNY id
        val newMain = currentWeatherData.main.copy(
            tempMin = newWeatherInfoData.lowestTemperature.toDouble(),
            tempMax = newWeatherInfoData.highestTemperature.toDouble(),
        )
        val newCurrentWeatherData = currentWeatherData.copy(
            weather = listOf(newWeather),
            main = newMain,
            name = newWeatherInfoData.place,
        )

        // テスト
        val repository = WeatherInfoDataRepositoryImpl(
            initialWeatherInfoData = initialWeatherInfoData,
            currentWeatherDataAPI = FakeCurrentWeatherDataAPI { newCurrentWeatherData },
        )
        repository.updateWeatherInfoData()
        assert(repository.weatherInfoData.value == newWeatherInfoData)
    }

    @Test(expected = UnknownException::class)
    fun updateWeatherInfoData_isFailed() = runTest {
        val initialWeatherInfoData = WeatherInfoData(
            weather = WeatherType.RAINY,
            lowestTemperature = 5,
            highestTemperature = 40,
            place = "岐阜",
            temperature = 10,
            dateTime = LocalDateTime.now(),
        )
        val repository = WeatherInfoDataRepositoryImpl(
            initialWeatherInfoData = initialWeatherInfoData,
            currentWeatherDataAPI = FakeCurrentWeatherDataAPI { throw Exception() },
        )
        repository.updateWeatherInfoData()
    }
}
