package jp.co.yumemi.droidtraining.usecases

import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import javax.inject.Inject

class UpdateWeatherInfoDataUseCase @Inject constructor(private val weatherInfoDataRepository: WeatherInfoDataRepository) {
    val weatherInfoData = weatherInfoDataRepository.weatherInfoData

    suspend fun updateWeather(onFailed: () -> Unit) {
        try {
            weatherInfoDataRepository.updateWeatherInfoData()
        } catch (e: UnknownException) {
            onFailed()
        }
    }
}
