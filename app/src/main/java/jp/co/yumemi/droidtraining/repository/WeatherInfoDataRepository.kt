package jp.co.yumemi.droidtraining.repository

import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.api.YumemiWeather
import jp.co.yumemi.droidtraining.WeatherType
import jp.co.yumemi.droidtraining.model.JsonWeatherInfoData
import jp.co.yumemi.droidtraining.model.WeatherInfoData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class WeatherInfoDataRepository @Inject constructor(
    private val weatherApi: YumemiWeather,
    initialWeatherInfoData: WeatherInfoData = WeatherInfoData(
        weather = WeatherType.SUNNY,
        lowestTemperature = 5,
        highestTemperature = 40,
        place = "岐阜"
    ),
    private val fetchDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val _weatherInfoData = MutableStateFlow(initialWeatherInfoData)
    val weatherInfoData = _weatherInfoData.asStateFlow()


    /** 天気情報取得
     * @return 新しい天気情報
     * @throws UnknownException 天気取得できなかった場合
     */
    private suspend fun fetchWeatherInfoData(): WeatherInfoData {
        val newJsonWeatherInfoData = withContext(fetchDispatcher) {
            val requestJson = """ { "area" : "東京", "date": "2020-04-01T12:00"} """.trimMargin()
            val jsonStr = weatherApi.fetchJsonWeather(requestJson)
            return@withContext Json.decodeFromString<JsonWeatherInfoData>(jsonStr)
        }
        _weatherInfoData.value = WeatherInfoData(newJsonWeatherInfoData)
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
