package com.example.weatherapi

import com.example.weatherapi.api.CurrentWeatherData
import com.example.weatherapi.api.CurrentWeatherDataService
import com.example.weatherapi.api.ForecastDataList
import com.example.weatherapi.api.OpenWeatherDataAPI
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class ForecastWeatherDataAPITest {
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
                    ),
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
                    ),
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
            ),
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

    class FakeCurrentWeatherDataService(private val response: Response<ForecastDataList>) :
        CurrentWeatherDataService {
        override suspend fun fetchCurrentWeatherData(
            apiKey: String,
            cityId: Int,
        ): Response<CurrentWeatherData> {
            throw NotImplementedError()
        }

        override suspend fun fetch5day3hourForecastData(
            apiKey: String,
            cityId: Int,
        ): Response<ForecastDataList> {
            return response
        }
    }

    @Test
    fun fetch5day3hourForecastData_isSuccess() = runTest {
        val currentWeatherDataService = FakeCurrentWeatherDataService(
            Response.success(fakeForecastDataList),
        )
        val openWeatherDataAPI = OpenWeatherDataAPI(
            "fakeApiKey",
            currentWeatherDataService = currentWeatherDataService,
        )
        val forecastDataList =
            openWeatherDataAPI.fetch5day3hourForecastData(OpenWeatherDataAPI.CityId.KUSIRO)

        // 同値チェック
        assertEquals(fakeForecastDataList, forecastDataList)
    }

    @Test(expected = Throwable::class)
    fun fetch5day3hoursForecastData_failed_by_notFound() = runTest {
        val currentWeatherDataService = FakeCurrentWeatherDataService(
            Response.error(404, "not found".toResponseBody()),
        )
        val openWeatherDataAPI = OpenWeatherDataAPI(
            "fakeApiKey",
            currentWeatherDataService = currentWeatherDataService,
        )
        openWeatherDataAPI.fetch5day3hourForecastData(OpenWeatherDataAPI.CityId.KUSIRO)
    }

    @Test(expected = Throwable::class)
    fun fetch5day3hoursForecastData_failed_by_body_empty() = runTest {
        val currentWeatherDataService = FakeCurrentWeatherDataService(
            Response.success(null),
        )
        val openWeatherDataAPI = OpenWeatherDataAPI(
            "fakeApiKey",
            currentWeatherDataService = currentWeatherDataService,
        )
        openWeatherDataAPI.fetch5day3hourForecastData(OpenWeatherDataAPI.CityId.KUSIRO)
    }
}
