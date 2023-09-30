package jp.co.yumemi.droidtraining.usecases

import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import javax.inject.Inject

class UpdateWeatherInfoDataUseCase @Inject constructor(private val weatherInfoDataRepository: WeatherInfoDataRepository) {
    suspend operator fun invoke(onFailed: () -> Unit) {
        try {
            weatherInfoDataRepository.updateWeatherInfoData()
        } catch (e: UnknownException) {
            onFailed()
        }
    }
}
