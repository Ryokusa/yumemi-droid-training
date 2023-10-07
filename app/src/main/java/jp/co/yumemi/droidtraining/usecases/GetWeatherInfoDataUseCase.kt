package jp.co.yumemi.droidtraining.usecases

import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetWeatherInfoDataUseCase @Inject constructor(
    private val weatherInfoDataRepository: WeatherInfoDataRepository,
) {
    operator fun invoke(): StateFlow<WeatherInfoData> {
        return weatherInfoDataRepository.weatherInfoData
    }
}
