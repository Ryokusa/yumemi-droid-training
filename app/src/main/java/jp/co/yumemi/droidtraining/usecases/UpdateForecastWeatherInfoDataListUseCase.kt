package jp.co.yumemi.droidtraining.usecases

import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import javax.inject.Inject

class UpdateForecastWeatherInfoDataListUseCase @Inject constructor(
    private val weatherInfoDataRepository: WeatherInfoDataRepository
) {
    suspend operator fun invoke(onFailed: () -> Unit) {
        try {
            weatherInfoDataRepository.updateForecastWeatherInfoDataList()
        } catch (e: Exception) {
            onFailed()
        }
    }
}
