package com.example.weatherapi

import com.example.weatherapi.api.CurrentWeatherData
import com.example.weatherapi.api.CurrentWeatherDataAPI
import com.example.weatherapi.api.CurrentWeatherDataService
import com.example.weatherapi.api.retrofit
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior


class CurrentWeatherDataAPITest {
    private val fakeCurrentWeatherData = CurrentWeatherData(
        coord = CurrentWeatherData.Coord(0.0f.toDouble(), 0.0f.toDouble()),
        weather = listOf(CurrentWeatherData.Weather(0, "", "", "")),
        base = "",
        main = CurrentWeatherData.Main(
            0.0f.toDouble(),
            0.0f.toDouble(),
            0.0f.toDouble(),
            0.0f.toDouble(),
            0,
            0
        ),
        visibility = 0,
        wind = CurrentWeatherData.Wind(0.0f.toDouble(), 0L, 0.0f.toDouble()),
        clouds = CurrentWeatherData.Clouds(0),
        dt = 0,
        sys = CurrentWeatherData.Sys(0, 0, "", 0, 0),
        timezone = 0,
        id = 0,
        name = "name",
        cod = 0
    )

    class MockCurrentWeatherDataService(
        private val delegate: BehaviorDelegate<CurrentWeatherDataService>,
        private val response: Response<CurrentWeatherData>
    ) : CurrentWeatherDataService {
        override suspend fun fetchCurrentWeatherData(
            apiKey: String,
            cityId: Int
        ): Response<CurrentWeatherData> {
            return delegate.returningResponse(response).fetchCurrentWeatherData(apiKey, cityId)
        }
    }

    private val behavior = NetworkBehavior.create()
    private val mockRetrofit: MockRetrofit = MockRetrofit.Builder(retrofit)
        .networkBehavior(behavior)
        .build()
    private val delegate = mockRetrofit.create(CurrentWeatherDataService::class.java)

    private fun createCurrentWeatherDataService(response: Response<CurrentWeatherData>): CurrentWeatherDataService {
        return MockCurrentWeatherDataService(delegate, response)
    }

    @Test
    fun fetchCurrentWeatherData_isSuccess() = runTest {
        val currentWeatherDataService = createCurrentWeatherDataService(
            Response.success(fakeCurrentWeatherData)
        )
        val currentWeatherDataAPI = CurrentWeatherDataAPI(
            "fakeApiKey", currentWeatherDataService = currentWeatherDataService
        )
        val currentWeatherData =
            currentWeatherDataAPI.fetchCurrentWeatherData(CurrentWeatherDataAPI.CityId.KUSIRO)

        // 同値チェック
        assert(currentWeatherData == fakeCurrentWeatherData)
    }

    @Test(expected = Throwable::class)
    fun fetchCurrentWeatherData_isFailed_by_not_found() = runTest {
        val currentWeatherDataService = createCurrentWeatherDataService(
            Response.error(404, "not found".toResponseBody())
        )
        val currentWeatherDataAPI = CurrentWeatherDataAPI(
            "fakeApiKey", currentWeatherDataService = currentWeatherDataService
        )

        currentWeatherDataAPI.fetchCurrentWeatherData(CurrentWeatherDataAPI.CityId.KUSIRO)
    }

    @Test(expected = Throwable::class)
    fun fetchCurrentWeatherData_isFailed_by_body_empty() = runTest {
        val currentWeatherDataService = createCurrentWeatherDataService(
            Response.success(null)
        )
        val currentWeatherDataAPI = CurrentWeatherDataAPI(
            "fakeApiKey", currentWeatherDataService = currentWeatherDataService
        )

        currentWeatherDataAPI.fetchCurrentWeatherData(CurrentWeatherDataAPI.CityId.KUSIRO)
    }
}
