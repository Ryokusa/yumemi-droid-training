package jp.co.yumemi.droidtraining

import androidx.lifecycle.SavedStateHandle
import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import jp.co.yumemi.droidtraining.usecases.GetWeatherInfoDataUseCase
import jp.co.yumemi.droidtraining.usecases.UpdateWeatherInfoDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 以下はプレビュー用のフェイク（天気情報初期値を設定できる）
class FakeWeatherMainViewModel(
    initialWeatherInfoData: WeatherInfoData,
    updatedWeatherInfoData: WeatherInfoData = initialWeatherInfoData,
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
    savedStateHandle = SavedStateHandle(), // fake(empty)
)

class FakeWeatherInfoDataRepository(
    private val initialWeatherInfoData: WeatherInfoData,
    private val updatedWeatherInfoData: WeatherInfoData = initialWeatherInfoData,
) :
    WeatherInfoDataRepository {
    private val _weatherInfoData = MutableStateFlow(initialWeatherInfoData)
    override val weatherInfoData: StateFlow<WeatherInfoData>
        get() = _weatherInfoData.asStateFlow()

    override suspend fun updateWeatherInfoData() {
        _weatherInfoData.value = updatedWeatherInfoData
    }

    override fun setWeatherInfoData(weatherInfoData: WeatherInfoData) {
        // do nothing
    }
}