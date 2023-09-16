package jp.co.yumemi.droidtraining.repository

import com.example.weatherapi.api.CurrentWeatherDataAPI
import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.droidtraining.WeatherType
import jp.co.yumemi.droidtraining.model.WeatherInfoData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherInfoDataRepository @Inject constructor(
    initialWeatherInfoData: WeatherInfoData = WeatherInfoData(
        weather = WeatherType.SUNNY,
        lowestTemperature = 5,
        highestTemperature = 40,
        place = "岐阜"
    ),
    private val currentWeatherDataAPI: CurrentWeatherDataAPI,
    private val fetchDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val _weatherInfoData = MutableStateFlow(initialWeatherInfoData)
    val weatherInfoData = _weatherInfoData.asStateFlow()


    /** 天気情報取得
     * @return 新しい天気情報
     * @throws UnknownException 天気取得できなかった場合
     */
    private suspend fun fetchWeatherInfoData(): WeatherInfoData {
        val currentWeatherData = withContext(fetchDispatcher) {
            val cityId = CurrentWeatherDataAPI.Companion.CityId.NAGOYA
            return@withContext currentWeatherDataAPI.fetchCurrentWeatherData(cityId)
        }
        _weatherInfoData.value = WeatherInfoData(currentWeatherData)
        return _weatherInfoData.value
    }

    /**
     * 天気情報を更新
     * @throws UnknownException 天気取得できなかった場合
     */
    suspend fun updateWeatherInfoData() {
        _weatherInfoData.value = fetchWeatherInfoData()
    }

    fun setWeatherInfoData(weatherInfoData: WeatherInfoData) {
        _weatherInfoData.value = weatherInfoData
    }
}
