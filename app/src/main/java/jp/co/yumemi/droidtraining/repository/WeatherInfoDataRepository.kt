package jp.co.yumemi.droidtraining.repository

import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.api.YumemiWeather
import jp.co.yumemi.droidtraining.WeatherInfoData
import jp.co.yumemi.droidtraining.WeatherType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class WeatherInfoDataRepository @Inject constructor (
    private val weatherApi: YumemiWeather, initialWeatherInfoData: WeatherInfoData
) {
    private val _weatherInfoData = MutableStateFlow(initialWeatherInfoData)
    val weatherInfoData = _weatherInfoData.asStateFlow()


    /** 天気情報取得
     * @return 新しい天気情報
     * @throws UnknownException 天気取得できなかった場合
     */
    private fun fetchWeatherInfoData(): WeatherInfoData {
        val newWeatherStr = weatherApi.fetchThrowsWeather()
        try {
            val newWeather = WeatherType.of(newWeatherStr)
            return _weatherInfoData.value.copy(weather = newWeather)
        } catch (e: NoSuchElementException){
            // 該当するWeatherTypeがない場合
            throw UnknownException()
        }
    }

    /**
     * 天気情報を更新
     * @throws UnknownException 天気取得できなかった場合
     */
    fun updateWeatherInfoData() {
        _weatherInfoData.value = fetchWeatherInfoData()
    }

    fun setWeatherInfoData(weatherInfoData: WeatherInfoData) {
        _weatherInfoData.value = weatherInfoData
    }
}
