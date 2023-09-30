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
) : WeatherMainViewModel(
    updateWeatherInfoDataUseCase = UpdateWeatherInfoDataUseCase(
        FakeWeatherInfoDataRepository(initialWeatherInfoData),
    ),
    getWeatherInfoDataUseCase = GetWeatherInfoDataUseCase(
        FakeWeatherInfoDataRepository(initialWeatherInfoData),
    ),
    savedStateHandle = SavedStateHandle(), // fake(empty)
)

class FakeWeatherInfoDataRepository(private val initialWeatherInfoData: WeatherInfoData) :
    WeatherInfoDataRepository {
    override val weatherInfoData: StateFlow<WeatherInfoData>
        get() = MutableStateFlow(initialWeatherInfoData).asStateFlow()

    override suspend fun updateWeatherInfoData() {
        // do nothing
    }

    override fun setWeatherInfoData(weatherInfoData: WeatherInfoData) {
        // do nothing
    }
}
