package jp.co.yumemi.droidtraining.repository

import com.example.weatherapi.api.OpenWeatherDataAPI
import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.droidtraining.WeatherType
import jp.co.yumemi.droidtraining.model.WeatherInfoData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

interface WeatherInfoDataRepository {
    val weatherInfoData: StateFlow<WeatherInfoData>
    val forecastWeatherInfoDataList: StateFlow<List<WeatherInfoData>>
    suspend fun updateWeatherInfoData()

    /**
     * 予報情報を取得・更新
     * @throws UnknownException 天気取得できなかった場合
     */
    suspend fun updateForecastWeatherInfoDataList()

    fun setWeatherInfoData(weatherInfoData: WeatherInfoData)
}

class WeatherInfoDataRepositoryImpl @Inject constructor(
    initialWeatherInfoData: WeatherInfoData = WeatherInfoData(
        weather = WeatherType.SUNNY,
        lowestTemperature = 5,
        highestTemperature = 40,
        place = "岐阜",
        temperature = 10,
        dateTime = LocalDateTime.now(),
    ),
    private val openWeatherDataAPI: OpenWeatherDataAPI,
    private val fetchDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : WeatherInfoDataRepository {

    private val _weatherInfoData = MutableStateFlow(initialWeatherInfoData)
    override val weatherInfoData = _weatherInfoData.asStateFlow()

    private val _forecastWeatherInfoDataList = MutableStateFlow(listOf<WeatherInfoData>())
    override val forecastWeatherInfoDataList = _forecastWeatherInfoDataList.asStateFlow()

    /** 天気情報取得
     * @return 新しい天気情報
     * @throws UnknownException 天気取得できなかった場合
     */
    private suspend fun fetchWeatherInfoData(): WeatherInfoData {
        try {
            val currentWeatherData = withContext(fetchDispatcher) {
                val cityId = OpenWeatherDataAPI.CityId.NAGOYA
                return@withContext openWeatherDataAPI.fetchCurrentWeatherData(cityId)
            }
            _weatherInfoData.value = WeatherInfoData(currentWeatherData)
        } catch (e: Exception) {
            throw UnknownException()
        }
        return _weatherInfoData.value
    }

    private suspend fun fetchForecastWeatherInfoDataList(): List<WeatherInfoData> {
        _forecastWeatherInfoDataList.value = listOf()
        try {
            val forecastDataList = withContext(fetchDispatcher) {
                val cityId = OpenWeatherDataAPI.CityId.NAGOYA
                return@withContext WeatherInfoData(
                    openWeatherDataAPI.fetch5day3hourForecastData(
                        cityId
                    )
                )
            }
            _forecastWeatherInfoDataList.value = forecastDataList
        } catch (e: Exception) {
            throw UnknownException()
        }
        return _forecastWeatherInfoDataList.value
    }

    /**
     * 天気情報を更新
     * 予報情報はリセット
     * @throws UnknownException 天気取得できなかった場合
     */
    override suspend fun updateWeatherInfoData() {
        _forecastWeatherInfoDataList.value = listOf()
        _weatherInfoData.value = fetchWeatherInfoData()
    }


    override suspend fun updateForecastWeatherInfoDataList() {
        _forecastWeatherInfoDataList.value = fetchForecastWeatherInfoDataList()
    }

    override fun setWeatherInfoData(weatherInfoData: WeatherInfoData) {
        _weatherInfoData.value = weatherInfoData
    }
}
