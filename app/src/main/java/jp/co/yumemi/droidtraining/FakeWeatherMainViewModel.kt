package jp.co.yumemi.droidtraining

import androidx.lifecycle.SavedStateHandle
import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import jp.co.yumemi.droidtraining.usecases.GetForecastWeatherInfoDataUseCase
import jp.co.yumemi.droidtraining.usecases.GetWeatherInfoDataUseCase
import jp.co.yumemi.droidtraining.usecases.UpdateForecastWeatherInfoDataListUseCase
import jp.co.yumemi.droidtraining.usecases.UpdateWeatherInfoDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 以下はプレビュー用のフェイク（天気情報初期値を設定できる）
class FakeWeatherMainViewModel(
    initialWeatherInfoData: WeatherInfoData?,
    updatedWeatherInfoData: WeatherInfoData? = initialWeatherInfoData,
    fakeWeatherInfoDataRepository: WeatherInfoDataRepository = FakeWeatherInfoDataRepository(
        initialWeatherInfoData,
        updatedWeatherInfoData,
    ),
) : WeatherMainViewModel(
    updateWeatherInfoDataUseCase = UpdateWeatherInfoDataUseCase(
        fakeWeatherInfoDataRepository,
    ),
    getWeatherInfoDataUseCase = GetWeatherInfoDataUseCase(
        fakeWeatherInfoDataRepository,
    ),
    updateForecastWeatherInfoDataListUseCase = UpdateForecastWeatherInfoDataListUseCase(
        fakeWeatherInfoDataRepository,
    ),
    getForecastWeatherInfoDataUseCase = GetForecastWeatherInfoDataUseCase(
        fakeWeatherInfoDataRepository,
    ),
    savedStateHandle = SavedStateHandle(), // fake(empty)
)

class FakeWeatherInfoDataRepository(
    private val initialWeatherInfoData: WeatherInfoData? = null,
    private val updatedWeatherInfoData: WeatherInfoData? = initialWeatherInfoData,
) :
    WeatherInfoDataRepository {
    private val _weatherInfoData = MutableStateFlow(initialWeatherInfoData)
    override val weatherInfoData: StateFlow<WeatherInfoData?>
        get() = _weatherInfoData.asStateFlow()

    private val _foreCastWeatherInfoDataList = MutableStateFlow(listOf<WeatherInfoData>())
    override val forecastWeatherInfoDataList: StateFlow<List<WeatherInfoData>>
        get() = _foreCastWeatherInfoDataList.asStateFlow()

    override suspend fun updateWeatherInfoData() {
        _weatherInfoData.value = updatedWeatherInfoData
    }

    override suspend fun updateForecastWeatherInfoDataList() {
        TODO("Not yet implemented")
    }

    override fun setWeatherInfoData(weatherInfoData: WeatherInfoData) {
        // do nothing
    }
}
