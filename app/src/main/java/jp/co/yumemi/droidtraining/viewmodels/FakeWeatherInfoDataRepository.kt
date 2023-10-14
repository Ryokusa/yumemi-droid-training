package jp.co.yumemi.droidtraining.viewmodels

import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeWeatherInfoDataRepository(
    initialWeatherInfoData: WeatherInfoData? = null,
    private val updatedWeatherInfoData: WeatherInfoData? = initialWeatherInfoData,
    initialForecastWeatherInfoDataList: List<WeatherInfoData> = listOf(),
    private val updatedForecastWeatherInfoDataList: List<WeatherInfoData> = initialForecastWeatherInfoDataList,
) :
    WeatherInfoDataRepository {
    private val _weatherInfoData = MutableStateFlow(initialWeatherInfoData)
    override val weatherInfoData: StateFlow<WeatherInfoData?>
        get() = _weatherInfoData.asStateFlow()

    private val _foreCastWeatherInfoDataList = MutableStateFlow(initialForecastWeatherInfoDataList)
    override val forecastWeatherInfoDataList: StateFlow<List<WeatherInfoData>>
        get() = _foreCastWeatherInfoDataList.asStateFlow()

    override suspend fun updateWeatherInfoData() {
        _weatherInfoData.value = updatedWeatherInfoData
    }

    override suspend fun updateForecastWeatherInfoDataList() {
        _foreCastWeatherInfoDataList.value = updatedForecastWeatherInfoDataList
    }

    override fun setWeatherInfoData(weatherInfoData: WeatherInfoData) {
        // do nothing
    }
}
